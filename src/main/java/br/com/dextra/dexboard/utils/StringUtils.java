package br.com.dextra.dexboard.utils;

public class StringUtils {

	public static boolean isNullOrEmpty(String parametro) {
		return parametro == null || parametro.trim().equals("");
	}
}
