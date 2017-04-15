package mood.comments.servlets;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;


public class ComnentsHeadInfosServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

    public ComnentsHeadInfosServlet() {super();}
    
    public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		//response.getWriter().print(Comments.comnentsHeadInfos());
		return new JSONObject();
    }
}
