package br.com.dextra.dexboard.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Indicador {

	@Id
	private String composeId;
	private Long id;
	@Index
	private Key<Projeto> projeto;
	private String nome;

	public Indicador() {
		super();
	}

	public Indicador(Long id, String nomeIndicador) {
		this.id = id;
		this.nome = nomeIndicador;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Key<Projeto> getProjeto() {
		return projeto;
	}

	public void setProjeto(Key<Projeto> projeto) {
		this.projeto = projeto;
	}

	public void defineComposeId() {
		String value = String.format("%s;%s", this.getProjeto().getId(), this.getId().toString());
		this.composeId = value;
	}
}
