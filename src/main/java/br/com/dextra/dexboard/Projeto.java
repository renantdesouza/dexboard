package br.com.dextra.dexboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Projeto {


	//teste
	private static final ServicoPma SERVICO_PMA_AMAZON = new ServicoPma(
			"https://50.17.210.152/pma/services/indicadores", "ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	@SuppressWarnings("unused")
	private static final ServicoPma SERVICO_PMA_LOCAL = new ServicoPma("http://localhost:3000/services/indicadores",
			"ac4ef0ec195ed24ab08d1e4a8a3a1ed0");

	private static final ServicoPma SERVICO_PMA = SERVICO_PMA_AMAZON;

	private static JsonArray cache = null;

	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(Projeto.class);

	public static JsonArray buscarDadosProjetos() {

		if (cache == null) {
			JsonArray dadosPlanilha = new PlanilhaDexboard(
					"0Au2Lk990DvFfdGVDQm9rTW1OYmw3dW5yOUVQSkdPSGc").buscarDadosDosProjetos();

			cache = new JsonArray();

			for (JsonElement elemento : dadosPlanilha) {
				int idProjeto = Integer.parseInt(elemento.getAsJsonObject().get("id").getAsString());
				// Busca dados do PMA
				JsonObject dados = SERVICO_PMA.buscarDadosDoProjeto(idProjeto);

				// Acrescenta a estes os outros dados, vindos da planilha
				Utils.mesclar(dados, elemento.getAsJsonObject());

				cache.add(dados);
			}
		}

		return cache;
	}

	public static void limparCache() {
		cache = null;
	}

}
