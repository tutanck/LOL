package mood.posts;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;


public class FriendsPostsServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public FriendsPostsServlet() {super();}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(Posts.friendsPosts(params.get("skey")));
		return new JSONObject();
	}
 
}
