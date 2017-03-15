package fr.aj.jeez.servlet.basic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import fr.aj.jeez.servlet.template.JEEZServlet;

/**
 * * @author Anagbla Joan */
public abstract class PostServlet extends JEEZServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
		try{
			JSONObject params = beforeBusiness(request,response);
			if(params!=null) 	
				afterBusiness(
						request,response,
						doBusiness(request,response,params),
						true
						);
		}catch (Exception e){
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "AN INTERNAL SERVER ERROR OCCURRED");
		}
	}

	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
		doPost(request, response);
	}

}