package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.Indicador;

import java.util.ArrayList;
import java.util.List;

class PlanilhaIndicadoresImpl extends PlanilhaDexboard implements PlanilhaIndicadores {

	private static final int COLUNA_QUANTIDADE_DE_INDICADORES = 4;

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

	private int buscarQuantidadeDeColunas() {
		return recuperarConteudoCelulaInt(2, COLUNA_QUANTIDADE_DE_INDICADORES);
	}

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<>();

		int quantidadeIndicadores = buscarQuantidadeDeColunas();

		for (Long i = 0l; i < quantidadeIndicadores; ++i) {
			int index = i.intValue();

			String nome = buscarNomeDoIndicador(index);
			String descricao = buscarDescricaoDoIndicador(index);
			int posicao = buscarPosicaoDoIndicador(index);

			indicadores.add(new Indicador(i, nome, descricao, posicao));
		}

		return indicadores;
	}

}
