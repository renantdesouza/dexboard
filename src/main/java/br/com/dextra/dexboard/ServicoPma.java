package br.com.dextra.dexboard;

import com.google.gson.JsonObject;

public class ServicoPma {

	private final String url;
	private final String token;

	public ServicoPma(String url, String token) {
		this.url = url;
		this.token = token;
	}

	public JsonObject buscarDadosDoProjeto(int idProjeto) {
		return Utils.baixarJson(url + "?token=" + token + "&projeto_id=" + idProjeto).getAsJsonObject();
	}

}
