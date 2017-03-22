package br.com.dextra.dexboard.planilha;

import java.util.Map;

import br.com.dextra.dexboard.domain.Projeto;

public interface PlanilhaPrincipal {

	Map<Long, Projeto> buscarDadosDosProjetos();
	
}
