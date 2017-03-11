package fr.aj.mood.groups;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Groups;
import servlets.tools.templates.online.OnlineGetServlet;

public class UserMembershipServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public UserMembershipServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"uid"}));}


	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, 
			Map<String, String> params)throws Exception {
		response.getWriter().print(Groups.userMembership(
				request.getParameter("uid")));}

}
