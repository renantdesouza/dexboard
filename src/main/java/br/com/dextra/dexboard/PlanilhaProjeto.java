package br.com.dextra.dexboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public class PlanilhaProjeto extends Planilha {

	@SuppressWarnings("unused")
	private Logger LOG = LoggerFactory.getLogger(PlanilhaProjeto.class);

	enum Satisfacao {
		// Mesma ordem da planilha
		MUITO_SATISFEITO, SATISFEITO, INSATISFEITO, MUITO_INSATISFEITO
	}

	enum Qualidade {
		// Mesma ordem da planilha
		evoluir, erros, testar, aprender
	}

	public PlanilhaProjeto(String uriPlanilhaProjeto) {
		super(true, uriPlanilhaProjeto, "DexBoard");
	}

	// -----------------------------------------------------------

	private int buscarUltimoSprintCliente() {
		if (recuperarConteudoCelulaInt(6, 29) != null || recuperarConteudoCelulaInt(6, 29) == 0)
			return recuperarConteudoCelulaInt(6, 29);
		else {
			estrutura = false;
			return 0;
		}
	}

	private int buscarUltimoSprintEquipe() {
		if (recuperarConteudoCelulaInt(4, 29) != null || recuperarConteudoCelulaInt(4, 29) != 0)
			return recuperarConteudoCelulaInt(4, 29);
		else {
			estrutura = false;
			return 0;
		}
	}

	private int buscarUltimoSprintQualidade() {
		if (recuperarConteudoCelulaInt(8, 29) != null || recuperarConteudoCelulaInt(8, 29) != 0)
			return recuperarConteudoCelulaInt(8, 29);
		else {
			estrutura = false;
			return 0;
		}
	}

	private JsonObject buscarSatisfacaoEquipe(int ultimoSprint) {

		List<Integer> resultadoEquipe = recuperarConteudoCelulasInt(3 + ultimoSprint, 2, Satisfacao.values().length);
		List<Integer> andamentoEquipe = recuperarConteudoCelulasInt(3 + ultimoSprint, 6, Satisfacao.values().length);

		JsonObject retEntregue = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			retEntregue.addProperty(Satisfacao.values()[i].name(), resultadoEquipe.get(i));
		}

		JsonObject retAndamento = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			retAndamento.addProperty(Satisfacao.values()[i].name(), andamentoEquipe.get(i));
		}

		JsonObject ret = new JsonObject();
		ret.add("resultadoEntregue", retEntregue);
		ret.add("andamentoDoSprint", retAndamento);

		return ret;
	}

	private JsonObject buscarSatisfacaoCliente(int ultimoSprint) {
		List<Integer> resultadoCliente = recuperarConteudoCelulasInt(3 + ultimoSprint, 10, Satisfacao.values().length);
		List<Integer> andamentoCliente = recuperarConteudoCelulasInt(3 + ultimoSprint, 14, Satisfacao.values().length);

		JsonObject retResultado = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			retResultado.addProperty(Satisfacao.values()[i].name(), resultadoCliente.get(i));
		}

		JsonObject retAndamento = new JsonObject();
		for (int i = 0; i < Satisfacao.values().length; ++i) {
			retAndamento.addProperty(Satisfacao.values()[i].name(), andamentoCliente.get(i));
		}

		JsonObject ret = new JsonObject();
		ret.add("resultadoEntregue", retResultado);
		ret.add("andamentoDoSprint", retAndamento);

		return ret;
	}

	private JsonObject buscarQualidade(int ultimoSprint) {
		List<Integer> qualidade = recuperarConteudoCelulasInt(3 + ultimoSprint, 18, Qualidade.values().length);

		JsonObject ret = new JsonObject();
		for (int i = 0; i < Qualidade.values().length; ++i) {
			ret.addProperty(Qualidade.values()[i].name(), qualidade.get(i));
		}

		return ret;
	}

	private int maiorSprint(int s1, int s2, int s3) {

		if (s1 > s2 && s1 > s3) {
			return s1;
		} else if (s2 > s1 && s2 > s3) {
			return s2;
		} else if (s3 > s1 && s3 > s2) {
			return s3;
		} else return 0;
	}

	// -----------------------------------------------------------

	public JsonObject buscarDadosProjeto() {
		JsonObject ret = new JsonObject();

		if (achouAba) {
			int sprintEquipe = buscarUltimoSprintEquipe();
			int sprintCliente = buscarUltimoSprintCliente();
			int sprintQualidade = buscarUltimoSprintQualidade();

			if (estrutura) {
				ret.addProperty("ultimoSprint", maiorSprint(sprintEquipe, sprintCliente, sprintQualidade));

				if (sprintEquipe > 0) {
					ret.add("satisfacaoEquipe", buscarSatisfacaoEquipe(sprintEquipe));
				}

				if (sprintCliente > 0) {
					ret.add("satisfacaoCliente", buscarSatisfacaoCliente(sprintCliente));
				}

				if (sprintQualidade > 0) {
					ret.add("qualidade", buscarQualidade(sprintQualidade));
				}
			} else {
				ret.add("estrutura", new JsonObject());
			}
		} else {
				ret.add("semDexBoard", new JsonObject());
		}

		return ret;
	}

}
