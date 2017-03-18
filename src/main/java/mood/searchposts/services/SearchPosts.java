package mood.searchposts.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.mapreduce.ObjetRSV;
import db.mongo.CommentsDB;
import db.mongo.PostsDB;
import db.mongo.SearchPostsDB;
import db.sqldb.business.UserDB;
import db.tools.DbException;
import services.mapreduce.MRProgram;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;

/**
 * @author  Anagbla Jean
 * **@goodToKnow ! FLUENT STYLE CODE*/
public class SearchPosts {	

	/**
	 * Return location's informations about all post matching the query string. 
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject QMatchingAllPostsLocation(String query,String skey)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingAllPosts(query))
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("color",Posts.color(UserSession.sessionOwner(skey),
							(String) orsv.getDbo().get("authid")))
					.put("lat",orsv.getDbo().get("lat"))
					.put("lon",orsv.getDbo().get("lon"))
					//.put("score",dbo.get("score"));//Debug on
					.put("authname",UserDB.getUsernameById(
							(String) orsv.getDbo().get("authid"))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI, 
				new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * Return location's informations about all user's post matching the query string.
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @param skey
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject QMatchingMyPostsLocation(String query,String skey)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingMyPosts(query,
				UserSession.sessionOwner(skey)))
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("color",Posts.color(UserSession.sessionOwner(skey),
							(String) orsv.getDbo().get("authid")))
					.put("lat",orsv.getDbo().get("lat"))
					.put("lon",orsv.getDbo().get("lon"))
					//.put("score",dbo.get("score"));//Debug on
					.put("authname",UserDB.getUsernameById(
							(String) orsv.getDbo().get("authid"))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI, 
				new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * Return location's informations about all user friends's post matching the query string. 
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @param skey
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject QMatchingFriendsPostsLocation(String query,String skey)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingFriendsPosts(query,
				UserSession.sessionOwner(skey)))	
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("color",Posts.color(UserSession.sessionOwner(skey),
							(String) orsv.getDbo().get("authid")))
					.put("lat",orsv.getDbo().get("lat"))
					.put("lon",orsv.getDbo().get("lon"))
					//.put("score",dbo.get("score"));//Debug on
					.put("authname",UserDB.getUsernameById(
							(String) orsv.getDbo().get("authid"))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI, 
				new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode());}

	/**
	 * Return location's informations about all public post matching the query string. 
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @param skey
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject QMatchingPublicPostsLocation(String query,String skey)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingPublicPosts(query,
				UserSession.sessionOwner(skey)))	
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("color",Posts.color(UserSession.sessionOwner(skey),
							(String) orsv.getDbo().get("authid")))
					.put("lat",orsv.getDbo().get("lat"))
					.put("lon",orsv.getDbo().get("lon"))
					//.put("score",dbo.get("score"));//Debug on
					.put("authname",UserDB.getUsernameById(
							(String) orsv.getDbo().get("authid"))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI, 
				new JSONObject().put("posts",jar), null,
				ServiceCaller.whichServletIsAsking().hashCode());}

	
	
	
	

	
	
	
	
	/************************************************************************
	 * 
	 * 
	 * *DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED * *
	 * 
	 * 
	 ************************************************************************/
	
	/**
	 * Return all post matching the query string. 
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @return
	 * @throws DBException
	 * @throws JSONException */@Deprecated
	public static JSONObject QMatchingAllPosts(String query)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingAllPosts(query))
 			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("desc",orsv.getDbo().get("desc"))
					.put("date",orsv.getDbo().get("date"))
					//.put("score",dbo.get("score"));//Debug on
					.put("comments",CommentsDB.postComments((String)orsv.getDbo().get("_id")))
					.put("auteur",
							new JSONObject()
							.put("auth",(String) orsv.getDbo().get("authid"))
							.put("authname",UserDB.getUsernameById(
									(String) orsv.getDbo().get("authid")))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,jar,null,ServiceCaller.whichServletIsAsking().hashCode());}
	
	
	/**
	 * Return all user's post matching the query string.
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @param skey
	 * @return
	 * @throws DBException
	 * @throws JSONException */@Deprecated
	public static JSONObject QMatchingMyPosts(String query,String skey)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingMyPosts(query,
				UserSession.sessionOwner(skey)))
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("desc",orsv.getDbo().get("desc"))
					.put("date",orsv.getDbo().get("date"))
					//.put("score",dbo.get("score"));//Debug on
					.put("comments",CommentsDB.postComments((String)orsv.getDbo().get("_id")))
					.put("auteur",
							new JSONObject()
							.put("auth",(String) orsv.getDbo().get("authid"))
							.put("authname",UserDB.getUsernameById(
									(String) orsv.getDbo().get("authid")))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,jar,null,ServiceCaller.whichServletIsAsking().hashCode());}	
	
	/**
	 * Return all user friends's post matching the query string. 
	 * The results are ordered by relevance score provided by map reduce sorting
	 * @param query
	 * @param skey
	 * @return
	 * @throws DBException
	 * @throws JSONException */@Deprecated
	public static JSONObject QMatchingFriendsPosts(String query,String skey)throws DBException, JSONException{
		JSONArray jar =new JSONArray();
		for(ObjetRSV orsv:SearchPostsDB.QMatchingFriendsPosts(query,
				UserSession.sessionOwner(skey)))
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","post")
					.put("desc",orsv.getDbo().get("desc"))
					.put("date",orsv.getDbo().get("date"))
					//.put("score",dbo.get("score"));//Debug on
					.put("comments",CommentsDB.postComments((String)orsv.getDbo().get("_id")))
					.put("auteur",
							new JSONObject()
							.put("auth",(String) orsv.getDbo().get("authid"))
							.put("authname",UserDB.getUsernameById(
									(String) orsv.getDbo().get("authid")))));
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,jar,null,ServiceCaller.whichServletIsAsking().hashCode());}


	public static void main(String[] args) throws JSONException, DBException {
		PostsDB.collection.drop();//reset : determinism required for the tests
		PostsDB.addPost("jojo41", "he he oppai",2.3,4.5);
		PostsDB.addPost("jojo42", "he he oppai to shiggi",2.3,4.5);
		PostsDB.addPost("jojo43", "he he shiggi desu nein",2.3,4.5);
		PostsDB.addPost("jojo44", "he he oppai desu nein",2.3,4.5);
		PostsDB.addPost("jojo45", "he he oppai to shiggi desu nein",2.3,4.5); 
		MRProgram.ResetTFDFDB();
		System.out.println(QMatchingAllPosts("oppai shiggi nein"));
		JSONArray jares=((JSONArray)QMatchingAllPosts("oppai shiggi nein").get("result"));
		for(int i=0; i< jares.length();i++)
			System.out.println(jares.get(i));}

}