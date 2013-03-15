package br.com.dextra.dexboard.planilha;

import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.service.StringUtils;

public class PlanilhaIndicadores extends Planilha {

	public PlanilhaIndicadores(String chavePlanilha) {
		super(chavePlanilha, "Principal");
	}

	private String buscarNomeDoIndicador(int linha) {
		return recuperarConteudoCelula(linha, 1);
	}
	
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<Indicador>();

		int i = 1;
		while (true) {
			String nomeIndicador = buscarNomeDoIndicador(i);
			
			if (!StringUtils.isNullOrEmpty(nomeIndicador)) {
				indicadores.add(new Indicador(i, nomeIndicador));
				i++;
			} else {
				return indicadores;
			}
		}

	}
}
