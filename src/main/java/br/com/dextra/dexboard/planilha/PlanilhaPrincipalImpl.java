package br.com.dextra.dexboard.planilha;

import java.util.Map;

import br.com.dextra.dexboard.domain.Projeto;

class PlanilhaPrincipalImpl extends PlanilhaDexboard implements PlanilhaPrincipal {

	private static final int COLUNA_QUANTIDADE_PROJETOS = 9;

	private static final String COLUNA_ID_PROJETO = "id no PMA";
	private static final String COLUNA_NOME_PROJETO = "NickName";
	private static final String COLUNA_EQUIPE_PROJETO = "Equipe";
	private static final String COLUNA_EMAIL_PROJETO = "Email";
	private static final String COLUNA_CPI_PROJETO = "CPI";

	public PlanilhaPrincipalImpl() {
		super("Principal");
	}

	private int buscarQuantidadeDeProjetos() {
		return recuperarConteudoCelulaInt(2, COLUNA_QUANTIDADE_PROJETOS);
	}

	private String buscarNomeDoProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(indiceProjeto, COLUNA_NOME_PROJETO);
	}

	private String buscarEquipeProjeto(int indiceProjeto) {
		String equipe = recuperarConteudoCelula(indiceProjeto, COLUNA_EQUIPE_PROJETO);
		if (equipe != null) {
			return equipe.toUpperCase();
		}
		return null;
	}

	private String buscarEmailProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(indiceProjeto, COLUNA_EMAIL_PROJETO);
	}

	private Long buscarIdProjeto(int indiceProjeto) {
		return recuperarConteudoCelulaInt(indiceProjeto, COLUNA_ID_PROJETO).longValue();
	}

	private Double buscarCpiProjetoX(int indiceProjeto) {
		return recuperarConteudoCelulaDouble(indiceProjeto, COLUNA_CPI_PROJETO);
	}

	@Override
	public Map<Long, Projeto> buscarDadosDosProjetos() {
		Map<Long, Projeto> projetos = new java.util.HashMap<Long, Projeto>();

		int quantidadeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < quantidadeProjetos; ++i) {
			Projeto projeto = new Projeto();
			projeto.setIdPma(buscarIdProjeto(i));
			projeto.setNome(buscarNomeDoProjeto(i));
			projeto.setEquipe(buscarEquipeProjeto(i));
			projeto.setEmail(buscarEmailProjeto(i));
			projeto.setCpi(buscarCpiProjetoX(i));
			projetos.put(projeto.getIdPma(), projeto);
		}

		return projetos;
	}
}
