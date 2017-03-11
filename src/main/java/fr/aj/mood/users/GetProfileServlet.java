package fr.aj.mood.users;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.User;
import servlets.tools.templates.online.OnlineGetServlet;

/**
 * * @author Anagbla Jean */
public class GetProfileServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public GetProfileServlet() {super();}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(User.getProfile(params));}}