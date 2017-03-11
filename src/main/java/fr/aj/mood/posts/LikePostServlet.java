package fr.aj.mood.posts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Posts;
import servlets.tools.templates.online.OnlinePostServlet;

public class LikePostServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	 public LikePostServlet() {super();}

		@Override
		public void init() throws ServletException {
			super.init();
			super.epn=new HashSet<>(Arrays.asList(new String[]{"id","stars"}));}

		@Override
		public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
				throws Exception {
			response.getWriter().print(Posts.likePost(
					params.get("skey"),
					request.getParameter("id"),
					Integer.parseInt(request.getParameter("stars"))));}
}
