package br.com.dextra.dexboard.planilha;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.dextra.dexboard.domain.Projeto;

class PlanilhaPrincipalImpl extends PlanilhaDexboard implements PlanilhaPrincipal {

	private static final int COLUNA_QUANTIDADE_PROJETOS = 14;

	private static final String COLUNA_ID_PROJETO = "id no PMA";
	private static final String COLUNA_NOME_PROJETO = "NickName";
	private static final String COLUNA_EQUIPE_PROJETO = "Equipe";
	private static final String COLUNA_EMAIL_PROJETO = "Email";
	private static final String COLUNA_CPI_PROJETO = "CPI";
	private static final String COLUNA_APRESENTACAO = "Apresentacao";
	private static final String COLUNA_SATISFACAO_CLIENTE = "Satisfacao Cliente";
	private static final String COLUNA_SATISFACAO_EQUIPE = "Satisfacao Equipe";
	private static final String COLUNA_QUALIDADE_TECNICA = "Qualidade Tecnica";
	private static final String COLUNA_UX = "ux";

	private static final Pattern LINK_APRESENTACAO = Pattern.compile("https://(.+)embed");

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

	private String buscarLinkApresentacao(int indiceProjeto) {
		String rawLink = recuperarConteudoCelula(indiceProjeto, COLUNA_APRESENTACAO);
		if (rawLink == null) return null;

		Matcher matcher = LINK_APRESENTACAO.matcher(rawLink);
		if (!matcher.find()) return null;

		return matcher.group();
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

	private Double buscarSatisfacaoCliente(int indice) {
		return recuperarConteudoCelulaDouble(indice, COLUNA_SATISFACAO_CLIENTE);
	}

	private Double buscarSatisfacaoEquipe(int indice) {
		return recuperarConteudoCelulaDouble(indice, COLUNA_SATISFACAO_EQUIPE);
	}

	private Double buscarQualidadeTecnica(int indice) {
		return recuperarConteudoCelulaDouble(indice, COLUNA_QUALIDADE_TECNICA);
	}

	private Double buscarUx(int indice) {
		return recuperarConteudoCelulaDouble(indice, COLUNA_UX);
	}

	@Override
	public Map<Long, Projeto> buscarDadosDosProjetos() {
		Map<Long, Projeto> projetos = new java.util.HashMap<>();

		int quantidadeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < quantidadeProjetos; ++i) {
			Projeto projeto = new Projeto();
			projeto.setIdPma(buscarIdProjeto(i));
			projeto.setNome(buscarNomeDoProjeto(i));
			projeto.setEquipe(buscarEquipeProjeto(i));
			projeto.setEmail(buscarEmailProjeto(i));
			projeto.setCpi(buscarCpiProjetoX(i));
			projeto.setApresentacao(buscarLinkApresentacao(i));
			projeto.setSatisfacaoCliente(buscarSatisfacaoCliente(i));
			projeto.setSatisfacaoEquipe(buscarSatisfacaoEquipe(i));
			projeto.setQualidadeTecnica(buscarQualidadeTecnica(i));
			projeto.setUx(buscarUx(i));

			projetos.put(projeto.getIdPma(), projeto);
		}

		return projetos;
	}

}
