package com.ipomoea.webapp.web;
/**
 * @author Marie-Luise Lux
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/login" })
public class RenderLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 4;
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("RenderLogin");
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
