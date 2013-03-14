package br.com.dextra.dexboard.old;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.service.ServicoPma;
import br.com.dextra.dexboard.service.Utils;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Projeto {

	public final static Logger LOG = LoggerFactory.getLogger(Projeto.class);

	@SuppressWarnings("unused")
	public static final ServicoPma SERVICO_PMA_AMAZON = new ServicoPma(
			"https://50.17.210.152/pma/services/indicadores", "ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	public static final ServicoPma SERVICO_PMA_LOCAL = new ServicoPma("http://localhost:3000/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	public static final ServicoPma SERVICO_PMA = SERVICO_PMA_AMAZON;

	public static JsonArray buscarDadosProjetos() {
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
		JsonObject data = new JsonObject();

		TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
		TimeZone.setDefault(tz);
		Calendar ca = GregorianCalendar.getInstance(tz);

		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String modified = formatador.format(ca.getTime());
		data.addProperty("lastModified", modified);

		obj.add(data);
		// cache.put("dados-projetos", obj.toString(),
		// Expiration.byDeltaSeconds(CACHE_EXPIRATION_SECONDS),
		// SetPolicy.ADD_ONLY_IF_NOT_PRESENT);
		return obj;
	}

	public static void limparCache() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
		cache.delete("dados-projetos");
	}

}
