package br.com.dextra.dexboard;

import java.util.ArrayList;
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

	//@SuppressWarnings("unused")
	private List<Integer> tresUltimosSprints(int ultimoSprint, int coluna) {
		List<Integer> valores = new ArrayList<Integer>();

		int cont = 0;
		for (int i = ultimoSprint+4; i > 4; i-- ) {
			if (recuperarConteudoCelulaInt(i, coluna) != null) {
				valores.add(recuperarConteudoCelulaInt(i, coluna));
				if (cont == 2) {
					break;
				}
				cont++;
			}
		}

		if (cont < 2) {
			for (int i = cont; i < 3; i++) {
				valores.add(0);
			}
		}

		return valores;
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
		List<Integer> indices = tresUltimosSprints(ultimoSprint, 25);

		JsonObject retResultado = new JsonObject();
		JsonObject retAndamento = new JsonObject();
		for (int i = 0; i < indices.size(); i++) {

			if (indices.get(i) > 0) {
				List<Integer> resultadoCliente = recuperarConteudoCelulasInt(3 + indices.get(i), 10, Satisfacao.values().length);
				List<Integer> andamentoCliente = recuperarConteudoCelulasInt(3 + indices.get(i), 14, Satisfacao.values().length);

				JsonObject retResultadoAux = new JsonObject();
				for (int j = 0; j < Satisfacao.values().length; ++j) {
					retResultadoAux.addProperty(Satisfacao.values()[j].name(), resultadoCliente.get(j));
				}

				JsonObject retAndamentoAux = new JsonObject();
				for (int j = 0; j < Satisfacao.values().length; ++j) {
					retAndamentoAux.addProperty(Satisfacao.values()[j].name(), andamentoCliente.get(j));
				}

				retResultado.add("sprint"+i, retResultadoAux);
				retAndamento.add("sprint"+i, retAndamentoAux);
			}
		}

		JsonObject ret = new JsonObject();
		ret.add("resultadoEntregue", retResultado);
		ret.add("andamentoDoSprint", retAndamento);

		return ret;
	}

	/*private JsonObject buscarSatisfacaoCliente(int ultimoSprint) {
		List<Integer> resultadoCliente = recuperarConteudoCelulasInt(3 + ultimoSprint, 10, Satisfacao.values().length);
		List<Integer> andamentoCliente = recuperarConteudoCelulasInt(3 + ultimoSprint, 14, Satisfacao.values().length);

		JsonObject retResultado = new JsonObject();
		for (int j = 0; j < Satisfacao.values().length; ++j) {
			retResultado.addProperty(Satisfacao.values()[j].name(), resultadoCliente.get(j));
		}

		JsonObject retAndamento = new JsonObject();
		for (int j = 0; j < Satisfacao.values().length; ++j) {
			retAndamento.addProperty(Satisfacao.values()[j].name(), andamentoCliente.get(j));
		}

		JsonObject ret = new JsonObject();
		ret.add("resultadoEntregue", retResultado);
		ret.add("andamentoDoSprint", retAndamento);

		return ret;
	}*/

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
