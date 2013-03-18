package br.com.dextra.dexboard.integration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.dextra.dexboard.base.AbstractTestCase;
import br.com.dextra.dexboard.servlet.IndicadorServlet;

import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.repackaged.org.apache.http.client.methods.HttpPost;
import com.googlecode.restitory.gae.http.Response;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class AtualizaValorIndicadorTest extends AbstractTestCase {

	@Test
	public void testeInserindoDadosNosIndicadores()
			throws MalformedURLException, IOException, SAXException {
		/*
		 * ListaProjeto lista = new ListaProjeto(new ArrayList<Projeto>());
		 * lista.getValue().add(new Projeto());
		 * lista.getValue().get(0).setNome("bla");
		 * lista.getValue().get(0).addIndicador(new Indicador(1, "teste"));
		 * 
		 * 
		 * Gson gson = new Gson(); String json = gson.toJson(lista);
		 * 
		 * ListaProjeto fromJson = gson.fromJson(json, ListaProjeto.class);
		 * 
		 * JSONSerializer serializer = new JSONSerializer();
		 * serializer.exclude("class"); String serialize =
		 * serializer.serialize(lista); System.out.println(serialize);
		 * 
		 * JSONDeserializer<ListaProjeto> des = new
		 * JSONDeserializer<ListaProjeto>(); ListaProjeto deserialize =
		 * des.deserialize(serialize);
		 */

		@SuppressWarnings("unused")
		Response resp = adapter.success("GET", "/reload/projetos", null, null);

		ServletRunner sr = new ServletRunner();
		sr.registerServlet("indicadorServlet", IndicadorServlet.class.getName());

		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest(
				"http://localhost:8380/indicadorServlet");
		request.setParameter("projeto", "1");
		request.setParameter("indicador",
				"{ 'id' : '1', 'nome' : 'nome', 'cor' : '2', 'descricao': 'desc desc' }");

		WebResponse response = sc.getResponse(request);

	}
}
