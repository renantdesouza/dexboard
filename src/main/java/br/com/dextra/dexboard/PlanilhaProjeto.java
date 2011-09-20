package br.com.dextra.dexboard;

import java.util.List;

import com.google.gson.JsonObject;

public class PlanilhaProjeto extends Planilha {

	enum Satisfacao {
		// Mesma ordem da planilha
		MUITO_SATISFEITO, SATISFEITO, INSATISFEITO, MUITO_INSATISFEITO
	}

	public PlanilhaProjeto(String uriPlanilhaProjeto) {
		super(true, uriPlanilhaProjeto, 10);
	}

	// -----------------------------------------------------------

	private int buscarUltimoSprint() {
		return recuperarConteudoCelulaInt(2, 18);
	}

	private JsonObject buscarSatisfacaoEquipe(int ultimoSprint) {
		List<Integer> satisfacaoEquipe = recuperarConteudoCelulasInt(2 + ultimoSprint, 2, Satisfacao.values().length);

		JsonObject ret = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			ret.addProperty(Satisfacao.values()[i].name(), satisfacaoEquipe.get(i));
		}

		return ret;
	}

	private JsonObject buscarSatisfacaoCliente(int ultimoSprint) {
		List<Integer> satisfacaoCliente = recuperarConteudoCelulasInt(2 + ultimoSprint, 6, Satisfacao.values().length);

		JsonObject ret = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			ret.addProperty(Satisfacao.values()[i].name(), satisfacaoCliente.get(i));
		}

		return ret;
	}

	private JsonObject buscarQualidade(int ultimoSprint) {
		List<Integer> qualidade = recuperarConteudoCelulasInt(2 + ultimoSprint, 10, Satisfacao.values().length);

		JsonObject ret = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			ret.addProperty(Satisfacao.values()[i].name(), qualidade.get(i));
		}

		return ret;
	}

	// -----------------------------------------------------------

	public JsonObject buscarDadosProjeto() {
		JsonObject ret = new JsonObject();

		int ultimoSprint = buscarUltimoSprint();

		ret.addProperty("ultimoSprint", ultimoSprint);
		ret.add("satisfacaoEquipe", buscarSatisfacaoEquipe(ultimoSprint));
		ret.add("satisfacaoCliente", buscarSatisfacaoCliente(ultimoSprint));
		ret.add("qualidade", buscarQualidade(ultimoSprint));

		return ret;
	}

}
