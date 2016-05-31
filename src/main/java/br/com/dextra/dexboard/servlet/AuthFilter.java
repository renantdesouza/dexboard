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
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		UserService service = UserServiceFactory.getUserService();

		String uri = request.getRequestURI();
		User user = service.getCurrentUser();

		if (isAdminPath(uri)) {
			chain.doFilter(request, response);
			return;
		}

		if (user == null) {
			if ("GET".equals(request.getMethod())) {
				goToLoginPage(response, service, uri);
				return;
			}

			unautorizedRequest(response, service);
			return;
		}

		if (isLogoutPath(uri) || !isDextraUser(user)) {
			goToLogoutPage(response, service);
			return;
		}

		chain.doFilter(request, response);
		return;
	}

	private boolean isAdminPath(String uri) {
		return uri.startsWith("/_ah") || uri.startsWith("/cron/") || uri.startsWith("/_tools");
	}

	private void unautorizedRequest(HttpServletResponse response, UserService service) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.append("<html><head><title>Murkfight</title></head><body>You need login <a href=\"");
		writer.append(service.createLoginURL("/"));
		writer.append("\">here</a></body></html>");
	}

	private void goToLoginPage(HttpServletResponse response, UserService service, String uri) throws IOException {
		response.sendRedirect(service.createLoginURL(uri));
	}

	private void goToLogoutPage(HttpServletResponse response, UserService service) throws IOException {
		response.sendRedirect(service.createLogoutURL("/"));
	}

	private boolean isLogoutPath(String uri) {
		return uri.equals("/logout");
	}

	private boolean isDextraUser(User user) {
		return user.getEmail().endsWith("@dextra-sw.com");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
}
