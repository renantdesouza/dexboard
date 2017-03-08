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

	public static Map<Long, Projeto> buscarDadosProjetosAtivos() {
		return PlanilhaFactory.principal().buscarDadosDosProjetos();
	}

	public static void limparCache() {
		MemcacheServiceFactory.getMemcacheService("dados-projetos").delete("dados-projetos");
	}

}
