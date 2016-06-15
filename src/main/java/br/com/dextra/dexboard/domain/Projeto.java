package br.com.dextra.dexboard.domain;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import br.com.dextra.dexboard.json.ProjetoJson;

@Entity
public class Projeto {

	@Id
	private Long idPma;
	private String nome;
	private Double cpi;
	@Index
	private boolean ativo = true;
	@Index
	private String equipe;

	private String email;
	
	private String apresentacao;

	public Long getIdPma() {
		return idPma;
	}

	public void setIdPma(Long idPma) {
		this.idPma = idPma;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getCpi() {
		return cpi;
	}

	public void setCpi(Double cpi) {
		this.cpi = cpi;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe.toUpperCase();
	}

	public String getEquipe() {
		return equipe;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getApresentacao() {
		return apresentacao;
	}
	
	public void setApresentacao(String apresentacao) {
		this.apresentacao = apresentacao;
	}
	
	@Ignore // Lazy
	private ProjetoJson projetoJson;
	
	public ProjetoJson toProjetoJson() {
		if (this.projetoJson == null) {
			this.projetoJson = new ProjetoJson(this); 
		}
		return this.projetoJson;
	}
	
	public static List<ProjetoJson> toProjetoJson(List<Projeto> projetos) {
		ArrayList<ProjetoJson> projetosJson = new ArrayList<ProjetoJson>(projetos.size());
		for (Projeto p : projetos) {
			projetosJson.add(p.toProjetoJson());
		}
		
		return projetosJson;
	}
}
