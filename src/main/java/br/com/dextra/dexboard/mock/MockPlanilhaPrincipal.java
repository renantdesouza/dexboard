package br.com.dextra.dexboard.mock;

import java.util.HashMap;
import java.util.Map;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaPrincipal;

public class MockPlanilhaPrincipal implements PlanilhaPrincipal {

	@Override
	public Map<Long, Projeto> buscarDadosDosProjetos() {
		Map<Long, Projeto> map = new HashMap<>();

		// TODO: trazer da build
		String email = "fernando@dextra-sw.com";

		map.put(495l, p(495l, "A4C", "Chaos", email, 1.01d, 10, 8, 9, 6));
		map.put(565l, p(565l, "Confidence", "Rocket", email, 0.99d, 10, 8, 9, 6));
		map.put(530l, p(530l, "DPaschoal", "Heisenberg", email, 1.05d, 10, 8, 9, 6));
		map.put(568l, p(568l, "FAB SDAB", "Mustache", email, 0.92d, 10, 8, 9, 6));
		map.put(521l, p(521l, "Certics", "Buzz", email, 0.99d, 10, 8, 9, 6));
		map.put(537l, p(537l, "Globosat Scrum", "Walking", email, 1.00d, 10, 8, 9, 6));
		map.put(441l, p(441l, "ICTS: Manutenção", "Mustache", email, 1.05d, 10, 8, 9, 6));
		map.put(571l, p(571l, "Trópico", "Tropico", email, 1.07d, 10, 8, 9, 6));
		map.put(517l, p(517l, "Marinha", "Chuck", email, 0.59d, 10, 8, 9, 6));
		map.put(542l, p(542l, "Portal Doc", "Buzz", email, 1.08d, 10, 8, 9, 6));
		map.put(543l, p(543l, "Poli.TIC.Sys", "Buzz", email, 1.00d, 10, 8, 9, 6));
		map.put(549l, p(549l, "Jequiti", "SS", email, 1.02d, 10, 8, 9, 6));
		map.put(555l, p(555l, "Unimed CG", "Unimed", email, 0.95d, 10, 8, 9, 6));
		map.put(556l, p(556l, "Pósitron", "Positron", email, 0.85d, 10, 8, 9, 6));
		map.put(564l, p(564l, "CMB - Autor. Cert.", "Buzz", email, 1.01d, 10, 8, 9, 6));
		map.put(563l, p(563l, "Integral: Busca Multimídia", "Walking", email, 0.99d, 10, 8, 9, 6));
		map.put(553l, p(553l, "Move+ Suporte", "Minions", email, 1.20d, 10, 8, 9, 6));
		map.put(566l, p(566l, "CMB - Insumos", "Minions", email, 0.97d, 10, 8, 9, 6));
		map.put(569l, p(569l, "SMARAPD", "Heisenberg", email, 0.98d, 10, 8, 9, 6));
		map.put(579l, p(579l, "Movile", "Rocket", email, 1.70d, 10, 8, 9, 6));
		map.put(583l, p(583l, "Wareline: Editor", "Mustache", email, 1.18d, 10, 8, 9, 6));
		map.put(585l, p(585l, "ADV: Fase II", "Mustache", email, 1.39d, 10, 8, 9, 6));
		map.put(24l, p(24l, "TI Dextra", "TI", email, 1.00d, 10, 8, 9, 6));

		return map;
	}

	private static Projeto p(long id, String nome, String equipe, String email, double cpi,
							 double satisfacaoCliente, double satisfacaoEquipe,
							 double qualidadeTecnica, double ux) {
		return new Projeto(id, nome, equipe, email, cpi, satisfacaoCliente, satisfacaoEquipe, qualidadeTecnica, ux);
	}

}
