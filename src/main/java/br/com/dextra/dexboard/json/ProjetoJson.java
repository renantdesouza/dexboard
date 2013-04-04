package br.com.dextra.dexboard.json;

import java.util.List;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import flexjson.JSON;

public class ProjetoJson {

	private Projeto projeto;
	private List<Indicador> indicadores;
	private Classificacao classificacao;

	public ProjetoJson(Projeto projeto) {
		this.projeto = projeto;

		ProjetoDao dao = new ProjetoDao();
		this.indicadores = dao.buscarIndicadoresDoProjeto(getIdPma());

		this.classificacao = defineClassificacao();
	}

	@JSON
	public List<Indicador> getIndicadores() {
		return indicadores;
	}

	public int getIdPma() {
		return this.projeto.getIdPma();
	}

	public String getNome() {
		return this.projeto.getNome();
	}

	public Double getCpi() {
		return this.projeto.getCpi();
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	private Classificacao defineClassificacao() {
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
