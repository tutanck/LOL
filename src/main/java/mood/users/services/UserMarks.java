package mood.users.services;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.sqldb.business.MarkDB;
import db.tools.DbException;
import services.tools.ServiceCodes;
import services.tools.ServiceCaller;
import services.tools.ServicesToolBox;

/**
 * @author AJoan
 ***@goodToKnow ! FLUENT STYLE CODE */
public class UserMarks {

	/**
	 * @description add a mark given by the current user to an user 
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject addMark(Map<String, String> url_parameters) 
			throws DbException, JSONException{ 
		MarkDB.addMark(url_parameters.get("uther"), url_parameters.get("skey"), 
				Double.parseDouble(url_parameters.get("mark"))); 
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,null,null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description add the mark given by the current user to another user
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject updateMark(Map<String, String> url_parameters)
			throws DbException, JSONException{ 
 		MarkDB.updateMark(url_parameters.get("uther"), url_parameters.get("skey"), 
				Double.parseDouble(url_parameters.get("mark")));  	
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,null,null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description verify if total user's mark is greater than a criterion
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject userMarkIsGreaterThan(Map<String, String> url_parameters)
			throws DbException, JSONException {
 		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject().put("response",MarkDB.userMarkIsGreaterThan(
						url_parameters.get("uther"), 
						Double.parseDouble(url_parameters.get("criterion")))),null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description verify if total user's mark is lower than a criterion
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject userMarkIsLowerThan(Map<String, String> url_parameters) 
			throws DbException, JSONException {
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject().put("response",MarkDB.userMarkIsLowerThan(
						url_parameters.get("uther"), 
						Double.parseDouble(url_parameters.get("criterion")))),null,
				ServiceCaller.whichServletIsAsking().hashCode());} 


	/**
	 * @description return the total user's mark  
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getMarkByUsername(Map<String, String> url_parameters) 
			throws DbException, JSONException {
		return ServicesToolBox.reply(
				ServiceCodes.STATUS_KANPEKI, new JSONObject().put("response",
						MarkDB.getMarkByUID(url_parameters.get("uther"))),null,
				ServiceCaller.whichServletIsAsking().hashCode());} 


	/**
	 * @description return the total user's mark by his user ID
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getMarkByUID(Map<String, String> url_parameters) throws DbException, JSONException {
		return ServicesToolBox.reply(
				ServiceCodes.STATUS_KANPEKI, new JSONObject().put("response",
						MarkDB.getMarkByUID(url_parameters.get("uid"))),
				null,ServiceCaller.whichServletIsAsking().hashCode());} 


	/**
	 * @description return a list of  user ID whose mark is greater than criterion
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getUIDListWhereMarkGreaterThan(Map<String, String> url_parameters) 
			throws DbException, JSONException {
		JSONArray jar=new JSONArray();
		for(String uid : MarkDB.getUIDListWhereMarkGreaterThan(
				Double.parseDouble(url_parameters.get("criterion"))))
			jar.put(new JSONObject().put("uid", uid));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,jar,null,
				ServiceCaller.whichServletIsAsking().hashCode());}

	
	/**
	 * @description return a list of  user ID whose mark is lower than criterion
	 * @param map
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getUIDListWhereMarkLowerThan(Map<String, String> url_parameters)
			throws DbException, JSONException {
		JSONArray jar=new JSONArray();
		for(String uid : MarkDB.getUIDListWhereMarkLowerThan(
				Double.parseDouble(url_parameters.get("criterion"))))
			jar.put(new JSONObject().put("uid", uid));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,jar,null,
				ServiceCaller.whichServletIsAsking().hashCode());}
	
}