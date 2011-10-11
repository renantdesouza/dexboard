package br.com.dextra.dexboard;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		if("/".equals(uri)) {
			resp.sendRedirect("/index.html");
			return;
		}

		Principal principal = req.getUserPrincipal();
		if(principal != null) {
			LOG.info("User: " + principal.getName());
			chain.doFilter(request, response);
			return;
		}

		LOG.info("Redirect to login");
		UserService userService = UserServiceFactory.getUserService();
		String url = userService.createLoginURL(uri);
		resp.sendRedirect(url);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
