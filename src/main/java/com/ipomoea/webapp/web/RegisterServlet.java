package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ipomoea.webapp.controller.UserCont;
import com.ipomoea.webapp.exception.CustomException;
import com.ipomoea.webapp.model.User;

/**
 * @email Ramesh Fadatare
 */

@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 2;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	
		System.out.println("Register User");

    	String username = request.getParameter("username");
		User user = null;
		try {
			user = UserCont.getInstance().fetchUserByUsername(username);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		
		if(user == null) {//If username is not taken
	        boolean register = register(request, response);
	        
	        if(register) {//succesfull registration
	    		System.out.println("successfull registration");
	    		response.setHeader("Location", "login");
	    		response.setStatus(302);
	        }
	        else {//unsuccessfull registration
	        	request.setAttribute("registererror", "<p style=\"color:red\">An unexpected error occoured. Please contact the system admin.<p>");
	    		System.out.println("failed registration");
				//response.setHeader("Location", "renderreg?error=0");
	    		//response.setStatus(302);
				request.getRequestDispatcher("register.jsp").forward(request, response);
	        }
		}
		else {//If username is taken
    		System.out.println("username already taken");
    		request.setAttribute("registererror", "<p style=\"color:red\">The choosen username has already been taken!<p>");
			//response.setHeader("Location", "renderreg?error=0");
    		//response.setStatus(302);
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
    }

    private boolean register(HttpServletRequest request, HttpServletResponse response){
    	try {
        	String username = request.getParameter("username");
        	String password = request.getParameter("password");
        	String firstname = request.getParameter("firstname");
        	String lastname = request.getParameter("lastname");
        	
    		User user = new User(username);
    		user.setPassword(password);
    		user.setFirstName(firstname);
    		user.setLastName(lastname);

			UserCont.getInstance().createUser(user);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}