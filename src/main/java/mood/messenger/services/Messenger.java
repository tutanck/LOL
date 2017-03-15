package mood.messenger.services;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import db.mongo.MessengerDB;
import db.sqldb.business.UserDB;
import db.tools.DbException;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;

/**
 * @author AJoan
 ***@goodToKnow ! FLUENT STYLE CODE */
public class Messenger {

	private static int maxInOne=15;

	/**
	 * Add a new message to private conversation
	 * between logged user and an remote user
	 * @param skey
	 * @param remoteuser
	 * @param message
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject newPrivateMessage(String skey,String remoteuser,String message) 
			throws DBException, JSONException{		
		MessengerDB.newMessage(SessionManager.sessionOwner(skey)
				,remoteuser,message);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,null,null,ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * Return private conversation between logged user and an remote user
	 * @param skey
	 * @param remoteuser
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject conversation(String skey,String remoteuser) 
			throws DBException, JSONException{
		JSONArray jar=new JSONArray();
		DBCursor cursor =MessengerDB.messages(
				SessionManager.sessionOwner(skey),remoteuser);
		cursor.sort(new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne);
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("type","message")
					.put("sender",dbo.get("sender"))
					.put("sendername",
							UserDB.getUsernameById((String)dbo.get("sender")))
					.put("recipient",dbo.get("recipient"))
					.put("recipientname",
							UserDB.getUsernameById((String)dbo.get("recipient")))
					.put("message",dbo.get("message"))
					.put("date",dbo.get("date")));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject().put("messages", jar)
				,null,ServiceCaller.whichServletIsAsking().hashCode());}



	public static JSONObject interlocutors(String skey) 
			throws DBException, JSONException{
		JSONArray jar=new JSONArray();
		Set<String>unicqids=new HashSet<>();
		String uid=SessionManager.sessionOwner(skey);
		DBCursor cursor =MessengerDB.messages(uid);
		cursor.sort(new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne);
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			unicqids.add(dbo.get("sender").equals(uid)==true?
					(String)dbo.get("recipient")
					:
						(String)dbo.get("sender"));}
		for(String unicqid : unicqids)
			jar.put(new JSONObject()
					//.put("id",dbo.get("_id")) //useless for now for our configuration
					.put("type","speaker")
					.put("username",UserDB.getUsernameById(unicqid))
					.put("id",unicqid));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject().put("speakers",jar),null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	public static void main(String[] args) throws MongoException, DBException, JSONException {
		System.out.println(interlocutors("a981a551a770b86e02f3a5a0f49ee2bd"));}

}