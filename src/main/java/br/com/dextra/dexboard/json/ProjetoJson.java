package br.com.dextra.dexboard.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import flexjson.JSON;

public class ProjetoJson {

	private Projeto projeto;
	private List<IndicadorJson> indicadores = new ArrayList<IndicadorJson>();
	private Classificacao classificacao;
	private double satisfacaoCliente;
	private double satisfacaoEquipe;
	private boolean atrasado;

	public ProjetoJson() {
	}

	public ProjetoJson(Projeto projeto) {
		this.projeto = projeto;
		satisfacaoCliente = projeto.getSatisfacaoCliente();
		satisfacaoEquipe = projeto.getSatisfacaoEquipe();

		inicializaIndicadores();
		ordenaIndicadores();

		classificacao = defineClassificacao();
		atrasado = defineAtrasado();
	}

	private void inicializaIndicadores() {
		List<Indicador> indicadoresDataStore = new ProjetoDao().buscarIndicadoresDoProjeto(getIdPma());
		for (Indicador i : indicadoresDataStore) {
			indicadores.add(new IndicadorJson(i));
		}
	}

	private void ordenaIndicadores() {
		Collections.sort(indicadores, new Comparator<IndicadorJson>() {
			@Override
			public int compare(IndicadorJson i1, IndicadorJson i2) {
				return i1.getNome().compareToIgnoreCase(i2.getNome());
			}
		});
	}

	public boolean getAtrasado() {
		return atrasado;
	}


	public double getSatisfacaoCliente() {
		return satisfacaoCliente;
	}

	public double getSatisfacaoEquipe() {
		return satisfacaoEquipe;
	}

	@JSON
	public List<IndicadorJson> getIndicadores() {
		return indicadores;
	}

	public Long getIdPma() {
		return projeto.getIdPma();
	}

	public String getNome() {
		return projeto.getNome();
	}

	public Double getCpi() {
		return projeto.getCpi();
	}

	public String getEquipe() {
		return this.projeto.getEquipe();
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public String getApresentacao() {
		return projeto.getApresentacao();
	}

	private boolean defineAtrasado() {
		for (IndicadorJson indicador : getIndicadores()) {
			if (indicador.getAtrasado()) {
				return true;
			}
		}
		return false;
	}

	private Classificacao defineClassificacao() {
		Classificacao retorno = Classificacao.OK;

		for (IndicadorJson indicador : getIndicadores()) {
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
