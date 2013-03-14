package br.com.dextra.dexboard.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		UserService service = UserServiceFactory.getUserService();
		String uri = request.getRequestURI();
		if (uri.equals("/logout")) {
			response.sendRedirect(service.createLogoutURL("/"));
			return;
		}
		User user = service.getCurrentUser();
		if (user != null || uri.startsWith("/_ah") || uri.startsWith("/dados/")) {
			chain.doFilter(request, response);
			return;
		}
		if ("GET".equals(request.getMethod())) {
			response.sendRedirect(service.createLoginURL(uri));
			return;
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.append("<html><head><title>Murkfight</title></head><body>You need login <a href=\"");
		writer.append(service.createLoginURL("/"));
		writer.append("\">here</a></body></html>");
		return;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
