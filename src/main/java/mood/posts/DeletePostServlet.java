package mood.posts;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletePostServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public DeletePostServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"id"}));}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(Posts.deletePost(
		//		params.get("skey"),
				//request.getParameter("id")));
		return new JSONObject();
	}
}
