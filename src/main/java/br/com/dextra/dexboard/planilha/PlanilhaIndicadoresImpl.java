package br.com.dextra.dexboard.planilha;

import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.utils.StringUtils;

class PlanilhaIndicadoresImpl extends PlanilhaDexboard implements PlanilhaIndicadores {

	public PlanilhaIndicadoresImpl() {
		super("Indicadores");
	}

	private String buscarNomeDoIndicador(int linha) {
		return recuperarConteudoCelula(linha, 1);
	}

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<Indicador>();

		Long i = 1l;
		while (true) {
			String nomeIndicador = buscarNomeDoIndicador(i.intValue());

			if (!StringUtils.isNullOrEmpty(nomeIndicador)) {
				indicadores.add(new Indicador(i, nomeIndicador));
				i++;
			} else {
				return indicadores;
			}
		}

	}
}
