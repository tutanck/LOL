package mood.posts;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;


public class GetAllPostsLocationServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public GetAllPostsLocationServlet() {super();}
	
	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	//	response.getWriter().print(Posts.getAllPostsLocation());
	}
}
