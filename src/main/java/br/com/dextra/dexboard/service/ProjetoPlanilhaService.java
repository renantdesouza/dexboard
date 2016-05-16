package br.com.dextra.dexboard.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaFactory;
import br.com.dextra.dexboard.planilha.PlanilhaPrincipal;

public class ProjetoPlanilhaService {

	public final static Logger LOG = LoggerFactory.getLogger(ProjetoPlanilhaService.class);

	public static Map<Long, Projeto> buscarDadosProjetosAtivos() {
		PlanilhaPrincipal planilhaPrincipal = PlanilhaFactory.principal();
		return planilhaPrincipal.buscarDadosDosProjetos();
	}

	public static void limparCache() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
		cache.delete("dados-projetos");
	}

}
