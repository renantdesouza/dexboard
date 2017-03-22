package br.com.dextra.dexboard.json;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import flexjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjetoJson {

	private Projeto projeto;
	private List<IndicadorJson> indicadores = new ArrayList<IndicadorJson>();
	private Classificacao classificacao;
	private boolean atrasado;

	public ProjetoJson() {
	}

	public ProjetoJson(Projeto projeto) {
		this.projeto = projeto;

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
				return i1.getPosicao() - i2.getPosicao();
			}
		});
	}

	public boolean getAtrasado() {
		return atrasado;
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
		return projeto.getEquipe();
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public String getApresentacao() {
		return projeto.getApresentacao();
	}

	public String getSatisfacaoCliente() {
		return emptyVal(projeto.getSatisfacaoCliente());
	}

	public String getSatisfacaoEquipe() {
		return emptyVal(projeto.getSatisfacaoEquipe());
	}

	public String getUx() {
		return emptyVal(projeto.getUx());
	}

	public String getQualidadeTecnica() {
		return emptyVal(projeto.getQualidadeTecnica());
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

	private String emptyVal(Double d) {
		return d == null ? "" : d.toString();
	}

}
