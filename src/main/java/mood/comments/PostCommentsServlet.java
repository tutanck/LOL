package mood.comments;

import fr.aj.jeez.servlet.basic.PostServlet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PostCommentsServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

    public PostCommentsServlet() {super();}
    
    @Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"pid"}));}


	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(Comments.postComments(
	//			request.getParameter("pid")));
    }
}
