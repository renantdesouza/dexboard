package br.com.dextra.dexboard.planilha;

import java.util.Map;

import br.com.dextra.dexboard.domain.Projeto;

public class PlanilhaPrincipal extends PlanilhaDexboard {

	private static final int COLUNA_QUANTIDADE_PROJETOS = 9;

	private static final String COLUNA_ID_PROJETO_X = "id no PMA";
	private static final String COLUNA_NOME_PROJETO_X = "NickName";
	private static final String COLUNA_EQUIPE_PROJETO_X = "Equipe";
	private static final String COLUNA_EMAIL_PROJETO_X = "Email";
	private static final String COLUNA_CPI_PROJETO_X = "CPI";

	public PlanilhaPrincipal() {
		super("Principal");
	}

	private int buscarQuantidadeDeProjetos() {
		return recuperarConteudoCelulaInt(2, COLUNA_QUANTIDADE_PROJETOS);
	}

	private String buscarNomeDoProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(indiceProjeto, COLUNA_NOME_PROJETO_X);
	}

	private String buscarEquipeProjeto(int indiceProjeto) {
		String equipe = recuperarConteudoCelula(indiceProjeto, COLUNA_EQUIPE_PROJETO_X);
		if (equipe != null) {
			return equipe.toUpperCase();
		}
		return null;
	}

	private String buscarEmailProjeto(int indiceProjeto) {
		String equipe = recuperarConteudoCelula(indiceProjeto, COLUNA_EMAIL_PROJETO_X);
		if (equipe != null) {
			return equipe.toUpperCase();
		}
		return null;
	}

	private Long buscarIdProjetoX(int indiceProjeto) {
		return recuperarConteudoCelulaInt(indiceProjeto, COLUNA_ID_PROJETO_X).longValue();
	}

	private Double buscarCpiProjetoX(int indiceProjeto) {
		return recuperarConteudoCelulaDouble(indiceProjeto, COLUNA_CPI_PROJETO_X);
	}

	public Map<Long, Projeto> buscarDadosDosProjetos() {
		Map<Long, Projeto> projetos = new java.util.HashMap<Long, Projeto>();

		int quantidadeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < quantidadeProjetos; ++i) {
			Projeto projeto = new Projeto();
			projeto.setIdPma(buscarIdProjetoX(i));
			projeto.setNome(buscarNomeDoProjeto(i));
			projeto.setEquipe(buscarEquipeProjeto(i));
			projeto.setEmail(buscarEmailProjeto(i));
			projeto.setCpi(buscarCpiProjetoX(i));
			projetos.put(projeto.getIdPma(), projeto);
		}

		return projetos;
	}
}
