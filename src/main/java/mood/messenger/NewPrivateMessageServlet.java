package mood.messenger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;

public class NewPrivateMessageServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"uther","msg"}));}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		System.out.println("prmskey="+params.get("skey"));
		/*response.getWriter().print(Messenger.newPrivateMessage(
				params.get("skey"),
				request.getParameter("uther"),
				request.getParameter("msg")));*/
		return new JSONObject();
				}

}
