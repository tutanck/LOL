package mood.posts;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UserPostsLocationServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public UserPostsLocationServlet() {super();}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	//		response.getWriter().print(Posts.userPostsLocation(params.get("skey")));
		return new JSONObject();
	}
}

