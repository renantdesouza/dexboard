package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.utils.StringUtils;
import com.github.feroult.gapi.GoogleAPI;
import com.github.feroult.gapi.SpreadsheetAPI;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

abstract class Planilha {

	private final String sheetName;

	private SpreadsheetAPI spreadSheet;

	private List<Map<String, String>> spreadSheetMap;

	protected Planilha(String chave, String sheet) {
		sheetName = sheet;
		spreadSheet = new GoogleAPI().spreadsheet(chave);

		if (spreadSheet == null) {
			throw new RuntimeException("SpreadSheet " + chave + " does not exist or certificate lacks permission to view it.");
		}

		sheetNameNotExist();

		spreadSheetMap = spreadSheet.worksheet(sheetName).asMap();
	}

	protected String recuperarConteudoCelula(int linha, int coluna) {
		sheetNameNotExist();
		return spreadSheet.worksheet(sheetName).getValue(linha, coluna);
	}

	private void sheetNameNotExist() {
		if (!spreadSheet.hasWorksheet(sheetName)) {
			throw new RuntimeException("worksheet " + sheetName + " does not exist.");
		}
	}

	protected String recuperarConteudoCelula(int linha, String nomeColuna) {
		try {
			return spreadSheetMap.get(linha).get(normalizarNomeColuna(nomeColuna));
		} catch (IndexOutOfBoundsException aiobe) {
			return null;
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	private String normalizarNomeColuna(String nomeColuna) {
		return nomeColuna.replaceAll(" ", "").toLowerCase();
	}

	protected Integer recuperarConteudoCelulaInt(int linha, int coluna) {
		return Integer.valueOf(recuperarConteudoCelula(linha, coluna));
	}

	protected Integer recuperarConteudoCelulaInt(int linha, String coluna) {
		return Integer.valueOf(recuperarConteudoCelula(linha, coluna));
	}

	protected Double recuperarConteudoCelulaDouble(int linha, int coluna) {
		return parseDouble(recuperarConteudoCelula(linha, coluna));
	}

	protected Double recuperarConteudoCelulaDouble(int linha, String coluna) {
		return parseDouble(recuperarConteudoCelula(linha, coluna));
	}

	private static Double parseDouble(String value) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');

		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(symbols);

		try {
			return StringUtils.isNullOrEmpty(value) || !value.matches("-?\\d+(\\.|\\,\\d+)?") ? null : df.parse(value).doubleValue();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}