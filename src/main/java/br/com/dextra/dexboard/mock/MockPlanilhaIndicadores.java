package br.com.dextra.dexboard.mock;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;

import java.util.Arrays;
import java.util.List;

public class MockPlanilhaIndicadores implements PlanilhaIndicadores {

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		return Arrays.asList(new Indicador[] {
				new Indicador(1l, "Satisfa\u00E7\u00E3o do cliente", "Determina o qu\u00E3o satisfeito est\u00E1 o cliente",2),
				new Indicador(2l, "Satisfa\u00E7\u00E3o da equipe", "Determina o qu\u00E3o satisfeito est\u00E1 a equipe",3),
				new Indicador(3l, "UX", "Determina a qualidade e o valor da experi\u00EAncia do usu\u00E1rio", 5),
				new Indicador(5l, "Qualidade t\u00E9cnica", "Determina a qualidade do c\u00F3digo gerado", 4),
				new Indicador(6l, "CPI", "Determina o desempenho de acordo com o custo", 1)
		});
	}

}
