package fr.aj.jeez.servlet.interfaces;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * * @author Anagbla Joan */
public interface IJEEZServlet {
	
	public JSONObject doBusiness(HttpServletRequest request,
                                 HttpServletResponse response, Map<String, String> params)
					throws Exception ;
}