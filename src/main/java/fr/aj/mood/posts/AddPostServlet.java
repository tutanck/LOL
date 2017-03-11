package fr.aj.mood.posts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Posts;
import servlets.tools.templates.online.OnlinePostServlet;

public class AddPostServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public AddPostServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"desc","lon","lat"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(Posts.addPost(
				params.get("skey"),
				request.getParameter("desc"),
				Double.parseDouble(request.getParameter("lon")),
				Double.parseDouble(request.getParameter("lat"))));}

}
