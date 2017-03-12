package mood.searchposts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.SearchPosts;
import servlets.tools.templates.online.OnlineGetServlet;

public class QMatchingFriendsPostsLocationServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public QMatchingFriendsPostsLocationServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"q"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(SearchPosts.QMatchingFriendsPostsLocation(
				 params.get("skey"),request.getParameter("q")));}

}
