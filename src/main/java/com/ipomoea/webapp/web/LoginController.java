package com.ipomoea.webapp.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ipomoea.webapp.controller.UserCont;
import com.ipomoea.webapp.model.User;

/**
 * @email Ramesh Fadatare
 */

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            authenticate(request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        

    	try {
			User user = UserCont.getInstance().fetchUserByUsername(username);
			if(user.getPassword().equals(password)) {
	            RequestDispatcher dispatcher = request.getRequestDispatcher("login-success.jsp");
	            dispatcher.forward(request, response);
			}
			else {
		        /*
		         * To Do: show some kind of error " Login details (pw/user) did not match information stored in db
		         */
		        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		        dispatcher.forward(request, response);
			}
		} catch (Exception e) {
            e.printStackTrace();
            /*
             * To Do: show some kind of error " Login details (pw/user) did not match information stored in db
             */
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
		}
    }
}