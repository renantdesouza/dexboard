package br.com.dextra.dexboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

public abstract class Planilha {

	protected final String chave;

	protected Planilha(String chave) {
		this.chave = chave;
	}

	public static void main(String[] args) {
		System.out.println(new PlanilhaProjeto("https://docs.google.com/a/dextra-sw.com/spreadsheet/ccc?key=0AuUR54zFnHLOdHdrR3BNVHJ0b2J2QWRsekRxR29RN2c&hl=en_US#gid=0").chave);
	}

	@SuppressWarnings("unused")
	// O parametro boolean serve somente para diferenciar os construtores
	protected Planilha(String uri, boolean criarUsandoUri) {

		// Usa expressao regular para extrair da URI a chave da planilha

		Matcher matcher = Pattern.compile("key=(.+?)([&#]|$)").matcher(uri);

		if (!matcher.find()) {
			throw new IllegalArgumentException("Esta URI nao representa uma planilha do Google Docs valida: " + uri);
		}

		this.chave = matcher.group(1);
	}

	protected String gerarUri(String celula) {
		return "https://spreadsheets.google.com/feeds/cells/" + chave + "/1/public/values/" + celula + "?alt=json";
	}

	protected String gerarUri(int linha, int coluna) {
		return gerarUri("R" + linha + "C" + coluna);
	}

	protected String recuperarConteudoCelula(int linha, int coluna) {
		JsonObject json = Utils.baixarJson(gerarUri(linha, coluna)).getAsJsonObject();
		String asString = json.getAsJsonObject("entry").getAsJsonObject("content").get("$t").getAsString();
		return asString.isEmpty() ? null : asString;
	}

}
