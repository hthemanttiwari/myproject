package devops.myproject.controllerWithoutDb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import devops.myproject.model.User;
import devops.myproject.service.UserService;

public class UserRegisterController extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		  String registerPath = "/register.jsp";
		  response.setContentType("text/html");
		  PrintWriter out = response.getWriter();
		  String fname = request.getParameter("firstname");
		  String lname = request.getParameter("lastname");
		  String pass = request.getParameter("pass");
		  String confirmpass = request.getParameter("confirmpass");
		  String email = request.getParameter("email");
		 

		  if (fname.isEmpty() || lname.isEmpty() || pass.isEmpty() || confirmpass.isEmpty() || email.isEmpty()) {
			   RequestDispatcher rd = request.getRequestDispatcher(registerPath);
			   out.println("<font color=red>Please fill all the fields</font>");
			   rd.include(request, response);
		  } 
		  else {
			   PrintWriter pout= response.getWriter();
			   if(pass.equals(confirmpass)) {

			  User portalNewUser=new User(fname,lname,pass,email);
			
			    if(new UserService().doRegistration(portalNewUser)){   
				    pout.write("Successfuly registered...");
				    RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
				    rd.forward(request, response);
			    }
			    else
			    {
			  	  RequestDispatcher rd = request.getRequestDispatcher(registerPath);
			  	  out.println("<font color=red>Registration fail</font>");
			  	  rd.include(request, response);
			    }
			    
			  }
			  
			  else
			  {
				  out.println("<font color=red>Password and Confirm Password doen't matches</font>");
				  RequestDispatcher rd = request.getRequestDispatcher(registerPath);
				  rd.include(request, response);
			  }
		 }
		 }
}
