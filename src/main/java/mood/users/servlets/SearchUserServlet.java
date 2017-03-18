package mood.users.servlets;

import fr.aj.jeez.servlet.online.OnlineGetServlet;
import mood.users.services.User;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * * @author Anagbla Joan */

@WebServlet(name = "SearchUserServlet" ,urlPatterns={"/user/search"})
public class SearchUserServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"query"}));}

	@Override
	public JSONObject doBusiness(
			HttpServletRequest request,
			HttpServletResponse response,
			JSONObject params
			)throws Exception {
		return User.searchUser(params);
	}
}