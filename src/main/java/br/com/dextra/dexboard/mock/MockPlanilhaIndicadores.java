package br.com.dextra.dexboard.mock;

import java.util.Arrays;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;

public class MockPlanilhaIndicadores implements PlanilhaIndicadores {

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		return Arrays.asList(new Indicador[] {
				new Indicador(1l, "Satisfa\u00E7\u00E3o do cliente"),
				new Indicador(2l, "Satisfa\u00E7\u00E3o da equipe"),
				new Indicador(3l, "Foco em entrega do valor"),
				new Indicador(4l, "Qualidade funcional"),
				new Indicador(5l, "Qualidade do c\u00F3digo"),
				new Indicador(6l, "CPI")
		});
	}

}
