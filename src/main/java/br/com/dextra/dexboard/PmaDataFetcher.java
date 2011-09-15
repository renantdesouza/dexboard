package br.com.dextra.dexboard;

import com.google.gson.JsonElement;

public class PmaDataFetcher extends DataFetcher {

	public static JsonElement fetchData(int idProjeto) {
		return fetchData("http://172.16.129.159:8001/services/indicadores?token=ac4ef0ec195ed24ab08d1e4a8a3a1ed0&projeto_id=" + idProjeto);
	}

}
