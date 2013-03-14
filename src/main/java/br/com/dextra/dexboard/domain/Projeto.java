package br.com.dextra.dexboard.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Projeto {

	private int idPma;
	private String nome;
	private List<Indicador> indicadores;
	private Date ultimaAlteracao;
	private Double cpi;

	public int getIdPma() {
		return idPma;
	}

	public void setIdPma(int idPma) {
		this.idPma = idPma;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Indicador> getIndicadores() {
		if (indicadores == null) {
			indicadores = new ArrayList<Indicador>();
		}
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public void addIndicador(Indicador indicador) {
		this.getIndicadores().add(indicador);
	}

	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}

	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public Double getCpi() {
		return cpi;
	}

	public void setCpi(Double cpi) {
		this.cpi = cpi;
	}
}
