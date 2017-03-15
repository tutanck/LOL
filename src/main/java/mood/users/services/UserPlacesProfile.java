package mood.users.services;

import org.json.JSONException;
import org.json.JSONObject;

import tools.db.DbException;


/**
 * @author AJoan
 ***@goodToKnow ! FLUENT STYLE CODE */
public class UserPlacesProfile {

	/**
	 * Update places profile of a user
	 * can be call by both servlets (external call : rpcode!=0)
	 * or services(internal call rpcode=0 (not a nearby servlet call)) 
	 * @param skey
	 * @param places
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject updatePp(JSONObject params) 
			throws DbException, JSONException{		
		UserPlacesProfileDB.updatePp(SessionManager.sessionOwner(skey),places);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,null,null,ServiceCaller.whichServletIsAsking().hashCode());} 
	

	/**
	 * @param params
	 * @param remoteuser
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getPp(JSONObject params) 
			throws DbException, JSONException{			
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject()
				.put("places",UserPlacesProfileDB.getPp(
						SessionManager.sessionOwner("skey")))
		,null,ServiceCaller.whichServletIsAsking().hashCode());}	

	
	
	public static void main(String[] args) throws  DbException, JSONException {
		updatePp("nico", "paris mexico japan");
		updatePp("jo92", "paris");
		updatePp("jo92", "ctn haie vive");
		System.out.println(getPp(""));
	}	
}