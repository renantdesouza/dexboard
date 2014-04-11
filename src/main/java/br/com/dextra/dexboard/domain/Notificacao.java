package br.com.dextra.dexboard.domain;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Notificacao {

	@Id
	private Long idPma;

	private Date date;

	public Long getIdPma() {
		return idPma;
	}

	public void setIdPma(Long idPma) {
		this.idPma = idPma;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
