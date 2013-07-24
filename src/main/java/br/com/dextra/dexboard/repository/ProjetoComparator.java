package br.com.dextra.dexboard.repository;

import java.util.Comparator;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.json.ProjetoJson;

public class ProjetoComparator implements Comparator<ProjetoJson> {

	@Override
	public int compare(ProjetoJson p1, ProjetoJson p2) {
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
