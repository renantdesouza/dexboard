package br.com.dextra.dexboard.old;

import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.Planilha;

public class PlanilhaDexboard extends Planilha {
	//
	public PlanilhaDexboard(String chavePlanilha) {
		super(chavePlanilha, "Principal");
	}

	// -----------------------------------------------------------

	private int buscarQuantidadeDeProjetos() {
		return recuperarConteudoCelulaInt(2, 6);
	}

	private String buscarNomeDoProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(2 + indiceProjeto, 1);
	}

	private int buscarIdProjeto(int indiceProjeto) {
		return recuperarConteudoCelulaInt(2 + indiceProjeto, 2);
	}

	// -----------------------------------------------------------

	public List<Projeto> buscarDadosDosProjetos() {
		List<Projeto> ret = new ArrayList<Projeto>();

		int qtdeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < qtdeProjetos; ++i) {
			Projeto proj = new Projeto();
			proj.setIdPma(buscarIdProjeto(i));
			proj.setNome(buscarNomeDoProjeto(i));
			ret.add(proj);
		}

		return ret;
	}

}
