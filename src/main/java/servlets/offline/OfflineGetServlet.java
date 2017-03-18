package servlets.offline;

import javax.servlet.http.HttpServletRequest;

import mood.users.db.UserSessionDB;

/**
 * * @author Anagbla Joan */
public abstract class OfflineGetServlet extends fr.aj.jeez.servlet.offline.OfflineGetServlet{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isConnected(
			HttpServletRequest request
			){
		
		UserSessionDB.uid(token, did);
		
		return false;		
	}
	 
}