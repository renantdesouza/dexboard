
package br.com.dextra.dexboard.service;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaFactory;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.util.Map;

public class ProjetoPlanilhaService {

	public static Map<Long, Projeto> buscarDadosProjetosAtivos() {
		return PlanilhaFactory.principal().buscarDadosDosProjetos();
	}

	public static void limparCache() {
		MemcacheServiceFactory.getMemcacheService("dados-projetos").delete("dados-projetos");
	}

}
