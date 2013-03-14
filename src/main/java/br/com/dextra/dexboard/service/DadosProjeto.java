package br.com.dextra.dexboard.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaDexboard;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.JsonObject;

public class DadosProjeto {

	public final static Logger LOG = LoggerFactory.getLogger(DadosProjeto.class);

	public static final ServicoPma SERVICO_PMA_AMAZON = new ServicoPma(
			"https://50.17.210.152/pma/services/indicadores", "ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	public static final ServicoPma SERVICO_PMA_LOCAL = new ServicoPma("http://localhost:3000/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	public static final ServicoPma SERVICO_PMA = SERVICO_PMA_AMAZON;

	public static List<Projeto> buscarDadosProjetos() {
		LOG.info("Criando planilhaDexboard");
		PlanilhaDexboard planilhaDexboard = new PlanilhaDexboard("0Au2Lk990DvFfdGVDQm9rTW1OYmw3dW5yOUVQSkdPSGc");
		LOG.info("buscarDadosProjetos");
		List<Projeto> projetos = planilhaDexboard.buscarDadosDosProjetos();
		LOG.info("buscarDadosPma");
		for (Projeto p : projetos) {
			JsonObject dados = SERVICO_PMA.buscarDadosDoProjeto(p.getIdPma());
			p.setCpi(dados.get("cpi").getAsDouble());
			p.setUltimaAlteracao(new Date());
		}

		return projetos;
	}

	public static void limparCache() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
		cache.delete("dados-projetos");
	}

}
