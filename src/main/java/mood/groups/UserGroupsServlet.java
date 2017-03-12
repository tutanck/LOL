package mood.groups;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;


public class UserGroupsServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public UserGroupsServlet() {super();}

	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response,
								 Map<String, String> params)throws Exception {
		//response.getWriter().print(Groups.userGroups(
		//		params.get("skey")));
		return new JSONObject();
	}

}
