package fr.aj.mood.friends;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Friends;

import servlets.tools.templates.online.OnlinePostServlet;

public class AddFriendServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public AddFriendServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"fid"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(Friends.addFriend(params));}

}
