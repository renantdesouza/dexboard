package br.com.dextra.dexboard.base;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

import com.google.gson.JsonObject;
import com.googlecode.restitory.gae.http.HttpClientRequestService;
import com.googlecode.restitory.gae.http.RequestAdapter;
import com.googlecode.restitory.gae.http.RequestService;
import com.googlecode.restitory.gae.http.Response;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class DexboardTestCase {

	private static final String APP_TEST = "app.test";

	protected GAETestHelper helper;

	protected RequestService service;

	protected RequestAdapter adapter;

	@Before
	public void setUp() throws Exception {
		System.setProperty(APP_TEST, "true");
		helper = new GAETestHelper();
		helper.prepareLocalServiceTestHelper();
		helper.bootMycontainer();

		service = new HttpClientRequestService("http://localhost:8380");
		adapter = new RequestAdapter(service);
	}

	@After
	public void tearDown() {
		if (helper != null) {
			helper.shutdownMycontainer();
		}
	}

	protected JsonObject getJson(Response resp) {
		return resp.getContent().getJson().getAsJsonObject();
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
		Response response = adapter.success("GET", "/reload/projetos", null, null);
		String status = getJson(response).get("status").getAsString();
		assertEquals("success", status);
	}
}
