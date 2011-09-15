package br.com.dextra.dexboard;

import com.google.gson.JsonObject;

public class Pma extends UrlDataFetchService {

	private static final String TOKEN_PMA = "ac4ef0ec195ed24ab08d1e4a8a3a1ed0";

	private static final String URL_SERVICO_PMA = "http://172.16.129.159:8001/services/indicadores";

	public static JsonObject buscarDadosProjeto(int idProjeto) {
		return baixarJson(URL_SERVICO_PMA + "?token=" + TOKEN_PMA + "&projeto_id=" + idProjeto).getAsJsonObject();
	}

}
