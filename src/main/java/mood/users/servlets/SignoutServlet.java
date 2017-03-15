package mood.users.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import mood.users.services.User;

import org.json.JSONObject;


/**
 * * @author Anagbla Joan */

@WebServlet(name = "SignoutServlet" ,urlPatterns={"/signout"})
public class SignoutServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public JSONObject doBusiness(
			HttpServletRequest request,
			HttpServletResponse response,
			JSONObject params
			)throws Exception {
		return User.logout(params);
//		response.sendRedirect("signin.jsp");
	}
}