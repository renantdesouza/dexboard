package br.com.dextra.dexboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Projeto {

	public static JsonArray buscarDadosProjetos() {
		JsonArray dadosPlanilha = Planilha.buscarDadosProjetos();

		JsonArray ret = new JsonArray();
		for (JsonElement elemento : dadosPlanilha) {
			int idProjeto = Integer.parseInt(elemento.getAsJsonObject().get("id").getAsString());

			// Busca dados do PMA
			JsonObject dados = Pma.buscarDadosProjeto(idProjeto);

			// Acrescenta a esses dados os dados vindos da planilha
			dados.addProperty("id", idProjeto);

			ret.add(dados);
		}
		return ret;
	}

}
