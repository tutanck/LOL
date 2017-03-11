package fr.aj.mood.users;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.tools.DBToolBox;
import services.business.User;
import servlets.tools.ServletToolBox;
import servlets.tools.templates.online.OnlinePostServlet;

/**
 * * @author Anagbla Jean */
public class SignoutServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public SignoutServlet() {super();}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			Map<String, String>params=ServletToolBox.beforeOnlineBusiness
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
		JSONObject res=User.logout(params);

		//Here we are sure that the session exists and contains a valid session key
		HttpSession session = request.getSession(false); 
		if((int)res.get("status")==0){
			session.setAttribute("skey",null);
			session.invalidate();//close HTTPsession
		}
		response.sendRedirect("signin.jsp");
	}
}