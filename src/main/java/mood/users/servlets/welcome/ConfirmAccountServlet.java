package mood.users.servlets.welcome;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;

/**
 * * @author Anagbla Jean */
public class ConfirmAccountServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public ConfirmAccountServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn= new HashSet<>(Arrays.asList(new String[]{"ckey"}));}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//User.confirmUser(request.getParameter("ckey"));
		//response.sendRedirect("signin.jsp");
		return new JSONObject();
		}
}