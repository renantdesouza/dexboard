package br.com.dextra.dexboard.domain;

import java.util.Date;
import java.util.Map;

public class Indicador {

	private Integer id;
	private String nome;
	private int cor;
	private String descricao;
	private String usuarioUltimaAlteracao;
	private Date ultimaAlteracao;
	
	public Indicador(int id, String nomeIndicador) {
		this.id = id;
		this.nome = nomeIndicador;
	}

	public Indicador(Map<String, Object> indicadorJSon) {
		
		if (indicadorJSon.containsKey("id")) {
			String x = indicadorJSon.get("id").toString();
			System.out.println(x);
			this.id = Integer.valueOf(indicadorJSon.get("id").toString());
		}

		if (indicadorJSon.containsKey("nome")) {
			this.nome = (String) indicadorJSon.get("nome");
		}

		if (indicadorJSon.containsKey("cor")) {
			this.cor = (Integer) indicadorJSon.get("cor");
		}

		if (indicadorJSon.containsKey("descricao")) {
			this.descricao = (String) indicadorJSon.get("descricao");
		}

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

	public int getCor() {
		return cor;
	}

	public void setCor(int cor) {
		this.cor = cor;
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

}
