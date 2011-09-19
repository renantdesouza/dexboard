package br.com.dextra.dexboard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public abstract class Planilha {

	private static final Logger LOG = LoggerFactory.getLogger(Planilha.class);

	protected final String chave;
	private final int numeroAba;

	protected Planilha(String chave, int numeroAba) {
		this.chave = chave;
		this.numeroAba = numeroAba;
	}

	@SuppressWarnings("unused")
	// O parametro boolean serve somente para diferenciar os construtores
	protected Planilha(String uri, boolean criarUsandoUri, int numeroAba) {

		// Usa expressao regular para extrair da URI a chave da planilha

		Matcher matcher = Pattern.compile("key=(.+?)([&#]|$)").matcher(uri);

		if (!matcher.find()) {
			throw new IllegalArgumentException("Esta URI nao representa uma planilha do Google Docs valida: " + uri);
		}

		this.chave = matcher.group(1);

		this.numeroAba = numeroAba;
	}

	protected String gerarUri(String celula) {
		return "https://spreadsheets.google.com/feeds/cells/" + chave + "/" + numeroAba + "/public/values/" + celula + "?alt=json";
	}

	protected String gerarUri(int linha, int coluna) {
		LOG.error("Recuperando " + linha + ", " + coluna);
		return gerarUri("R" + linha + "C" + coluna);
	}

	// -----------------------------------------------------------

	protected String recuperarConteudoCelula(int linha, int coluna) {
		JsonObject json = Utils.baixarJson(gerarUri(linha, coluna)).getAsJsonObject();
		String asString = json.getAsJsonObject("entry").getAsJsonObject("content").get("$t").getAsString();
		return asString.isEmpty() ? null : asString;
	}

	protected Integer recuperarConteudoCelulaInt(int linha, int coluna) {
		LOG.error("Recuperando " + linha + ", " + coluna);
		String conteudo = recuperarConteudoCelula(linha, coluna);
		return conteudo == null ? null : Integer.valueOf(conteudo);
	}

	protected List<String> recuperarConteudoCelulas(int linha, int colunaInicial, int quantasColunas) {
		// TODO Melhorar desempenho. Atualmente, esta' abrindo uma conexao para cada celula.

		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < quantasColunas; ++i) {
			ret.add(recuperarConteudoCelula(linha, colunaInicial + i));
		}
		return ret;
	}

	protected List<Integer> recuperarConteudoCelulasInt(int linha, int colunaInicial, int quantasColunas) {
		// TODO Melhorar desempenho. Atualmente, esta' abrindo uma conexao para cada celula.

		List<Integer> ret = new ArrayList<Integer>();
		for (int i = 0; i < quantasColunas; ++i) {
			ret.add(recuperarConteudoCelulaInt(linha, colunaInicial + i));
		}
		return ret;
	}

}
