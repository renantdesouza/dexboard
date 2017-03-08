package br.com.dextra.dexboard.mock;

import java.util.Arrays;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;

public class MockPlanilhaIndicadores implements PlanilhaIndicadores {

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		return Arrays.asList(new Indicador[] {
				new Indicador(1l, "Satisfa\u00E7\u00E3o do cliente", "",2),
				new Indicador(2l, "Satisfa\u00E7\u00E3o da equipe", "", 3),
				new Indicador(3l, "UX", "", 5),
				new Indicador(5l, "Qualidade t\u00E9cnica", "", 4),
				new Indicador(6l, "CPI", "", 1)
		});
	}

}
