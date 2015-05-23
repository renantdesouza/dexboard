package br.com.dextra.dexboard.json;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;

public class HistoricoJson {

	private static Map<Classificacao, String> ESTILO_CLASSIFICACAO = new HashMap<Classificacao, String>();

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	static {
		ESTILO_CLASSIFICACAO.put(Classificacao.ATENCAO, "warning");
		ESTILO_CLASSIFICACAO.put(Classificacao.OK, "success");
		ESTILO_CLASSIFICACAO.put(Classificacao.PERIGO, "danger");
	}

	private String nomeProjeto;
	private String autor;
	private String descricao;
	private String equipe;
	private Date dateAlteracao;
	private String classificacao;
	private String indicador;

	public HistoricoJson(RegistroAlteracao registro, Projeto projeto, Indicador indicador) {
		this.nomeProjeto = projeto.getNome();
		this.equipe = projeto.getEquipe();
		this.autor = registro.getUsuario();
		this.dateAlteracao = registro.getData();
		this.descricao = registro.getComentario();
		this.classificacao = ESTILO_CLASSIFICACAO.get(registro.getClassificacao());
		this.indicador = indicador.getNome();
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public String getAutor() {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "none";
		}
		m.update(autor.getBytes(), 0, autor.length());
		return new BigInteger(1, m.digest()).toString(16);
	}

	public String getDescricao() {
		return descricao;
	}

	public String getEquipe() {
		return equipe;
	}

	public String getDateAlteracao() {
		return sdf.format(dateAlteracao);
	}

	public String getClassificacao() {
		return classificacao;
	}

	public String getIndicador() {
		return indicador;
	}
}
