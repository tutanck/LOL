package mood.users.servlets.welcome;
 
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;

/**
 * * @author Anagbla Jean */
public class AccessRecoveryServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public AccessRecoveryServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"email"}));}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> params) throws Exception{
		//response.getWriter().print(User.accessRecover(request.getParameter("email")));
		return new JSONObject();

	}
}
