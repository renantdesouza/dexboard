package br.com.dextra.dexboard.unit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.AbstractTestCase;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.json.IndicadorJson;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

import com.googlecode.restitory.gae.http.Response;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class IndicadorJsonTest extends AbstractTestCase {

	@Test
	public void testeTempo() throws Exception {

		carregaProjetos();

		for (int i = 1; i <= 6; i++) {
			alteraIndicadorDeProjeto(495, i, Classificacao.ATENCAO);
		}

		ProjetoDao dao = new ProjetoDao();
		List<Indicador> indicadores = dao.buscarIndicadoresDoProjeto(495l);
		Indicador indicador = indicadores.get(0);
		IndicadorJson indicadorJson = new IndicadorJson(indicador);
		Assert.assertEquals(Classificacao.ATENCAO, indicadorJson.getClassificacao());
		System.setProperty("validade", "-3");
		Assert.assertEquals(Classificacao.PERIGO, indicadorJson.getClassificacao());
	}

	private void alteraIndicadorDeProjeto(Integer idProjeto,
			Integer idIndicador, Classificacao classificacao)
			throws IOException, SAXException {

		ServletRunner sr = new ServletRunner();
		sr.registerServlet("indicadorServlet", IndicadorServlet.class.getName());

		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest(
				"http://localhost:8380/indicadorServlet");
		request.setParameter("projeto", idProjeto.toString());
		request.setParameter("indicador", idIndicador.toString());
		request.setParameter("registro", "{ 'classificacao' : '"
				+ classificacao + "', 'descricao': 'desc desc' }");

		sc.getResponse(request);
	}

	private void carregaProjetos() {
		Response resp = adapter.success("GET", "/reload/projetos", null, null);
		String status = getJson(resp).get("status").getAsString();
		assertEquals("success", status);
	}
		
	public void expirarClassificacaoProjetos() {		
		carregaProjetos();		
		ProjetoDao dao = new ProjetoDao();
		
		
	}	
}
