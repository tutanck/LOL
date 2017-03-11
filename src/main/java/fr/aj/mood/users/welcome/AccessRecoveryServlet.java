package fr.aj.mood.users.welcome;
 
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.User;
import servlets.tools.templates.offline.OfflineGetServlet;

/**
 * * @author Anagbla Jean */
public class AccessRecoveryServlet extends OfflineGetServlet {
	private static final long serialVersionUID = 1L;
	public AccessRecoveryServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"email"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> params) throws Exception{
		response.getWriter().print(User.accessRecover(request.getParameter("email")));}
}
