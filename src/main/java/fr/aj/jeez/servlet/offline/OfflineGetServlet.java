package fr.aj.jeez.servlet.offline;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.GetServlet;

/**
 * * @author Anagbla Joan */
public abstract class OfflineGetServlet extends GetServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			if(requireToBeConnected(request, response, false))
				super.doGet(request, response);

		} catch (Exception e){
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "AN INTERNAL SERVER ERROR OCCURRED");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);}

}