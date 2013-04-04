package br.com.dextra.dexboard.domain;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class Projeto {

	@Id
	private Long idPma;
	private String nome;
	private Double cpi;

	public Long getIdPma() {
		return idPma;
	}

	public void setIdPma(Long idPma) {
		this.idPma = idPma;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public Double getCpi() {
		return cpi;
	}

	public void setCpi(Double cpi) {
		this.cpi = cpi;
	}

}
