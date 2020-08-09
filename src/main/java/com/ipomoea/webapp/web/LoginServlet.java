package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ipomoea.webapp.controller.UserCont;
import com.ipomoea.webapp.model.User;


@WebServlet("/auth")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
		System.out.println("Authenticating User");
        authenticate(request, response);
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response){
    	try {
        	String username = request.getParameter("username");
            String password = request.getParameter("password");
			User user = UserCont.getInstance().fetchUserByUsername(username);
			final HttpSession session = request.getSession(true);

			if(user.getPassword() == null) {	//Password Authentication disabled
				System.out.println("Password authentication disabled");
				RenderLoginServlet.redirectToLoginPageWithError(response, ErrorId.PASSWORD_DISABLED);
	            return;
			}
			else if(user.getPassword().equals(password)) {	//successful authentication
	    		System.out.println("Successful authentication");
				session.setAttribute(Constants.SESSION_NATIVE_APP_USER, user);
	    		response.setHeader("Location", "success");
	    		response.setStatus(302);
	    		//request.getRequestDispatcher("login-success.jsp").forward(request, response);
	            return;
			}
			else {	//Password does not match
				System.out.println("Password does not match");
				RenderLoginServlet.redirectToLoginPageWithError(response, ErrorId.INVALID_USERNAME_OR_PASSWORD);
	            return;
			}
		} catch (Exception e) {
			System.out.println("Error while authenticating");
	        e.printStackTrace();
	        RenderLoginServlet.redirectToLoginPageWithError(response, ErrorId.INVALID_USERNAME_OR_PASSWORD);
            return;
		}
    }
}