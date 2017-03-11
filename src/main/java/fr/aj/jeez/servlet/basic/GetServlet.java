package fr.aj.jeez.servlet.basic;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.template.JEEZServlet;

/**
 * * @author Anagbla Joan */
public abstract class GetServlet extends JEEZServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
		try{
			Map<String, String>params = beforeBusiness(request,response);
			if(params!=null) 	
				doBusiness(request,response,params);
			//else nothing to do : all is already done in beforeBusiness

		} catch (Exception e){
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "AN INTERNAL SERVER ERROR OCCURRED");
		}
	}

	@Override
	protected void doPost(
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
		doGet(request, response);
	}

}