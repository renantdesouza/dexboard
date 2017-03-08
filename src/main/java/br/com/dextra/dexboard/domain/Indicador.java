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
	private String descricao;
	private int posicao;

	public Indicador() {
	}

	public Indicador(Long id, String nome, String descricao, int posicao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.posicao = posicao;
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

	public String getDescricao() {
		return descricao;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public void setComposeId(String composeId) {
		this.composeId = composeId;
	}

	public Key<Projeto> getProjeto() {
		return projeto;
	}

	public void setProjeto(Key<Projeto> projeto) {
		this.projeto = projeto;
	}

	public void defineComposeId() {
		this.composeId = String.format("%s;%s", getProjeto().getId(), getId().toString());
	}
}
