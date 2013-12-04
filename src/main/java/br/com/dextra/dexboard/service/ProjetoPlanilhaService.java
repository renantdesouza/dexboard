package br.com.dextra.dexboard.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaPrincipal;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.JsonObject;

public class ProjetoPlanilhaService {

	public final static Logger LOG = LoggerFactory
			.getLogger(ProjetoPlanilhaService.class);

	public static final PMAService SERVICO_PMA_AMAZON = new PMAService(
			"https://pma.dextra.com.br/pma/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	public static final PMAService SERVICO_PMA_LOCAL = new PMAService(
			"http://localhost:3000/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	public static final PMAService SERVICO_PMA = SERVICO_PMA_AMAZON;

	public static Map<Long, Projeto> buscarDadosProjetosAtivos() {
		PlanilhaPrincipal planilhaPrincipal = new PlanilhaPrincipal();

		Map<Long, Projeto> projetos = planilhaPrincipal.buscarDadosDosProjetos();

		/*
		for (Projeto projeto : projetos.values()) {
			JsonObject dados = SERVICO_PMA.buscarDadosDoProjeto(projeto
					.getIdPma().intValue());
			projeto.setCpi(dados.get("cpi").getAsDouble());
		}*/

		return projetos;
	}

	public static void limparCache() {
		MemcacheService cache = MemcacheServiceFactory
				.getMemcacheService("dados-projetos");
		cache.delete("dados-projetos");
	}

}
