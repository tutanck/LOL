package fr.aj.mood.users.pp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.UserPlacesProfile;
import servlets.tools.templates.online.OnlinePostServlet;


public class GetPpServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public GetPpServlet() {super();}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(UserPlacesProfile.getPp(
				params.get("skey")));}		
}