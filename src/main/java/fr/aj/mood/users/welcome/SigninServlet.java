package fr.aj.mood.users.welcome;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import fr.aj.jeez.servlet.basic.GetServlet;


/**
 * * @author Anagbla Joan */
@WebServlet(name="simpleservlet", urlPatterns={"/myservlet"})  

public class SigninServlet extends GetServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{
				"username","pass"
				}));}
	
	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> params) throws Exception{
		
		
		//JSONObject res=User.login(params);
		
		response.getWriter().print("res");
	return new JSONObject();	
	} 
}