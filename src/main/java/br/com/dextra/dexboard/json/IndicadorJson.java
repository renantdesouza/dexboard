package br.com.dextra.dexboard.json;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import flexjson.JSON;

public class IndicadorJson {

	private Indicador indicador;
	private List<RegistroAlteracao> registros;

	public IndicadorJson(Indicador indicador) {
		this.indicador = indicador;

		ProjetoDao projetoDao = new ProjetoDao();
		this.registros = projetoDao.buscarRegistrosDeAlteracoes(indicador);

		Collections.sort(this.registros, new Comparator<RegistroAlteracao>() {

			@Override
			public int compare(RegistroAlteracao r1, RegistroAlteracao r2) {
				return r2.getData().compareTo(r1.getData());
			}
		});
	}

	public Long getId() {
		return this.indicador.getId();
	}

	public String getNome() {
		return this.indicador.getNome();
	}

	public Classificacao getClassificacao() {
		if (this.registros.isEmpty()) {
			return Classificacao.PERIGO;
		}
		RegistroAlteracao ultimaAlteracao = this.registros.get(0);

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.roll(Calendar.DAY_OF_MONTH, -20);
		if (calendar.after(ultimaAlteracao.getData())) {
			return Classificacao.PERIGO;
		}

		Classificacao classificacao = ultimaAlteracao.getClassificacao();
		return classificacao;
	}

	@JSON
	public List<RegistroAlteracao> getRegistros() {
		return registros;
	}
}
