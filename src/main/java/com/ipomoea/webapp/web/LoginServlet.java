package com.ipomoea.webapp.web;

import java.io.IOException;
import com.ipomoea.webapp.web.Constants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ipomoea.webapp.controller.UserCont;
import com.ipomoea.webapp.model.User;

/**
 * @email Ramesh Fadatare
 */

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
        boolean auth = authenticate(request, response);
        
        if(auth) {//succesfull authentication
    		System.out.println("successfull authentication");
    		response.setHeader("Location", "success");
    		response.setStatus(302);
        }
        else {//unsuccessfull authentication
			request.setAttribute("loginerror", "<p style=\"color:red\">Invalid username or password!<p>");
    		System.out.println("failed authentication");
			//response.setHeader("Location", "login");
    		//response.setStatus(302);
			request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    private boolean authenticate(HttpServletRequest request, HttpServletResponse response){
    	try {
        	String username = request.getParameter("username");
            String password = request.getParameter("password");
			User user = UserCont.getInstance().fetchUserByUsername(username);
			
			if(user.getPassword().equals(password)) {	//succesfull authentication
		        final HttpSession session = request.getSession(true);
	            session.setAttribute(Constants.SESSION_NATIVE_APP_USER, user);
				session.setAttribute("username", user.getUsername());
				session.setAttribute("firstname", user.getFirstName());
				session.setAttribute("lastname", user.getLastName());
	            return true;
			}
			else {	//Password does not match
				return false;
			}
		} catch (Exception e) {
	        e.printStackTrace();
			return false;
		}
    }
}