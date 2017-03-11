package fr.aj.mood.messenger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Messenger;
import servlets.tools.templates.online.OnlinePostServlet;

public class NewPrivateMessageServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public NewPrivateMessageServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"uther","msg"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		System.out.println("prmskey="+params.get("skey"));
		response.getWriter().print(Messenger.newPrivateMessage(
				params.get("skey"),
				request.getParameter("uther"),
				request.getParameter("msg")));}

}
