package br.com.dextra.dexboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Planilha {

	public static final String CHAVE_PLANILHA = "0Au2Lk990DvFfdGVDQm9rTW1OYmw3dW5yOUVQSkdPSGc";

	private static String gerarUri(int linha, int coluna) {
		return gerarUri("R" + linha + "C" + coluna);
	}

	private static String gerarUri(String celula) {
		return "https://spreadsheets.google.com/feeds/cells/" + CHAVE_PLANILHA + "/1/public/values/" + celula
				+ "?alt=json";
	}

	private static String recuperarConteudoCelula(int linha, int coluna) {
		JsonObject json = UrlDataFetchService.baixarJson(gerarUri(linha, coluna)).getAsJsonObject();
		return json.getAsJsonObject("entry").getAsJsonObject("content").get("$t").getAsString();
	}

	private static int buscarQuantidadeProjetos() {
		// return Integer.parseInt(recuperarConteudoCelula(2, 5));

		// XXX
		return 1;
	}

	private static int buscarIdProjeto(int i) {
		// return Integer.parseInt(recuperarConteudoCelula(i + 1, 1));

		// XXX
		return 397;
	}

	public static JsonArray buscarDadosProjetos() {
		JsonArray ret = new JsonArray();

		int qtdeProjetos = buscarQuantidadeProjetos();

		for (int i = 0; i < qtdeProjetos; ++i) {
			int idProjetoAtual = buscarIdProjeto(i);
			JsonObject projeto = new JsonObject();
			projeto.addProperty("id", idProjetoAtual);
			ret.add(projeto);
		}

		return ret;
	}
}
