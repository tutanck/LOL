package fr.aj.mood.friends;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Friends;
import servlets.tools.templates.online.OnlineGetServlet;

public class FriendListServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public FriendListServlet() {super();}
	 
	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(Friends.friendList(params));}

}
