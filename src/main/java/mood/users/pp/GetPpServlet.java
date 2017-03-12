package mood.users.pp;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetPpServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public GetPpServlet() {super();}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(UserPlacesProfile.getPp(
		//		params.get("skey")));
		return new JSONObject();
		}
}