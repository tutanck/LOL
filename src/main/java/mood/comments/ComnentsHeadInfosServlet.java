package mood.comments;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Comments;
import servlets.tools.templates.online.OnlinePostServlet;

public class ComnentsHeadInfosServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;

    public ComnentsHeadInfosServlet() {super();}
    
    @Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(Comments.comnentsHeadInfos());}
}
