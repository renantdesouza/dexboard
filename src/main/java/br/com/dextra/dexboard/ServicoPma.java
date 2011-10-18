package br.com.dextra.dexboard;

import java.nio.charset.Charset;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicoPma {

	private static final Charset ENCODING = Charset.forName("latin1");

	@SuppressWarnings("unused")
	private Logger LOG = LoggerFactory.getLogger(ServicoPma.class);

	private final String url;
	private final String token;

	public ServicoPma(String url, String token) {
		this.url = url;
		this.token = token;
	}

	public JsonObject buscarDadosDoProjeto(int idProjeto) {
		return Utils.baixarJson(url + "?token=" + token + "&projeto_id=" + idProjeto, ENCODING).getAsJsonObject();
	}

}
