package br.com.dextra.dexboard.planilha;

import java.util.Map;

import br.com.dextra.dexboard.domain.Projeto;

public class PlanilhaPrincipal extends PlanilhaDexboard {

	public PlanilhaPrincipal() {
		super("Principal");
	}

	private int buscarQuantidadeDeProjetos() {
		return recuperarConteudoCelulaInt(2, 7);
	}

	private String buscarNomeDoProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(2 + indiceProjeto, 4);
	}

	private int buscarIdProjeto(int indiceProjeto) {
		return recuperarConteudoCelulaInt(2 + indiceProjeto, 2);
	}

	public Map<Integer, Projeto> buscarDadosDosProjetos() {
		Map<Integer, Projeto> ret = new java.util.HashMap<Integer, Projeto>();

		int qtdeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < qtdeProjetos; ++i) {
			Projeto proj = new Projeto();
			proj.setIdPma(buscarIdProjeto(i));
			proj.setNome(buscarNomeDoProjeto(i));
			ret.put(proj.getIdPma(), proj);
		}

		return ret;
	}

}
