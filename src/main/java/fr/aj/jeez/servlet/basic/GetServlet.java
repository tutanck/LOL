package fr.aj.jeez.servlet.basic;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

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
				afterBusiness(
						request,response,
						doBusiness(request,response,params),
						true
						);
		} 
		//TODO Solution temporaire car du code de service peut provoquer la mm except : au moment de faire les annotations deplacer cette exc au niveau des convertions des params pour fit avec la signature du service
		catch (IllegalArgumentException e){ 
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL MISUSED");
		}
		catch (Exception e){
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

	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}