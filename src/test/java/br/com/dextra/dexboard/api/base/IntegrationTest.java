package br.com.dextra.dexboard.api.base;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.xml.sax.SAXException;

import com.google.gson.JsonObject;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

public abstract class IntegrationTest {

	private static final String APP_TEST = "app.test";

	protected GaeHelper helper;

	protected HttpFacade service = new HttpFacade();

	@Before
	public void setUp() throws Exception {
		System.setProperty(APP_TEST, "true");
		helper = new GaeHelper();
		helper.prepareLocalServiceTestHelper();
	}

	protected void alteraIndicadorDeProjeto(long idProjeto, int idIndicador, Classificacao classificacao) throws IOException, SAXException {
	
		ServletRunner sr = new ServletRunner();
		sr.registerServlet("indicadorServlet", IndicadorServlet.class.getName());
	
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://localhost:8380/indicadorServlet");
		request.setParameter("projeto", idProjeto + "");
		request.setParameter("indicador", idIndicador + "");
		request.setParameter("registro", "{ 'classificacao' : '" + classificacao + "', 'descricao': 'desc desc' }");
	
		sc.getResponse(request);
	}

	protected void carregaProjetos() {
		JsonObject response = this.service.get("/reload/projetos").getAsJsonObject();
		String status = response.get("status").getAsString();
		
		assertEquals( "success", status);
	}
}
