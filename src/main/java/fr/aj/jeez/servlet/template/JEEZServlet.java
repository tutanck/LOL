package fr.aj.jeez.servlet.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;

import fr.aj.jeez.servlet.interfaces.IJEEZServlet;
import fr.aj.jeez.servlet.tools.MapRefiner;

/**
 * * @author Anagbla Joan */
public abstract class JEEZServlet  
extends HttpServlet 
implements IJEEZServlet{
	private static final long serialVersionUID = 1L;

	/**
	 * The set of incoming parameters names required 
	 * for the underlying service to work properly */
	protected Set<String> epnIn=new HashSet<String>(); //Incoming expected parameters names

	/**
	 * The set of outgoing parameters names required 
	 * for the client to work properly */
	protected Set<String> epnOut=new HashSet<String>(); //Outgoing expected parameters names

	/**
	 * The set of incoming additional parameters names  
	 *  taken into account by the underlying service*/
	protected Set<String> opnIn=new HashSet<String>(); //Incoming optional parameters names

	/**
	 * The set of outgoing additional parameters names  
	 *  taken into account by the underlying service*/
	protected Set<String> opnOut=new HashSet<String>(); //Outgoing optional parameters names


	protected Map<String, String> beforeBusiness(
			HttpServletRequest request,
			HttpServletResponse response
			)throws IOException, JSONException {

		response.setContentType("text/plain");

		Map<String,String>incommingParams=MapRefiner.refine(request.getParameterMap());

		Map<String,String>supportedParams= new HashMap<>();	

		for(String expected : epnIn){
			if(!paramIsFilled(incommingParams,expected)){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL MISUSED");
				return null;
			}
			//Copy the supported parameters into a restricted map (contains only epn and opn)
			supportedParams.put(expected, incommingParams.get(expected));
		}

		for(String optional : opnIn)
			if(paramIsFilled(incommingParams,optional))
				supportedParams.put(optional, incommingParams.get(optional));

		return supportedParams;
	}

	
	protected boolean requireToBeConnected(
			HttpServletRequest request,
			HttpServletResponse response,
			boolean require
			)throws IOException {

		boolean succeeded = true;

		HttpSession session = request.getSession(false);

		if(require){
			if(session==null){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "USER UNAUTHENTICATED");
				succeeded=false;
			}
			 /*TODO remove this comment : possible to be overwrited by the user 
			 * super.requireToBeConnected()
			 * if (session.getAttribute("ssid_token")==null){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "USER UNAUTHENTICATED");
				return false;}
				##trouver un moyen pour les pb cause par le send de l'http error si cette methode est redefinie
				par l'user idem pour beforeBusiness  : au pire les passer en final */
		}else
			if(session!=null)
				succeeded=false; //Should not have been connected once
		
		return succeeded;
	}
	
	
	//TODO check if it is necessary to check for null or undefined or other
	private boolean paramIsFilled(Map<String,String>params, String param){
		if(params.containsKey(param))
			return !(params.get(param).equals(""));
		return false;
	}
	
	
}