package br.com.dextra.dexboard.json;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import flexjson.JSON;

public class IndicadorJson {

	private Indicador indicador;
	private List<RegistroAlteracao> registros;

	public IndicadorJson() {
	}

	public IndicadorJson(Indicador indicador) {
		this.indicador = indicador;
		registros = new ProjetoDao().buscarRegistrosDeAlteracoes(indicador);

		Collections.sort(registros, new Comparator<RegistroAlteracao>() {
			@Override
			public int compare(RegistroAlteracao r1, RegistroAlteracao r2) {
				return r2.getData().compareTo(r1.getData());
			}
		});
	}

	public Long getId() {
		return indicador.getId();
	}

	public String getNome() {
		return indicador.getNome();
	}

	public boolean getAtrasado() {
		if (registros.isEmpty()) {
			return true;
		}

		if (!isValido(getUltimaAlteracao().getData())) {
			return true;
		}

		return false;
	}

	public Classificacao getClassificacao() {
		if (registros.isEmpty()) {
			return Classificacao.PERIGO;
		}
		return getUltimaAlteracao().getClassificacao();
	}

	@JSON
	public List<RegistroAlteracao> getRegistros() {
		return registros;
	}

	private boolean isValido(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, NotificacaoDao.getValidadeAlteracao());

		boolean validade = calendar.getTime().compareTo(new Date()) > -1;
		return validade;
	}

	private RegistroAlteracao getUltimaAlteracao() {
		return registros.get(0);
	}

}
