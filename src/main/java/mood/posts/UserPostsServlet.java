package mood.posts;

import fr.aj.jeez.servlet.basic.GetServlet;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserPostsServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public UserPostsServlet() {super();}
	
	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	// 	response.getWriter().print(Posts.userPosts(params.get("skey")));
	}
	
}
