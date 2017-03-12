package mood.users;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;


/**
 * * @author Anagbla Jean */
public class SignoutServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//JSONObject res=User.logout(params);

//		response.sendRedirect("signin.jsp");
	}
}