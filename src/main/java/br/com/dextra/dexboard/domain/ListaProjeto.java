package br.com.dextra.dexboard.domain;

import java.util.List;

import flexjson.JSON;

@JSON
public class ListaProjeto {

	private List<Projeto> value;
	
	public ListaProjeto(List<Projeto> value) {
		this.value = value;
	}

	@JSON
	public List<Projeto> getValue() {
		return value;
	}
}