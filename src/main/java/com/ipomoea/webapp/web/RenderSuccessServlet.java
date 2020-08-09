package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sqrlserverjava.SqrlServerOperations;
import com.github.sqrlserverjava.util.SqrlConfigHelper;
import com.ipomoea.webapp.model.User;

@WebServlet(urlPatterns = { "/success" })
public class RenderSuccessServlet extends HttpServlet {
	private static final long	serialVersionUID	= 3L;
	private static final Logger	logger				= LoggerFactory.getLogger(RenderSuccessServlet.class);

	private final SqrlServerOperations sqrlServerOperations = new SqrlServerOperations(
			SqrlConfigHelper.loadFromClasspath());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println("Render success page.");
			
			User user = null;
			if (request.getSession(false) != null) {
				user = (User) request.getSession(false).getAttribute(Constants.SESSION_NATIVE_APP_USER);
			}
			if (user == null) {
				logger.error("user is not in session, redirecting to login page");
				RenderLoginServlet.redirectToLoginPageWithError(response, ErrorId.ATTRIBUTES_NOT_FOUND);
				return;
			}

	        final HttpSession session = request.getSession(true);
	        
			// Set account type
			String accountType = "Password authentication";
			String sqrlId = "";
			if (sqrlServerOperations.fetchSqrlIdentityByUserXref(Long.toString(user.getId())) != null) {
				accountType = "SQRL authentication";
				sqrlId = sqrlServerOperations.fetchSqrlIdentityByUserXref(Long.toString(user.getId())).getIdk();
			}
			
			//Set attributes to display
            session.setAttribute(Constants.SESSION_NATIVE_APP_USER, user);
			session.setAttribute("username", user.getUsername());
			session.setAttribute("firstname", user.getFirstName());
			session.setAttribute("lastname", user.getLastName());
			session.setAttribute("accountType", accountType);
			session.setAttribute("sqrlId", sqrlId);
		
			request.getRequestDispatcher("login-success.jsp").forward(request, response);
		} catch (final RuntimeException e) {
			e.printStackTrace();
			response.setHeader("Location", "login");
			response.setStatus(302);
		}
	}
}
