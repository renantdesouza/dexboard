package br.com.dextra.dexboard.repository;

import java.util.Comparator;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Projeto;

public class ProjetoComparator implements Comparator<Projeto> {

	@Override
	public int compare(Projeto p1, Projeto p2) {
		Classificacao c1 = p1.getClassificacao();
		Classificacao c2 = p2.getClassificacao();
		if (c1 == c2) {
			return p1.getNome().compareTo(p2.getNome());
		} else {
			return c1.ordinal() - c2.ordinal();
		}
	}

}
