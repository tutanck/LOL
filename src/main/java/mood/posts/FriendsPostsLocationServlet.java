package mood.posts;
 
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;

public class FriendsPostsLocationServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public FriendsPostsLocationServlet() {super();}
	
	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(
	//			Posts.friendsPostsLocation(params.get("skey")));
	}
}
