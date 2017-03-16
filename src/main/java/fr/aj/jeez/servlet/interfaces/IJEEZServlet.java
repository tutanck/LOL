package fr.aj.jeez.servlet.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * * @author Anagbla Joan */
public interface IJEEZServlet {

	public JSONObject doBusiness(
			HttpServletRequest request,
			HttpServletResponse response, 
			JSONObject params
			)throws Exception ;

	public boolean isDisconnected(
			HttpServletRequest request
			);

	public boolean isConnected(
			HttpServletRequest request
			);

}