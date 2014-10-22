package br.com.dextra.dexboard.planilha;


public class PlanilhaDexboard extends Planilha {

	private static final String CHAVE_PLANILHA_DEXBOARD_PRODUCAO = "0AiEZezeUULf3dHpROW5aMVl6VHZkRmdrZXhObG4teGc";

	private static final String CHAVE_PLANILHA_DEXBOARD_TESTE = "0AiEZezeUULf3dEFLZmJRYTlNRmNqTHRvZ2FBM3p6SEE";

	public PlanilhaDexboard(String nomePlanilha) {
		super(getChavePlanilha(), nomePlanilha);
	}

	private static String getChavePlanilha() {
		if(isTestEnvironment()) {
			return CHAVE_PLANILHA_DEXBOARD_TESTE;
		}
		return CHAVE_PLANILHA_DEXBOARD_PRODUCAO;
	}

	private static boolean isTestEnvironment() {
		return System.getProperty("app.test") != null && System.getProperty("app.test").equals("true");
	}
}
