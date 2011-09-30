package br.com.dextra.dexboard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public abstract class Planilha {

	private static final Logger LOG = LoggerFactory.getLogger(Planilha.class);

	private final int idAba;
	private final String chave;
	private final CellFeed feed;

	private final Map<Integer, Map<Integer, String>> cache = new HashMap<Integer, Map<Integer, String>>();

	private static final SpreadsheetService service = new SpreadsheetService("DexBoard");
	private static final FeedURLFactory factory = FeedURLFactory.getDefault();

	static {
		final String usuario = "build-continua@dextra-sw.com";
		final String senha = "2275N5";
		try {
			service.setUserCredentials(usuario, senha);
		} catch (AuthenticationException e) {
			LOG.error("Problemas na autenticacao do usuario " + usuario);
		}
	}

	private CellFeed criarFeed() {
		try {
			URL cellFeedUrl = factory.getCellFeedUrl(chave, String.valueOf(idAba), "public", "basic");
			return service.getFeed(cellFeedUrl, CellFeed.class);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	protected Planilha(String chave, int idAba) {
		this.chave = chave;
		this.idAba = idAba;
		this.feed = criarFeed();
	}

	@SuppressWarnings("unused")
	// O parametro boolean serve somente para diferenciar os construtores
	protected Planilha(boolean criarUsandoUri, String uri, int idAba) {

		// Usa expressao regular para extrair da URI a chave da planilha

		Matcher matcher = Pattern.compile("key=(.+?)([&#]|$)").matcher(uri);

		if (!matcher.find()) {
			throw new IllegalArgumentException("Esta URI nao representa uma planilha do Google Docs valida: " + uri);
		}

		this.chave = matcher.group(1);
		this.idAba = idAba;
		this.feed = criarFeed();
	}

	protected String gerarUriPlanilha() {
		return "https://docs.google.com/spreadsheet/ccc?key=" + chave + "#gid=" + idAba;
	}

//	protected String gerarUri(String celula) {
//		return "https://spreadsheets.google.com/feeds/cells/" + chave + "/" + idAba + "/public/values/" + celula + "?alt=json";
//	}

//	protected String gerarUri(int linha, int coluna) {
//		LOG.error("Recuperando " + linha + ", " + coluna);
//		return gerarUri("R" + linha + "C" + coluna);
//	}

	// -----------------------------------------------------------

	protected String recuperarConteudoCelula(int linha, int coluna) {
		Map<Integer, String> mapa = cache.get(linha);
		if (mapa == null) {
			mapa = cache.put(linha, new HashMap<Integer, String>());
		}

		if (mapa.containsKey(coluna)) {
			// Esta' no cache
			return mapa.get(coluna);
		} else {
			// Buscar da internet

			// Insere no cache
			mapa.put(coluna, recuperaValorCelulaDaInternet(linha, coluna));

			// Retorna o valor
			return mapa.get(coluna);
		}
	}

	protected String recuperaValorCelulaDaInternet (int linha, int coluna) {

	    for (CellEntry entry : feed.getEntries()) {
	    	Matcher matcher = Pattern.compile("R(\\d+)C(\\d+)").matcher(entry.getId());

			if (!matcher.find()) {
				throw new IllegalArgumentException("Este identificador nao representa uma entrada valida de planilha: " + entry.getId());
			}

	    	int linhaCellEntry = Integer.parseInt(matcher.group(1));
	    	int colunaCellEntry = Integer.parseInt(matcher.group(2));

	    	if (linha == linhaCellEntry && coluna == colunaCellEntry) {
	    		return entry.getTextContent().getContent().getPlainText();
	    	}
	    }

//	    throw new NoSuchElementException("R" + linha + "C" + coluna + " da planilha " + gerarUriPlanilha());
	    return null;

	}

	protected Integer recuperarConteudoCelulaInt(int linha, int coluna) {
		String conteudo = recuperarConteudoCelula(linha, coluna);
		return conteudo == null ? null : Integer.valueOf(conteudo);
	}

	protected List<String> recuperarConteudoCelulas(int linha, int colunaInicial, int quantasColunas) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < quantasColunas; ++i) {
			ret.add(recuperarConteudoCelula(linha, colunaInicial + i));
		}
		return ret;
	}

	protected List<Integer> recuperarConteudoCelulasInt(int linha, int colunaInicial, int quantasColunas) {
		List<Integer> ret = new ArrayList<Integer>();
		for (int i = 0; i < quantasColunas; ++i) {
			ret.add(recuperarConteudoCelulaInt(linha, colunaInicial + i));
		}
		return ret;
	}

}
