package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * @email Ramesh Fadatare
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1;
    
    @Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
    	System.out.println("Logout Servlet");
		doPost(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		try {
			final HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			// Delete Cookies
			if (request == null || request.getCookies() == null) {
				return;
			}
			for (final Cookie cookie : request.getCookies()) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
			
			//response.setStatus(302);
			//response.setHeader("Location", "login");
			getServletContext().getRequestDispatcher("/index.jsp").forward (request, response);
		} catch (final RuntimeException e) {
			e.printStackTrace();
			//ToDo show error
		}
	}
}