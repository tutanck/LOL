package mood.friends;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;


public class FriendListServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public FriendListServlet() {super();}
	 
	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	//	response.getWriter().print(Friends.friendList(params));
		return new JSONObject();
	}

}
