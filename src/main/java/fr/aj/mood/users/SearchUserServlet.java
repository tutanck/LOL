package fr.aj.mood.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.User;
import servlets.tools.templates.online.OnlineGetServlet;

/**
 * * @author Anagbla Jean */
public class SearchUserServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public SearchUserServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"query"}));}
		
	@Override
	public void doBusiness(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(User.searchUser(params.get("skey"),
				request.getParameter("query")));}

	

}