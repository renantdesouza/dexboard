package br.com.dextra.dexboard.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;
import flexjson.JSON;

public class Projeto {

	private int idPma;
	private String nome;
	private List<Indicador> indicadores;
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

	@JSON
	public List<Indicador> getIndicadores() {
		if (indicadores == null) {
			indicadores = new ArrayList<Indicador>();
		}
		Collections.sort(indicadores, new Comparator<Indicador>() {

			@Override
			public int compare(Indicador o1, Indicador o2) {
				return o1.getNome().compareToIgnoreCase(o2.getNome());
			}
		});
		return indicadores;
	}

	public void setIndicadores(List<Indicador> indicadores) {
		this.indicadores = indicadores;
	}

	public void addIndicador(Indicador indicador) {
		this.getIndicadores().add(indicador);
	}
	

	public void alteraIndicador(Indicador indicadorAlterado, String usuario) {
		int i = 0;

		while (i < this.getIndicadores().size()) {
			Indicador indicador = this.getIndicadores().get(i);
			if (indicador.getId() == indicadorAlterado.getId()) {
				this.getIndicadores().remove(i);
				indicadorAlterado.setUsuarioUltimaAlteracao(usuario);
				indicadorAlterado.setUltimaAlteracao(new Date());
				this.addIndicador(indicadorAlterado);
				break;
			}
			i++;
		}
	}

	public Double getCpi() {
		return cpi;
	}

	public void setCpi(Double cpi) {
		this.cpi = cpi;
	}

	public Classificacao getClassificacao() {
		Classificacao retorno = Classificacao.OK;

		for (Indicador indicador : this.getIndicadores()) {
			
			Classificacao classificacao = indicador.getClassificacao();
			
			if (classificacao.equals(Classificacao.PERIGO)) {
				return Classificacao.PERIGO;
			}
			
			if (classificacao.equals(Classificacao.ATENCAO)) {
				retorno = Classificacao.ATENCAO;
			}
		}

		return retorno;
	}
}
