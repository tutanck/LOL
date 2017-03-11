package fr.aj.mood.messenger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Messenger;
import servlets.tools.templates.online.OnlineGetServlet;

public class ConversationServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public ConversationServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"uther"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(Messenger.conversation(
				params.get("skey"),
				request.getParameter("uther")));}

}
