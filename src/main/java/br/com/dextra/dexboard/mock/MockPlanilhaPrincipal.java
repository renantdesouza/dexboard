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
		
		map.put(495l, p(495l, "A4C", "Chaos", email, 1.01d));
		map.put(565l, p(565l, "Confidence", "Rocket", email, 0.99d));
		map.put(530l, p(530l, "DPaschoal", "Heisenberg", email, 1.05d));
		map.put(568l, p(568l, "FAB SDAB", "Mustache", email, 0.92d));
		map.put(521l, p(521l, "Certics", "Buzz", email, 0.99d));
		map.put(537l, p(537l, "Globosat Scrum", "Walking", email, 1.00d));
		map.put(441l, p(441l, "ICTS: Manutenção", "Mustache", email, 1.05d));
		map.put(571l, p(571l, "Trópico", "Tropico", email, 1.07d));
		map.put(517l, p(517l, "Marinha", "Chuck", email, 0.59d));
		map.put(542l, p(542l, "Portal Doc", "Buzz", email, 1.08d));
		map.put(543l, p(543l, "Poli.TIC.Sys", "Buzz", email, 1.00d));
		map.put(549l, p(549l, "Jequiti", "SS", email, 1.02d));
		map.put(555l, p(555l, "Unimed CG", "Unimed", email, 0.95d));
		map.put(556l, p(556l, "Pósitron", "Positron", email, 0.85d));
		map.put(564l, p(564l, "CMB - Autor. Cert.", "Buzz", email, 1.01d));
		map.put(563l, p(563l, "Integral: Busca Multimídia", "Walking", email, 0.99d));
		map.put(553l, p(553l, "Move+ Suporte", "Minions", email, 1.20d));
		map.put(566l, p(566l, "CMB - Insumos", "Minions", email, 0.97d));
		map.put(569l, p(569l, "SMARAPD", "Heisenberg", email, 0.98d));
		map.put(579l, p(579l, "Movile", "Rocket", email, 1.70d));
		map.put(583l, p(583l, "Wareline: Editor", "Mustache", email, 1.18d));
		map.put(585l, p(585l, "ADV: Fase II", "Mustache", email, 1.39d));
		map.put(24l, p(24l, "TI Dextra", "TI", email, 1.00d));
		
		return map;
	}
	
	private static Projeto p(long id, String nome, String equipe, String email, double cpi) {
		Projeto projeto = new Projeto();
		projeto.setIdPma(id);
		projeto.setNome(nome);
		projeto.setEquipe(equipe);
		projeto.setEmail(email);
		projeto.setCpi(cpi);
		return projeto;
	}
	
}
