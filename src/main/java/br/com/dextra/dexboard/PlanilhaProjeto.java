package br.com.dextra.dexboard;

import com.google.gson.JsonObject;

public class PlanilhaProjeto extends Planilha {

	public PlanilhaProjeto(String uriPlanilhaProjeto) {
		super(uriPlanilhaProjeto, true);
	}

	public JsonObject buscarDadosProjeto() {
		// TODO
		JsonObject ret = new JsonObject();
		ret.addProperty("TODO", "Buscar dados da planilha do projeto");
		return ret;
	}

}
