package fr.aj.mood.users.welcome;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.tools.DBToolBox;
import services.business.User;
import servlets.tools.ServletToolBox;
import servlets.tools.templates.offline.OfflinePostServlet;

/**
 * * @author Anagbla Jean */
public class ConfirmAccountServlet extends OfflinePostServlet {
	private static final long serialVersionUID = 1L;
	public ConfirmAccountServlet() {super();}
	
	@Override
	public void init() throws ServletException {
		super.init();
		super.epn= new HashSet<>(Arrays.asList(new String[]{"ckey"}));}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			Map<String, String>params=ServletToolBox.beforeOfflineBusiness
					(request, response, epn);
			if(params==null) 	return;

			doBusiness(request,response,params);

		} catch (Exception e){
			e.printStackTrace();
			//ServicesToolBox.logStackTrace(e);
			response.sendRedirect("err.jsp?e="+DBToolBox.getStackTrace(e));
		}}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		User.confirmUser(request.getParameter("ckey"));
		response.sendRedirect("signin.jsp");}
}