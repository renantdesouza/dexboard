package br.com.dextra.dexboard.planilha;

import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.utils.StringUtils;

class PlanilhaIndicadoresImpl extends PlanilhaDexboard implements PlanilhaIndicadores {

	private static final String COLUNA_NOME = "nome do indicador";
	private static final String COLUNA_DESCRICAO = "descricao do indicador";
	private static final String COLUNA_POSICAO = "posicao do indicador";

	public PlanilhaIndicadoresImpl() {
		super("Indicadores");
	}

	private String buscarNomeDoIndicador(int linha) {
		return recuperarConteudoCelula(linha, COLUNA_NOME);
	}

	private String buscarDescricaoDoIndicador(int linha) {
		return recuperarConteudoCelula(linha, COLUNA_DESCRICAO);
	}

	private int buscarPosicaoDoIndicador(int linha) {
		return recuperarConteudoCelulaInt(linha, COLUNA_POSICAO);
	}

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<Indicador>();

		Long i = 1l;
		while (true) {
			int index = i.intValue();

			String nome = buscarNomeDoIndicador(index);
			String descricao = buscarDescricaoDoIndicador(index);
			int posicao = buscarPosicaoDoIndicador(index);

			if (!StringUtils.isNullOrEmpty(nome)) {
				indicadores.add(new Indicador(i, nome, descricao, posicao));
				i++;
			} else {
				return indicadores;
			}
		}
	}
}
