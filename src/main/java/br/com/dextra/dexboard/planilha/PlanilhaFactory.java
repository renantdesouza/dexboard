package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.mock.MockPlanilhaIndicadores;
import br.com.dextra.dexboard.mock.MockPlanilhaPrincipal;
import br.com.dextra.dexboard.servlet.Context;

public class PlanilhaFactory {
	
	private static final boolean MOCK = Context.isDevelopmentEnvironment();
	
	public static PlanilhaIndicadores indicadores() {
		return (MOCK) ? new MockPlanilhaIndicadores() : new PlanilhaIndicadoresImpl();
	}
	
	public static PlanilhaPrincipal principal() {
		return (MOCK) ? new MockPlanilhaPrincipal() : new PlanilhaPrincipalImpl();
		
	} 

}
