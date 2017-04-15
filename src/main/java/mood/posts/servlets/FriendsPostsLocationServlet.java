package mood.posts.servlets;
 
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;

public class FriendsPostsLocationServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public FriendsPostsLocationServlet() {super();}
	
	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(
	//			Posts.friendsPostsLocation(params.get("skey")));
		return new JSONObject();
	}
}
