package mood.messenger;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;

public class InterlocutorsServlet extends GetServlet {
	private static final long serialVersionUID = 1L;
	public InterlocutorsServlet() {super();}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
	//	System.out.println("InterlocutorsServlet skey="+params.get("skey"));
	//	response.getWriter().print(Messenger.interlocutors(params.get("skey")));
	}

}