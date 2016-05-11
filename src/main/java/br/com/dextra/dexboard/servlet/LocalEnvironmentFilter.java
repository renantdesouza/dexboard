package br.com.dextra.dexboard.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.com.dextra.dexboard.servlet.dev.LocalGaeEnvironment;

public class LocalEnvironmentFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		if (Context.isTestEnvironment()) {
			try (LocalGaeEnvironment env = LocalGaeEnvironment.setUp()) {
				chain.doFilter(req, resp);
			}
		} else {
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}
}
