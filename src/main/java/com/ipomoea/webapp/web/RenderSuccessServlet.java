package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/success" })
public class RenderSuccessServlet extends HttpServlet {
	private static final long	serialVersionUID	= 3L;
	
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println("Render success page.");

			request.getRequestDispatcher("login-success.jsp").forward(request, response);
		} catch (final RuntimeException e) {
			response.setHeader("Location", "login");
		}
	}
}
