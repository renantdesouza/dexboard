package br.com.dextra.dexboard.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.AbstractTestCase;
import br.com.dextra.dexboard.repository.ProjetoRepository;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

import com.googlecode.restitory.gae.http.Response;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class CronTest extends AbstractTestCase {

	@Test
	public void testReloadProjetos() throws IOException, SAXException {

		carregaProjetos();
		
		alteraIndicadorDeProjeto(495);

		verificaSeProjetoEstaComIndicadorPreenchido(495);
		
	}

	private void verificaSeProjetoEstaComIndicadorPreenchido(Integer idProjeto) {

	//	ProjetoRepository.buscarPorId(495, projetos)
		
	}

	private void alteraIndicadorDeProjeto(Integer idProjeto) throws IOException, SAXException {

		ServletRunner sr = new ServletRunner();
		sr.registerServlet("indicadorServlet", IndicadorServlet.class.getName());
		
		ServletUnitClient sc = sr.newClient();
	    WebRequest request   = new PostMethodWebRequest("http://localhost:8380/indicadorServlet");
	    request.setParameter("projeto", idProjeto.toString());
	    request.setParameter("indicador", "{ 'id' : '1', 'nome' : 'nome', 'cor' : '2', 'descricao': 'desc desc' }");

	    sc.getResponse(request);
	}

	private void carregaProjetos() {

		Response resp = adapter.success("GET", "/reload/projetos", null, null);		
		String status = getJson(resp).get("status").getAsString();
		assertEquals("success", status);

	}

}
