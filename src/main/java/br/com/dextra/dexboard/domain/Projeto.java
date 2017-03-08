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

	@Index
	private Double satisfacaoCliente;

	@Index
	private Double satisfacaoEquipe;

	public Projeto() {
	}

	public Projeto(long id, String nome, String equipe, String email, double cpi,
				   double satisfacaoCliente, double satisfacaoEquipe) {
		setIdPma(id);
		setNome(nome);
		setEquipe(equipe);
		setEmail(email);
		setCpi(cpi);
		setSatisfacaoCliente(satisfacaoCliente);
		setSatisfacaoEquipe(satisfacaoEquipe);
	}

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

	public Double getSatisfacaoCliente() {
		return satisfacaoCliente;
	}

	public void setSatisfacaoCliente(Double satisfacaoCliente) {
		this.satisfacaoCliente = satisfacaoCliente;
	}

	public Double getSatisfacaoEquipe() {
		return satisfacaoEquipe;
	}

	public void setSatisfacaoEquipe(Double satisfacaoEquipe) {
		this.satisfacaoEquipe = satisfacaoEquipe;
	}

	@Ignore // Lazy
	private ProjetoJson projetoJson;

	public ProjetoJson toProjetoJson() {
		return projetoJson = (projetoJson == null) ? new ProjetoJson(this) : projetoJson;
	}

	public static List<ProjetoJson> toProjetoJson(List<Projeto> projetos) {
		ArrayList<ProjetoJson> jsons = new ArrayList<>(projetos.size());
		for (Projeto p : projetos) {
			jsons.add(p.toProjetoJson());
		}

		return jsons ;
	}
}
