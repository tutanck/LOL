package mood.posts.servlets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;

public class GetPostServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public GetPostServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"id"}));}

	
	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	//	response.getWriter().print(Posts.getPost(
	//			params.get("skey"),request.getParameter("id")));
		return new JSONObject();
	}
}
