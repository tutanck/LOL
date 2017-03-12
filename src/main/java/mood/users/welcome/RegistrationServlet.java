package mood.users.welcome;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;

/**
 * * @author Anagbla Jean */
public class RegistrationServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public RegistrationServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn= new HashSet<>(Arrays.asList(new String[]{"username","pass","email"}));}

	@Override
	public void doBusiness(HttpServletRequest request, 
			HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(User.registration(params));}
}