package br.com.dextra.dexboard.domain;

import java.util.List;

public class ListaProjeto {

	private List<Projeto> value;
	
	public ListaProjeto(List<Projeto> value) {
		this.value = value;
	}

	public List<Projeto> getValue() {
		return value;
	}
}
