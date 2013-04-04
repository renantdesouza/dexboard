package br.com.dextra.dexboard.dao;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;

import com.googlecode.objectify.ObjectifyService;

public class RegisterClasses {

	public static void register() {
		if (System.getProperty("app.objectfy.classes.registered") != null) {
			return;
		}

		ObjectifyService.register(Projeto.class);
		ObjectifyService.register(Indicador.class);

		System.setProperty("app.objectfy.classes.registered", "true");
	}
}
