package br.com.dextra.dexboard.domain;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.ibm.icu.text.SimpleDateFormat;

import flexjson.JSON;

@Entity
public class RegistroAlteracao {

	@Id
	private String id;
	private Key<Projeto> projeto;
	private Key<Indicador> indicador;
	private String usuario;
	private Classificacao classificacao;
	private Date data;
	private String comentario;

	public void defineId() {
		this.id = String.format("%s;%s;%s", this.getProjeto().getId(), this.getIndicador().getId(), this
				.getData().getTime() + "");
	}

	public Key<Projeto> getProjeto() {
		return projeto;
	}

	public void setProjeto(Key<Projeto> projeto) {
		this.projeto = projeto;
	}

	public Key<Indicador> getIndicador() {
		return indicador;
	}

	public void setIndicador(Key<Indicador> indicador) {
		this.indicador = indicador;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Classificacao classificacao) {
		this.classificacao = classificacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@JSON
	public String getUltimaAlteracaoString() {
		if (getData() == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(getData());
	}


}
