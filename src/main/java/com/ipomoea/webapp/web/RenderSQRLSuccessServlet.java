package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.sqrlserverjava.util.SqrlUtil;
import com.ipomoea.webapp.model.User;

@WebServlet(urlPatterns = { "/success" })
public class RenderSQRLSuccessServlet extends HttpServlet {
	private static final long	serialVersionUID	= 3L;
	
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println("Render SQRL success page.");
			User user = null;
			if (request.getSession(false) != null) {
				user = (User) request.getSession(false).getAttribute(Constants.SESSION_NATIVE_APP_USER);
			}

			request.getRequestDispatcher("login-success.jsp").forward(request, response);
		} catch (final RuntimeException e) {
			response.setHeader("Location", "login");
		}
	}
}
