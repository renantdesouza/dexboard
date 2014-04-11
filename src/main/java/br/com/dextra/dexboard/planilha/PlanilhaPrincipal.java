package br.com.dextra.dexboard.planilha;

import java.util.Map;

import br.com.dextra.dexboard.domain.Projeto;

public class PlanilhaPrincipal extends PlanilhaDexboard {

	private static final int COLUNA_NOME_PROJETO = 3;
	private static final int COLUNA_EQUIPE_PROJETO = 4;
	private static final int COLUNA_EMAIL_PROJETO = 5;
	private static final int COLUNA_CPI_PROJETO = 6;
	private static final int COLUNA_QUANTIDADE_PROJETOS = 9;

	public PlanilhaPrincipal() {
		super("Principal");
	}

	private int buscarQuantidadeDeProjetos() {
		return recuperarConteudoCelulaInt(2, COLUNA_QUANTIDADE_PROJETOS);
	}

	private String buscarNomeDoProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(2 + indiceProjeto, COLUNA_NOME_PROJETO);
	}
	
	private String buscarEquipeProjeto(int indiceProjeto) {
		String equipe = recuperarConteudoCelula(2 + indiceProjeto, COLUNA_EQUIPE_PROJETO);
		if(equipe != null) {
			return equipe.toUpperCase();
		}
		return null;
	}
	
	private String buscarEmailProjeto(int indiceProjeto) {
		String equipe = recuperarConteudoCelula(2 + indiceProjeto, COLUNA_EMAIL_PROJETO);
		if(equipe != null) {
			return equipe.toUpperCase();
		}
		return null;
	}


	private Long buscarIdProjeto(int indiceProjeto) {
		return recuperarConteudoCelulaInt(2 + indiceProjeto, 2).longValue();
	}
	
	private Double buscarCpiProjeto(int indiceProjeto) {
		return recuperarConteudoCelulaDouble(2 + indiceProjeto, COLUNA_CPI_PROJETO);
	}

	public Map<Long, Projeto> buscarDadosDosProjetos() {
		Map<Long, Projeto> projetos = new java.util.HashMap<Long, Projeto>();

		int quantidadeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < quantidadeProjetos; ++i) {
			Projeto projeto = new Projeto();
			projeto.setIdPma(buscarIdProjeto(i));
			projeto.setNome(buscarNomeDoProjeto(i));
			projeto.setEquipe(buscarEquipeProjeto(i));
			projeto.setEmail(buscarEmailProjeto(i));
			projeto.setCpi(buscarCpiProjeto(i));
			projetos.put(projeto.getIdPma(), projeto);
		}

		return projetos;
	}
}
