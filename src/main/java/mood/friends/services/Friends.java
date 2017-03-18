package mood.friends.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.sqldb.business.FriendDB;
import db.sqldb.business.UserDB;
import db.sqldb.regina.CRUD;
import db.sqldb.regina.CSRShuttleBus;
import db.sqldb.regina.THINGS;
import db.tools.DBToolBox;
import db.tools.DbException;
import services.tools.ServiceCodes;
import services.tools.MapRefiner;
import services.tools.ServiceCaller;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;

public class Friends {

	private static String table=FriendDB.table;
	private static String caller=Friends.class.getName();

	/**
	 * @description add friendship between 2 users
	 * @param url_parameters
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject addFriend(Map<String,String> url_parameters) 
			throws DBException, JSONException {
		//Branch the map (dissociate) like separating the yolk from the egg white
		List<Map<String,String>> node = JSONRefiner.branch(url_parameters, new String[]{"skey"});	

		//get uid from skey
		String uid = UserSession.sessionOwner(node.get(1).get("skey"));

		//create useful map containing only useful parameters (uid ,fid & status)
		node.get(0).put("uid",uid);
		Map<String,String> usefulMap = JSONRefiner.subMap(
				node.get(0),new String[]{"fid","uid"});

		if(!THINGS.matchTHINGS(usefulMap, table, caller))//if no relation exists 
			THINGS.addTHINGS(usefulMap, table, caller);//add friendship in database

		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,new JSONObject()
				.put("message","A request was sent to @"
						+UserDB.getUsernameById(usefulMap.get("fid")))
				.put("uid",usefulMap.get("fid"))
				,null,ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description return a JSONArray within a JSONReply 
	 * that contain user's friends identity (uid,username) 
	 * @param map
	 * @return
	 * @throws JSONException 
	 * @throws DBException */
	public static JSONObject friendList(Map<String,String> url_parameters) 
			throws JSONException, DBException {
		ArrayList<String>fidList= new ArrayList<>();
		
		String uid =UserSession.sessionOwner(url_parameters.get("skey"));
		url_parameters.put("uid", uid);
		url_parameters.put("status", "friend");
		CSRShuttleBus dataSet = CRUD.CRUDPull(THINGS.getTHINGS(JSONRefiner.subMap(
				url_parameters,new String[]{"uid","status"}),table));
		ResultSet rs=dataSet.getResultSet();
		try {while(rs.next())
			fidList.add(rs.getString("fid"));}
		catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		dataSet.close();
		
		url_parameters.put("fid", uid);
		dataSet = CRUD.CRUDPull(THINGS.getTHINGS(JSONRefiner.subMap(
				url_parameters,new String[]{"fid","status"}),table));
		rs=dataSet.getResultSet();
		try {while(rs.next())
			fidList.add(rs.getString("uid"));}
		catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		dataSet.close();
		
		url_parameters.put("fid", uid);
		url_parameters.put("status", "waiting");
		dataSet = CRUD.CRUDPull(THINGS.getTHINGS(JSONRefiner.subMap(
				url_parameters,new String[]{"fid","status"}),table));
		rs=dataSet.getResultSet();
		try {while(rs.next())
			fidList.add(rs.getString("uid"));}
		catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		dataSet.close();

		JSONArray FriendsArray=new JSONArray();
		for(String fid : fidList)
			FriendsArray.put(new JSONObject()
					.put("uid",fid)
					.put("type",FriendDB.status(uid,fid))
					.put("firstname",UserDB.getFirstnameById(fid))
					.put("lastname",UserDB.getLastnameById(fid))
					.put("username",UserDB.getUsernameById(fid)));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject().put("friends",FriendsArray), 
				null, ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description reply to an user invitation to become friend
	 * @param map
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject acceptFriend(Map<String,String> url_parameters) 
			throws DBException, JSONException {
		url_parameters.put("fid", 
				UserSession.sessionOwner(url_parameters.get("skey")));

		Map<String,String> wrap=new HashMap<>();
		wrap.put("status","friend");

		THINGS.updateTHINGS(wrap, JSONRefiner.subMap(url_parameters, 
				new String[]{"uid","fid"}),table,caller);

		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject()
				.put("message","You are now friend with @"
						+UserDB.getUsernameById(url_parameters.get("uid")))
				.put("uid",url_parameters.get("uid")),null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description destroy a friendShip by setting the field status to "angry"
	 * @param map
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject deleteFriend(Map<String,String> url_parameters) 
			throws DBException, JSONException {
		//parameters unpacking
		String uid =UserSession.sessionOwner(url_parameters.get("skey"));
		String fid =url_parameters.get("fid");
		
		//parameters repacking
		Map<String,String>shuttle = new HashMap<>();
		shuttle.put("uid", uid);
		shuttle.put("fid", fid);
		shuttle.put("status", "friend");

		//it is unclear who initiated the friendship so we have to check first
		if(THINGS.matchTHINGS(shuttle, table, caller))
			THINGS.removeTHINGS(shuttle, table,caller);
		else{
			shuttle.clear();
			shuttle.put("fid", uid);
			shuttle.put("uid", fid);
			shuttle.put("status", "friend");
			if(THINGS.matchTHINGS(shuttle, table, caller))
				THINGS.removeTHINGS(shuttle, table,caller);
			else throw new DBException("FriendShip not found!");}

		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject()
				.put("message","You are no more friend with @"
						+UserDB.getUsernameById(fid))
				.put("uid",fid)
				,null,ServiceCaller.whichServletIsAsking().hashCode());}
}