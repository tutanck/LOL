package mood.comments;

import fr.aj.jeez.servlet.basic.PostServlet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ModifyCommentServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

    public ModifyCommentServlet() {super();}
    
    @Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"id","updt"}));}


	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	//response.getWriter().print(Comments.modifyComment(
//				params.get("skey"),
//				request.getParameter("id"),
//				request.getParameter("updt")));
    }
}
