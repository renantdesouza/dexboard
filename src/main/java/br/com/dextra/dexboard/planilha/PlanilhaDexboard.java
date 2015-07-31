package br.com.dextra.dexboard.planilha;

public class PlanilhaDexboard extends Planilha {

	private static final String CHAVE_PLANILHA_DEXBOARD_PRODUCAO = "1Fjul3zOetgENCTfnnd3gDaxOEh3WaIQr4uV3Th2wRQo";

	private static final String CHAVE_PLANILHA_DEXBOARD_TESTE = "11z7PwH4sADaiXxshNPYr-88u3flqewCcGRGJok9cl6o";

	public PlanilhaDexboard(String nomePlanilha) {
		super(getChavePlanilha(), nomePlanilha);
	}

	private static String getChavePlanilha() {
		if (isTestEnvironment()) {
			return CHAVE_PLANILHA_DEXBOARD_TESTE;
		}
		return CHAVE_PLANILHA_DEXBOARD_PRODUCAO;
	}

	private static boolean isTestEnvironment() {
		return System.getProperty("app.test") != null && System.getProperty("app.test").equals("true");
	}
}
