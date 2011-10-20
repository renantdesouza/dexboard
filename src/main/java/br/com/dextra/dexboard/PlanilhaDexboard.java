package br.com.dextra.dexboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PlanilhaDexboard extends Planilha {

	public PlanilhaDexboard(String chavePlanilha) {
		super(chavePlanilha, 1);
	}

	// -----------------------------------------------------------

	private int buscarQuantidadeDeProjetos() {
		return recuperarConteudoCelulaInt(2, 6);
	}

	private String buscarUriPlanilhaDoProjeto(int indiceProjeto) {
		return recuperarConteudoCelula(2 + indiceProjeto, 3);
	}

	private int buscarIdProjeto(int indiceProjeto) {
		return recuperarConteudoCelulaInt(2 + indiceProjeto, 2);
	}

	// -----------------------------------------------------------

	public JsonArray buscarDadosDosProjetos() {
		JsonArray ret = new JsonArray();

		int qtdeProjetos = buscarQuantidadeDeProjetos();

		for (int i = 0; i < qtdeProjetos; ++i) {
			JsonObject projeto = new JsonObject();
			projeto.addProperty("id", buscarIdProjeto(i));

			String uriPlanilhaDoProjeto = buscarUriPlanilhaDoProjeto(i);
			if (uriPlanilhaDoProjeto != null) {
				acrescentarDadosDaPlanilhaDoProjeto(projeto, uriPlanilhaDoProjeto);
			}

			ret.add(projeto);
		}

		return ret;
	}

	private void acrescentarDadosDaPlanilhaDoProjeto(JsonObject projeto, String uriPlanilhaProjeto) {
		PlanilhaProjeto planilhaProjeto = new PlanilhaProjeto(uriPlanilhaProjeto);
		JsonObject dadosPlanilhaDoProjeto = planilhaProjeto.buscarDadosProjeto();
		Utils.mesclar(projeto, dadosPlanilhaDoProjeto);
	}

}
