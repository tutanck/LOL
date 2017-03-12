package mood.comments.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.mongo.CommentsDB;
import db.sqldb.business.UserDB;
import db.tools.DbException;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;

/**
 * @author Anagbla Jean 
 * **@goodToKnow ! FLUENT STYLE CODE*/
public class Comments {

	private static int maxInOne = 10;

	/**
	 * Add a comment to an existing post
	 * @param skey
	 * @param comment
	 * @param postID
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject commentPost(String skey,String comment,String postID)
			throws DbException, JSONException{
		CommentsDB.newComment(SessionManager.sessionOwner(skey),comment,postID);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Return the comments on a post
	 * @param postID
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject postComments(String postID) 
			throws DbException, JSONException{
		JSONArray jar=new JSONArray();
		DBCursor cursor = CommentsDB.postComments(postID);
		cursor.sort( new BasicDBObject("date",-1));
		cursor.limit(maxInOne);	
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			String auth = (String) dbo.get("authid");
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("pid",dbo.get("pid"))
					.put("com",dbo.get("com"))
					.put("type","com")
					.put("date",dbo.get("date"))
					.put("authid",auth)
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,new JSONObject().put("coms",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Delete a comment of a post
	 * @param skey
	 * @param commentID
	 * @return
	 * @throws JSONException
	 * @throws DbException */
	public static JSONObject deleteComment(String skey, String commentID) throws JSONException, DbException{
		String uid = SessionManager.sessionOwner(skey);
		String owner = CommentsDB.commentAuthor(commentID);
		if(!uid.equals(owner))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD
					, null, "You dont have the right to modify this comment"
					, ServiceCodes.ACTION_DENIED);
		CommentsDB.cleanComment(commentID);		
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Modify a comment 
	 * @param skey
	 * @param commentID
	 * @param update
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject modifyComment(String skey,String commentID,String update) throws  DbException, JSONException{
		String uid = SessionManager.sessionOwner(skey);
		String owner = CommentsDB.commentAuthor(commentID);
		if(!uid.equals(owner))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD
					, null, "You dont have the right to modify this comment"
					, ServiceCodes.ACTION_DENIED);
		CommentsDB.modify(commentID, update);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }		 


	/**
	 * ADMINISTRATOR FUNCTIONALITY
	 * Return all comments in database
	 * @return
	 * @throws DbException 
	 * @throws JSONException */
	public static JSONObject comnentsHeadInfos() throws DbException, JSONException{ 
		JSONArray jar=new JSONArray();
		DBCursor cursor = CommentsDB.comnentsHeadInfos();
		cursor.sort( new BasicDBObject("date",-1)); // newer comments first
		cursor.limit(maxInOne);
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			String auth=(String)dbo.get("authid");			
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("pid",dbo.get("pid"))
					.put("com",dbo.get("com"))
					.put("date",dbo.get("date"))
					.put("authid",auth)
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,new JSONObject().put("coms",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); } 



	public static void main(String[] args) throws DbException, JSONException {
		//newComment("jo47", "trop cool", "77jtyf8");
		//newComment("lola75", "trop naze", "77jtyf8");
		//String cid="580b12e074a4d6f534d1e11b";
		//System.out.println("com auth : "+commentAuthor(cid));
		//System.out.println("com : "+getComment(cid));
		//modify(cid,"trop bof");
		//deleteComment(cid);
		//deletePostComments("77jtyf8");
		System.out.println(postComments("77jtyf8"));
		System.out.println(comnentsHeadInfos());
	}
}