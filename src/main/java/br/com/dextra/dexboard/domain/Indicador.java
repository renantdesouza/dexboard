package br.com.dextra.dexboard.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ibm.icu.text.SimpleDateFormat;

import flexjson.JSON;

public class Indicador {

	private Integer id;
	private String nome;
	private Classificacao classificacao = Classificacao.OK;
	private String descricao;
	private String usuarioUltimaAlteracao;
	private Date ultimaAlteracao;

	public Indicador() {
		super();
	}

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

	public Classificacao getClassificacao() {
		if (this.ultimaAlteracao == null) {
			return Classificacao.PERIGO;
		}

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.roll(Calendar.DAY_OF_MONTH, -20);
		if (calendar.after(ultimaAlteracao)) {
			return Classificacao.PERIGO;
		}

		return this.classificacao;
	}

	public void setClassificacao(Classificacao classificacao) {
		this.classificacao = classificacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUsuarioUltimaAlteracao() {
		return usuarioUltimaAlteracao;
	}

	public void setUsuarioUltimaAlteracao(String usuarioUltimaAlteracao) {
		this.usuarioUltimaAlteracao = usuarioUltimaAlteracao;
	}

	@JSON
	public String getUltimaAlteracaoString() {
		if (getUltimaAlteracao() == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(getUltimaAlteracao());
	}
}
