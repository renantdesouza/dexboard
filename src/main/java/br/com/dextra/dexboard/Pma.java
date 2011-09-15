package br.com.dextra.dexboard;

import com.google.gson.JsonObject;

public class Pma extends UrlDataFetchService {

	public static JsonObject buscarDadosProjeto(int idProjeto) {
		return baixarJson("http://172.16.129.159:8001/services/indicadores?token=ac4ef0ec195ed24ab08d1e4a8a3a1ed0&projeto_id=" + idProjeto).getAsJsonObject();
	}

}
