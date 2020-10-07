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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sqrlserverjava.SqrlServerOperations;
import com.github.sqrlserverjava.persistence.SqrlIdentity;
import com.github.sqrlserverjava.util.SqrlConfigHelper;
import com.ipomoea.webapp.controller.UserCont;
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
			
			// Get the information of the currently logged in session
			User user = null;
			SqrlIdentity sqrlIdentity = null;
	        final HttpSession session = request.getSession(true);
			if (session != null) {
				user = (User) request.getSession(false).getAttribute(Constants.SESSION_NATIVE_APP_USER);
		        sqrlIdentity = (SqrlIdentity) session.getAttribute(Constants.SESSION_SQRL_IDENTITY);
			}
			
	        // Check if the user exists or if his SQRL identity needs to be registered
			if (user == null) {
				if(sqrlIdentity != null) {	// Check if there is a SQRL identity available to be registered
					// Create a new User and link the sqrlIdentity to the new User
					user = new User();
					UserCont.getInstance().createUser(user);
					sqrlServerOperations.updateNativeUserXref(sqrlIdentity, Long.toString(user.getId()));

					// Set new SESSION_NATIVE_APP_USER 
					session.setAttribute(Constants.SESSION_NATIVE_APP_USER, user);
				}
				else {	//If there is not SQRL identity render login page with error
					logger.error("user is not in session, redirecting to login page");
					RenderLoginServlet.redirectToLoginPageWithError(response, ErrorId.ATTRIBUTES_NOT_FOUND);
					return;
				}
			}
			
			// Set attributes to display
			String accountType = "Password authentication";
			String sqrlId = "";
			if (sqrlServerOperations.fetchSqrlIdentityByUserXref(Long.toString(user.getId())) != null) {
				accountType = "SQRL authentication";
				sqrlId = sqrlServerOperations.fetchSqrlIdentityByUserXref(Long.toString(user.getId())).getIdk();
			}
            session.setAttribute(Constants.SESSION_NATIVE_APP_USER, user);
			session.setAttribute("username", user.getUsername());
			session.setAttribute("firstname", user.getFirstName());
			session.setAttribute("lastname", user.getLastName());
			session.setAttribute("accountType", accountType);
			session.setAttribute("sqrlId", sqrlId);
			
			// Render the login-success.jsp
			request.getRequestDispatcher("login-success.jsp").forward(request, response);
		} catch (Exception e) {
			// SQRL user registration failed
			e.printStackTrace();
			response.setHeader("Location", "login");
			response.setStatus(302);
		}
	}
}
