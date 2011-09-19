package br.com.dextra.dexboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Projeto {

	private static final PlanilhaDexboard PLANILHA_DEXBOARD = new PlanilhaDexboard(
			"0Au2Lk990DvFfdGVDQm9rTW1OYmw3dW5yOUVQSkdPSGc");

	@SuppressWarnings("unused")
	private static final ServicoPma SERVICO_PMA_AMAZON = new ServicoPma(
			"http://50.19.12.252:3000/services/indicadores", "ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	private static final ServicoPma SERVICO_PMA_LOCAL = new ServicoPma("http://localhost:3000/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	private static final ServicoPma SERVICO_PMA = SERVICO_PMA_LOCAL;

	public static JsonArray buscarDadosProjetos() {
		JsonArray dadosPlanilha = PLANILHA_DEXBOARD.buscarDadosDosProjetos();

		JsonArray ret = new JsonArray();
		for (JsonElement elemento : dadosPlanilha) {
			int idProjeto = Integer.parseInt(elemento.getAsJsonObject().get("id").getAsString());

			// Busca dados do PMA
			JsonObject dados = SERVICO_PMA.buscarDadosDoProjeto(idProjeto);

			// Acrescenta a estes os outros dados, vindos da planilha
			Utils.mesclar(dados, elemento.getAsJsonObject());

			ret.add(dados);
		}
		return ret;
	}

}
