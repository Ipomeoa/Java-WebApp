package com.ipomoea.webapp.web;

import java.io.IOException;
import java.sql.SQLException;

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
import com.github.sqrlserverjava.util.SqrlUtil;
import com.ipomoea.webapp.controller.UserCont;
import com.ipomoea.webapp.model.User;

@WebServlet(urlPatterns = { "/linkaccount" })
public class LinkAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 5609899766821704630L;

	private static final Logger			logger					= LoggerFactory.getLogger(LinkAccountServlet.class);
	private final SqrlServerOperations	sqrlServerOperations	= new SqrlServerOperations(
			SqrlConfigHelper.loadFromClasspath());

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		logger.info(SqrlUtil.logEnterServlet(request));
		final HttpSession session = request.getSession(true);
		try {
			final User user = validateRequestAndAuthenticateUser(request, response);
			if (user == null) {
				//***************To Do! Reload page with error message!***************//
				System.out.println("To Do! Reload page with error message! User==null");
				return;
			} else {
				// We have a valid user to link
				final SqrlIdentity sqrlIdentity = (SqrlIdentity) session.getAttribute(Constants.SESSION_SQRL_IDENTITY);
				sqrlServerOperations.updateNativeUserXref(sqrlIdentity, Long.toString(user.getId()));
				session.setAttribute(Constants.SESSION_NATIVE_APP_USER, user);
				user.setPassword(null);
				UserCont.getInstance().updateUser(user);
				// All done, send them to the app page
	    		System.out.println("Successfull linking of accounts");
				
				response.setHeader("Location", "success");
				response.setStatus(302); // we use 302 to make it easy to understand what the example app is doing, but
				// a real app might do a server side redirect instead
				return;
			}
		} catch (final RuntimeException | SQLException e) {
			logger.error("Error in LinkAccountServlet", e);
			//***************To Do! Reload page with error message!***************//
			System.out.println("To Do! Reload page with error message! Error in LinkAccountServlet");
		}
	}

	private User validateRequestAndAuthenticateUser(final HttpServletRequest request,
			final HttpServletResponse response) throws SQLException, ServletException, IOException {
		try {
        	String username = request.getParameter("username");
            String password = request.getParameter("password");
            
			if (username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0) {
				//***************To Do! Reload page with missing credentials Error!***************//
				request.getRequestDispatcher("WEB-INF/linkaccounts.jsp").forward(request, response);
				return null;
			}
			
			User user = UserCont.getInstance().fetchUserByUsername(username);

			if(user == null) { //No such user
				//***************To Do! Reload page with wrong credentials Error!***************//
				request.getRequestDispatcher("linkaccounts.jsp").forward(request, response);
				return null;
			}
			else if(!user.getPassword().equals(password)) {	//wrong password
				//***************To Do! Reload page with wrong credentials Error!***************//
				request.getRequestDispatcher("linkaccounts.jsp").forward(request, response);
				return null;
			}
			else {
				// All good, let the user can be linked
				return user;
			}
		} catch (Exception e) {
	        e.printStackTrace();
			return null;
		}
	}

}
