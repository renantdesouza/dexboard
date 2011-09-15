package br.com.dextra.dexboard;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PlanilhaDataFetcher extends DataFetcher {

	public static final String CHAVE_PLANILHA = "0Ano2Pm_gaVoXdExHY1VuT1haRlRvenc0bmpyNDVUWlE";

	public static final String CELULA = "R3C1";

	public static final String URI_PLANILHA = "https://spreadsheets.google.com/feeds/cells/" + CHAVE_PLANILHA
			+ "/1/public/values/" + CELULA + "?alt=json";

	public static JsonElement fetchData() {
		JsonObject json = fetchData(URI_PLANILHA).getAsJsonObject();
		System.out.println(json.getAsJsonObject("entry").getAsJsonObject("content").get("$t").getAsString());
		return json;
	}

}
