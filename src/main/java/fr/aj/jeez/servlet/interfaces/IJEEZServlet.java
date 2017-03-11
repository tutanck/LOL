package fr.aj.jeez.servlet.interfaces;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * * @author Anagbla Joan */
public interface IJEEZServlet {
	
	public void doBusiness(HttpServletRequest request, 
			HttpServletResponse response,Map<String, String>params)
					throws Exception ;
}