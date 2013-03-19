package br.com.dextra.dexboard.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.AbstractTestCase;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.repository.ProjetoRepository;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

import com.googlecode.restitory.gae.http.Response;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class CronTest extends AbstractTestCase {

	@Test
	public void testReloadProjetos() throws IOException, SAXException {

		carregaProjetos();
		alteraIndicadorDeProjeto(495, 1, Classificacao.OK);
		alteraIndicadorDeProjeto(495, 2, Classificacao.ATENCAO);
		verificaSeProjetoEstaComIndicadorPreenchido(495, 1, Classificacao.OK);
		verificaSeProjetoEstaComIndicadorPreenchido(495, 2, Classificacao.ATENCAO);
	}

	private void verificaSeProjetoEstaComIndicadorPreenchido(Integer idProjeto, Integer idIndicadorAlterado, Classificacao classificacao) {

		Projeto projeto = ProjetoRepository.buscarPorId(idProjeto, ProjetoRepository.buscaProjetos());
		List<Indicador> indicadores = projeto.getIndicadores();
		
		Indicador indicadorAlterado = encontraIndicadorDeId(indicadores, idIndicadorAlterado);

		Assert.assertNotNull(indicadorAlterado);
		Assert.assertEquals(classificacao, indicadorAlterado.getClassificacao());
		Assert.assertNotNull(indicadorAlterado.getUltimaAlteracao());
		Assert.assertEquals("test@example.com", indicadorAlterado.getUsuarioUltimaAlteracao());
		
	}

	private Indicador encontraIndicadorDeId(List<Indicador> indicadores, int idParaEncontrar) {
		for (Indicador ind : indicadores) {
			if (ind.getId().intValue() == idParaEncontrar) {
				return ind;
			}
		}
		return null;
	}

	private void alteraIndicadorDeProjeto(Integer idProjeto, Integer idIndicador, Classificacao classificacao) throws IOException, SAXException {

		ServletRunner sr = new ServletRunner();
		sr.registerServlet("indicadorServlet", IndicadorServlet.class.getName());
		
		ServletUnitClient sc = sr.newClient();
	    WebRequest request   = new PostMethodWebRequest("http://localhost:8380/indicadorServlet");
	    request.setParameter("projeto", idProjeto.toString());
	    request.setParameter("indicador", "{ 'id' : '" + idIndicador + "', 'nome' : 'NomeBla', 'classificacao' : '" + classificacao + "', 'descricao': 'desc desc' }");

	    sc.getResponse(request);
	}

	private void carregaProjetos() {

		Response resp = adapter.success("GET", "/reload/projetos", null, null);		
		String status = getJson(resp).get("status").getAsString();
		assertEquals("success", status);

	}

}
