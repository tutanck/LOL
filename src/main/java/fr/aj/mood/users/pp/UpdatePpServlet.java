package fr.aj.mood.users.pp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.UserPlacesProfile;
import servlets.tools.templates.online.OnlinePostServlet;


public class UpdatePpServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public UpdatePpServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
 		super.epn=new HashSet<>(Arrays.asList(new String[]{"places"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		response.getWriter().print(UserPlacesProfile.updatePp(
				params.get("skey"),
				request.getParameter("places")));}		
}