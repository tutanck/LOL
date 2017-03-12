package mood.users;

import fr.aj.jeez.servlet.basic.GetServlet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * * @author Anagbla Jean */
public class SearchUserServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"query"}));}
		
	@Override
	public void doBusiness(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(User.searchUser(params.get("skey"),
	//			request.getParameter("query")));
	}

	

}