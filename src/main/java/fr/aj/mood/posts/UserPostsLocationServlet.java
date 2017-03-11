package fr.aj.mood.posts;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Posts;
import servlets.tools.templates.online.OnlineGetServlet;

public class UserPostsLocationServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public UserPostsLocationServlet() {super();}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
			response.getWriter().print(Posts.userPostsLocation(params.get("skey")));}		
}

