package mood.users.services;


import org.json.JSONException;
import org.json.JSONObject;

import db.mongo.UserPlacesProfileDB;
import db.tools.DbException;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;

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
	public static JSONObject updatePp(String skey,String places) 
			throws DbException, JSONException{		
		UserPlacesProfileDB.updatePp(SessionManager.sessionOwner(skey),places);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,null,null,ServiceCaller.whichServletIsAsking().hashCode());} 
	

	/**
	 * @param skey
	 * @param remoteuser
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getPp(String skey) 
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