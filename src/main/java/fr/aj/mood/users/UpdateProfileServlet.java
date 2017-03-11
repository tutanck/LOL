package fr.aj.mood.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.User;
import servlets.tools.templates.online.OnlinePostServlet;


public class UpdateProfileServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public UpdateProfileServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		//All but username (can't be changed)
		super.epn=new HashSet<>(Arrays.asList(new String[]{"username"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(User.updateProfile(params));}		
}