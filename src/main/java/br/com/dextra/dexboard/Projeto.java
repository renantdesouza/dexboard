package br.com.dextra.dexboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.restitory.gae.filter.util.JsonUtil;

public class Projeto {

	private static final int CACHE_EXPIRATION_SECONDS = 60 * 120;

	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(Projeto.class);

	private static final ServicoPma SERVICO_PMA_AMAZON = new ServicoPma(
			"https://50.17.210.152/pma/services/indicadores", "ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	@SuppressWarnings("unused")
	private static final ServicoPma SERVICO_PMA_LOCAL = new ServicoPma("http://localhost:3000/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	private static final ServicoPma SERVICO_PMA = SERVICO_PMA_AMAZON;

	public static JsonArray buscarDadosProjetos() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
		String json = (String) cache.get("dados-projetos");
		LOG.info("Cache hit: " + (json != null));
		if (json != null) {
			return (JsonArray) JsonUtil.parse(json);
		}
		LOG.info("Criando planilhaDexboard");
		PlanilhaDexboard planilhaDexboard = new PlanilhaDexboard("0Au2Lk990DvFfdGVDQm9rTW1OYmw3dW5yOUVQSkdPSGc");
		LOG.info("buscarDadosProjetos");
		JsonArray dadosPlanilha = planilhaDexboard.buscarDadosDosProjetos();
		LOG.info("criando json");
		JsonArray obj = new JsonArray();
		for (JsonElement elemento : dadosPlanilha) {
			int idProjeto = Integer.parseInt(elemento.getAsJsonObject().get("id").getAsString());
			JsonObject dados = SERVICO_PMA.buscarDadosDoProjeto(idProjeto);
			Utils.mesclar(dados, elemento.getAsJsonObject());
			obj.add(dados);
		}
		LOG.info("json criado");
		cache.put("dados-projetos", obj.toString(), Expiration.byDeltaSeconds(CACHE_EXPIRATION_SECONDS),
				SetPolicy.ADD_ONLY_IF_NOT_PRESENT);
		return obj;
	}

	public static void limparCache() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
		cache.delete("dados-projetos");
	}

}
