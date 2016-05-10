package br.com.dextra.dexboard.repository;

import java.util.Comparator;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.json.ProjetoJson;

public class ProjetoComparator implements Comparator<Projeto> {

	@Override
	public int compare(Projeto projeto1, Projeto projeto2) {
		
		ProjetoJson p1 = projeto1.toProjetoJson();
		ProjetoJson p2 = projeto2.toProjetoJson();
		
		Classificacao c1 = p1.getClassificacao();
		Classificacao c2 = p2.getClassificacao();
		
		if(!p1.getAtrasado() && p2.getAtrasado()) {
			return 1;
		}

		if(p1.getAtrasado() && !p2.getAtrasado()) {
			return -1;
		}
				
		if (c1 == c2) {
			return p1.getNome().compareTo(p2.getNome());
		} else {
			return c1.ordinal() - c2.ordinal();
		}
	}

}
