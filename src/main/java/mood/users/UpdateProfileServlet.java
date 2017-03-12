package mood.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;

public class UpdateProfileServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public UpdateProfileServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		//All but username (can't be changed)
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"username"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(User.updateProfile(params));
		}
}