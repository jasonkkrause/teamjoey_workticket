package workticket;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.UserDTO;

/**
 * Servlet implementation class LoginController
 */
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = this.getServletContext();
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
	    String command = request.getParameter("command");
	    System.out.println("Controller GET command:"+command);
	    
	    UserDTO userTicket = UserTicket.getUserTicket(session);
	    String redirect = "";
		Boolean notFound = false;
	    dispatcher = ctx.getRequestDispatcher("/login.jsp"); // default view
	    
	    // TODO
	    Boolean isManager = userTicket != null && userTicket.getRole().equals("Manager");
	    Boolean loggedIn = userTicket != null;
	    
		if (command == null) { // TODO
	    	if (loggedIn) { // TODO
	    		redirect = "/ticket";
	    	}
	    }
	    else if (command.equals("logout")) { // TODO
	    	UserTicket.destoryUserTicket(session);
	    	redirect = "/ticket";
	    }
	    else {
	    	notFound = true;
	    }
	    
		System.out.println("Redirect:"+redirect);
		System.out.println("NotFound:"+notFound);
		
		if (!redirect.isEmpty()) { // Redirect if redirect is set
			response.sendRedirect(redirect);
			return;
		}
		if (notFound) { // displays not found page
			dispatcher = ctx.getRequestDispatcher("/notFound.jsp"); 
		}
		
		// set for all valid requests
    	request.setAttribute("isManager", isManager);
    	request.setAttribute("loggedIn", loggedIn);
		
	    dispatcher.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = this.getServletContext();
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		String command = request.getParameter("command");
		System.out.println("Controller POST command:"+command);
		
		UserDTO userTicket = UserTicket.getUserTicket(session);
		String redirect = null;
		Boolean notFound = false;
		dispatcher = ctx.getRequestDispatcher("/login.jsp"); // default view
		
		// TODO
		Boolean isManager = userTicket != null && userTicket.getRole().equals("Manager");
	    Boolean loggedIn = userTicket != null;
		
		
		if (command == null) { // TODO
			if (loggedIn) { // TODO
				redirect = "/ticket";
	    	}
			else {
				notFound = true;
			}
		}
		else if (command.equals("user_login")) {
			if (loggedIn) { // No need to log in if logged in.
				redirect = "/ticket";
	    	}
			else { // Logs the user in
				String attrUsername = request.getParameter("username");
				String attrPassword = request.getParameter("password");
				
				// Validates password
				if (UserTicket.validPassword(attrUsername, attrPassword)) { 
					UserTicket.setUserTicket(session, attrUsername);
					redirect = "/ticket";
				}
				else { // Login Failed
					dispatcher = ctx.getRequestDispatcher("/loginFailed.jsp");
				}
			}
		}
		else { // Command not found
			notFound = true;
	    }
		
		System.out.println("Redirect:"+redirect);
		System.out.println("NotFound:"+notFound);
		
		if (!redirect.isEmpty()) { // Redirect if redirect is set
			response.sendRedirect(redirect);
			return;
		}
		if (notFound) { // displays not found page
			dispatcher = ctx.getRequestDispatcher("/notFound.jsp"); 
		}
		
		// set for all valid requests
    	request.setAttribute("isManager", isManager);
    	request.setAttribute("loggedIn", loggedIn);
		
		dispatcher.forward(request,response);
	}

}
