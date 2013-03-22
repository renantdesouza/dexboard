package br.com.dextra.dexboard.unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.repository.ProjetoComparator;
import edu.emory.mathcs.backport.java.util.Collections;

public class OrdenacaoProjetoTest {

	@Test
	public void test() {
		List<Projeto> projetos = getListaProjeto("1", Classificacao.PERIGO, "2", Classificacao.PERIGO);
		String nome = projetos.get(0).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

		projetos = getListaProjeto("1", Classificacao.PERIGO, "2", Classificacao.ATENCAO);
		nome = projetos.get(0).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

		projetos = getListaProjeto("1", Classificacao.PERIGO, "2", Classificacao.OK);
		nome = projetos.get(0).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

		projetos = getListaProjeto("1", Classificacao.ATENCAO, "2", Classificacao.OK);
		nome = projetos.get(0).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

		projetos = getListaProjeto("1", Classificacao.OK, "2", Classificacao.ATENCAO);
		nome = projetos.get(1).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

		projetos = getListaProjeto("1", Classificacao.OK, "2", Classificacao.PERIGO);
		nome = projetos.get(1).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

		projetos = getListaProjeto("1", Classificacao.ATENCAO, "2", Classificacao.PERIGO);
		nome = projetos.get(1).getNome();
		Collections.sort(projetos, new ProjetoComparator());
		Assert.assertEquals(nome, projetos.get(0).getNome());

	}
	
	private List<Projeto> getListaProjeto(String nomeProjeto1, Classificacao classificacaoProjeto1, String nomeProjeto2, Classificacao classificacaoProjeto2) {
		Indicador p1i1 = new Indicador(1, "bla1");
		p1i1.setClassificacao(classificacaoProjeto1);
		p1i1.setUltimaAlteracao(new Date());
		Indicador p1i2 = new Indicador(2, "bla2");
		p1i2.setClassificacao(classificacaoProjeto1);
		p1i2.setUltimaAlteracao(new Date());
		Projeto p1 = new Projeto();
		p1.addIndicador(p1i1);
		p1.addIndicador(p1i2);
		p1.setNome(nomeProjeto1);
		
		Indicador p2i1 = new Indicador(1, "bla1");
		p2i1.setClassificacao(classificacaoProjeto2);
		p2i1.setUltimaAlteracao(new Date());
		Indicador p2i2 = new Indicador(2, "bla2");
		p2i2.setClassificacao(classificacaoProjeto2);
		p2i2.setUltimaAlteracao(new Date());
		Projeto p2 = new Projeto();
		p2.addIndicador(p2i1);
		p2.addIndicador(p2i2);
		p2.setNome(nomeProjeto2);
		
		List<Projeto> projetos = new ArrayList<Projeto>();
		projetos.add(p1);
		projetos.add(p2);
		return projetos;
		
	}

}
