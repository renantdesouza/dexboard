package br.com.dextra.dexboard.service;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dextra.dexboard.utils.JsonUtils;

import com.google.gson.JsonObject;

public class PMAService {

	private static final Charset ENCODING = Charset.forName("latin1");

	private Logger LOG = LoggerFactory.getLogger(PMAService.class);

	private final String url;
	private final String token;

	public PMAService(String url, String token) {
		this.url = url;
		this.token = token;
	}

	public JsonObject buscarDadosDoProjeto(int idProjeto) {
		LOG.info("Buscando informacoes PMA do projeto id: " + idProjeto);
		return JsonUtils.baixarJson(url + "?token=" + token + "&projeto_id=" + idProjeto, ENCODING).getAsJsonObject();
	}

}
