package mood.searchposts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;
import org.json.JSONObject;

public class QMatchingAllPostsLocationServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public QMatchingAllPostsLocationServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"q"}));}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(SearchPosts.QMatchingAllPostsLocation(
	//			request.getParameter("q"),params.get("skey")));
		return new JSONObject();
	}

}
