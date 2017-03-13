package mood.posts.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import db.mongo.PostsDB;
import db.sqldb.business.FriendDB;
import db.sqldb.business.UserDB;
import db.tools.DbException;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;

/**
 * @author Anagbla Jean
 * **@goodToKnow ! FLUENT STYLE CODE */
public class Posts {

	private static int maxInOne = 50;

	/**
	 * Add a post in database as logged user's post
	 * @param skey
	 * @param desc
	 * @return
	 * @throws JSONException
	 * @throws DbException */
	public static JSONObject addPost(String skey,String desc,double lon,double lat) 
			throws JSONException, DbException{		
		PostsDB.addPost(SessionManager.sessionOwner(skey),desc,lon,lat);	
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Return a light version of all posts location informations
	 * @param skey
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject postsLocation(String skey) throws DbException, JSONException{  
		JSONArray jar=new JSONArray();
		DBCursor cursor = PostsDB.posts();
		cursor.sort( new BasicDBObject("date",-1));//TODO REVERSE ORDER MORE RECENT LAST
		cursor.limit(maxInOne); 
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			String auth=(String)dbo.get("authid");
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("type","post")
					.put("color",color(SessionManager.sessionOwner(skey),auth))
					.put("lat",dbo.get("lat"))
					.put("lon",dbo.get("lon"))
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }	

	/**
	 * Return the list of user's posts location informations   
	 * @param skey
	 * @return
	 * @throws JSONException
	 * @throws DbException */
	public static JSONObject userPostsLocation(String skey) throws JSONException, DbException{
		JSONArray jar=new JSONArray();
		DBCursor cursor = PostsDB.userPosts(SessionManager.sessionOwner(skey));
		cursor.sort( new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne);
		while (cursor.hasNext()) {
			DBObject dbo=cursor.next();
			String auth=(String)dbo.get("authid");
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("type","post")
					.put("color",color(SessionManager.sessionOwner(skey),auth))
					.put("lat",dbo.get("lat"))		 
					.put("lon",dbo.get("lon"))
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Return the list of user's friends posts location informations  
	 * @param skey
	 * @return
	 * @throws DbException 
	 * @throws JSONException */
	public static JSONObject friendsPostsLocation(String skey) throws DbException, JSONException{ 
		JSONArray jar=new JSONArray();
		DBCursor cursor = PostsDB.friendsPosts(
				FriendDB.friendsSet(SessionManager.sessionOwner(skey)));
		cursor.sort( new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne); 
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			String auth=(String) dbo.get("authid");
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("type","post")
					.put("color",color(SessionManager.sessionOwner(skey),auth))
					.put("lat",dbo.get("lat"))
					.put("lon",dbo.get("lon"))
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }
	
	/**
	 * Return the list of public posts according to the user's friends and identity
	 * @param skey
	 * @return
	 * @throws DbException 
	 * @throws JSONException */
	public static JSONObject publicPostsLocation(String skey) throws DbException, JSONException{ 
		JSONArray jar=new JSONArray();
		DBCursor cursor = PostsDB.publicPosts(SessionManager.sessionOwner(skey));
		cursor.sort( new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne); 
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			String auth=(String) dbo.get("authid");
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("type","post")
					.put("color",color(SessionManager.sessionOwner(skey),auth))
					.put("lat",dbo.get("lat"))
					.put("lon",dbo.get("lon"))
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Return the heavy version of the post corresponding to id
	 * @param id
	 * @param skey
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject getPost(String skey,String id) throws DbException, JSONException{ 
		JSONArray jar=new JSONArray();
		DBObject dbo = PostsDB.getPost(id); 
		String auth=(String) dbo.get("authid");
		jar.put(new JSONObject()
				.put("id",dbo.get("_id"))
				.put("type","post")
				.put("color",color(SessionManager.sessionOwner(skey),auth))
				.put("lat",dbo.get("lat"))
				.put("lon",dbo.get("lon"))
				.put("authname",UserDB.getUsernameById(auth))
				.put("desc",dbo.get("desc"))
				.put("authid",auth)
				.put("date",dbo.get("date"))
				.put("stars",dbo.get("stars")));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }

	/**
	 * Delete a user's post 
	 * @param skey
	 * @param postID
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject deletePost(String skey,String postID) throws DbException, JSONException{
		String uid = SessionManager.sessionOwner(skey);
		String owner = PostsDB.postAuthor(postID);
		if(!uid.equals(owner))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD
					, null, "You dont have the right to modify this comment"
					, ServiceCodes.ACTION_DENIED);
		PostsDB.deletePost(postID);			
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }


	/**
	 * Modify the post postID by replacing its content by update
	 * @param skey
	 * @param postID
	 * @param update
	 * @return
	 * @throws JSONException
	 * @throws MongoException
	 * @throws DbException */
	public static JSONObject modifyPost(String skey,String postID,String update) throws JSONException, MongoException, DbException{
		String uid = SessionManager.sessionOwner(skey);
		String owner = PostsDB.postAuthor(postID);
		if(!uid.equals(owner))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD
					, null, "You dont have the right to modify this post"
					, ServiceCodes.ACTION_DENIED);
		PostsDB.ModifyPost(postID,update);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }



	/**
	 * ADMINISTRATOR FUNCTIONALITY : 
	 * Return all the posts in database
	 * @return
	 * @throws DbException 
	 * @throws JSONException */
	public static JSONObject getAllPostsLocation() throws DbException, JSONException{  
		JSONArray jar=new JSONArray();
		DBCursor cursor = PostsDB.allPosts();
		cursor.sort( new BasicDBObject("date",-1));
		cursor.limit(maxInOne); 
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			String auth=(String)dbo.get("authid");
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("lon",dbo.get("lon"))
					.put("lat",dbo.get("lat"))
					.put("type","post")
					.put("authname",UserDB.getUsernameById(auth)));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				,new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode()); }



	/**
	 * Determine the 'color' of a post according to the relationship 
	 * between two users (@see FriendDB/status) 
	 * @param uid
	 * @param authid
	 * @return 
	 * @throws DbException */
	public static String color(String uid, String authid) throws DbException{
		return uid.equals(authid)
				?
						"author": FriendDB.status(uid,authid).equals("friend")
						?
								"friend":"public"; }













	/*************************************************************************
	 * 
	 * 
	 * DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED
	 * 
	 * 
	 ************************************************************************/

	/**
	 * Add one like to a post
	 * @param skey
	 * @param postID
	 * @return
	 * @throws JSONException
	 * @throws DbException */@Deprecated
	 public static JSONObject likePost(String skey,String postID,int nbStars) throws JSONException, DbException{
		 //TODO verif no multi likes	
		 PostsDB.like(postID,SessionManager.sessionOwner(skey),nbStars);
		 return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				 ,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }


	 /**
	  * Return the list of user's post
	  * @param skey
	  * @return
	  * @throws JSONException
	  * @throws DbException */@Deprecated
	  public static JSONObject userPosts(String skey) throws JSONException, DbException{
		  JSONArray jar=new JSONArray();
		  DBCursor cursor = PostsDB.userPosts(SessionManager.sessionOwner(skey));
		  cursor.sort( new BasicDBObject("date",-1)); 
		  cursor.limit(maxInOne );
		  while (cursor.hasNext()) {
			  DBObject dbo=cursor.next();
			  String authID=(String) dbo.get("authid");
			  jar.put(new JSONObject()
					  .put("id",dbo.get("_id"))
					  .put("type","post")
					  .put("desc",dbo.get("desc"))
					  .put("authid",authID)
					  .put("authname",UserDB.getUsernameById(authID))
					  .put("date",dbo.get("date"))
					  .put("stars",dbo.get("stars")));}		
		  return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				  ,new JSONObject().put("posts",jar), null,
				  ServiceCaller.whichServletIsAsking().hashCode()); }


	  /**
	   * Return the list of user's friends posts  
	   * @param skey
	   * @return
	   * @throws DbException 
	   * @throws JSONException */@Deprecated
	   public static JSONObject friendsPosts(String skey) throws DbException, JSONException{ 
		   JSONArray jar=new JSONArray();
		   DBCursor cursor = PostsDB.friendsPosts(
				   FriendDB.friendsSet(SessionManager.sessionOwner(skey)));
		   cursor.sort( new BasicDBObject("date",-1)); 
		   cursor.limit(maxInOne); 
		   while (cursor.hasNext()){
			   DBObject dbo=cursor.next();
			   String auth=(String) dbo.get("authid");
			   jar.put(new JSONObject()
					   .put("id",dbo.get("_id"))
					   .put("type","post")
					   .put("desc",dbo.get("desc"))
					   .put("authid",auth)
					   .put("authname",UserDB.getUsernameById(auth))
					   .put("date",dbo.get("date"))
					   .put("stars",dbo.get("stars")));}
		   return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				   ,new JSONObject().put("posts",jar), null,
				   ServiceCaller.whichServletIsAsking().hashCode()); }


	   /**
	    * ADMINISTRATOR FUNCTIONALITY
	    * Return all the posts in database
	    * @return
	    * @throws DbException 
	    * @throws JSONException */@Deprecated
	    public static JSONObject getAllPosts() throws DbException, JSONException{  
	    	JSONArray jar=new JSONArray();
	    	DBCursor cursor = PostsDB.allPosts();
	    	cursor.sort( new BasicDBObject("date",-1));
	    	cursor.limit(maxInOne); 
	    	while (cursor.hasNext()){
	    		DBObject dbo=cursor.next();
	    		String auth=(String)dbo.get("authid");
	    		jar.put(new JSONObject()
	    				.put("id",dbo.get("_id"))
	    				.put("type","post")
	    				.put("desc",dbo.get("desc"))
	    				.put("authid",auth)
	    				.put("authname",UserDB.getUsernameById(auth))
	    				.put("date",dbo.get("date"))
	    				.put("stars",dbo.get("stars")));}
	    	return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
	    			,new JSONObject().put("posts",jar), null,
	    			ServiceCaller.whichServletIsAsking().hashCode()); }

	    public static void main(String[] args) throws DbException {
	    	//	addPost("jojo45", "h� h� oppa�");
	    	//String id="580ba04774a4ae857d2ec810";
	    	//like(id);like(id);
	    	//System.out.println("post auth : "+postAuthor(id));
	    	//System.out.println("post : "+postDesc(id));
	    	//ModifyPost(id, "ho ho shiggi");
	    	//deletePost(id);
	    	//System.out.println("posts of user : "+userPosts("jojo45"));
	    	//System.out.println("posts of user's friends : "+friendsPosts(new HashSet<>(Arrays.asList(new String[]{"jojo45"}))));
	    	//System.out.println(getAllPosts());
	    }
}