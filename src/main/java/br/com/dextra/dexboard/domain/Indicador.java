package br.com.dextra.dexboard.domain;

import java.util.Date;

public class Indicador {

	private Integer id;
	private String nome;
	private int cor;
	private String descricao;
	private String usuario;
	private Date ultimaAlteracao;
	
	public Indicador(int id, String nomeIndicador) {
		this.id = id;
		this.nome = nomeIndicador;
	}

	public Integer getId() {
		return id;
	}

	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}
	
	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCor() {
		return cor;
	}

	public void setCor(int cor) {
		this.cor = cor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
