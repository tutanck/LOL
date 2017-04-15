package mood.posts.db;

import java.util.Date;
import java.util.Set;

import org.bson.types.ObjectId;
import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import db.sqldb.business.FriendDB;
import db.tools.BasicDBListFI;
import db.tools.Coordinates;
import db.tools.DBConnectionManager;
import db.tools.DBToolBox;
import db.tools.DbException;

/**
 * @author Anagbla Jean */									
public class PostsDB {
	public static DBCollection collection = DBConnectionManager.getMongoDBCollection("Posts");

	/**
	 * Add a new post in database 
	 * @param uid
	 * @param desc
	 * @throws DBException */
	public static void addPost(String uid,String desc,double lon,double lat) throws DBException {
		collection.insert(new BasicDBObject()
				.append("authid",uid)
				.append("desc",desc)
				.append("lon",lon)
				.append("lat",lat)
				.append("date",new Date()));

		//TODO after passage de tf df en mongo : 
		//update tf df on each doc insert /delete/update
		//List<String>wordlist= QueryString.wordList(desc, QueryString.tfdfpattern);
		//for(String word : wordlist)
		//int tf =Collections.frequency( wordlist, word );
		//http://stackoverflow.com/questions/22171479/how-to-get-the-id-of-one-document-mongodb-java
		//Mise a jour temps reel des index des mot concernes
	}


	/**
	 * Return the list of reachable posts by user  
	 * @return
	 * @throws DBException */
	public static DBCursor posts() throws DBException {  
		return collection.find(
				new BasicDBObject()
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}	


	/**
	 * Return the list of reachable posts by user  
	 * @return
	 * @throws DBException */
	public static DBCursor postsSQLMODO(String query) throws DBException {  
		return collection.find(
				new BasicDBObject()
				.append("desc",
						new BasicDBObject()
						.append("$regex","/"+query+"/")
						.append("$options","i")
						)
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}	


	/**
	 * Get Full entries of the posts identified by id 
	 * @param id
	 * @return
	 * @throws DBException */
	public static DBObject getPost(String id) throws DBException {
		return collection.findOne(new BasicDBObject()
				.append("_id",new ObjectId(id)));}

	/**
	 * Add status deleted to the post (to hidden it from the search)
	 * @param id
	 * @throws DBException */
	public static void deletePost(String id) throws DBException{
		collection.update(new BasicDBObject().append("_id",new ObjectId(id))
				,new BasicDBObject()
				.append("$set",
						new BasicDBObject()
						.append("status","deleted")));
		//DBObject post=collection.findOne(old);
		//String id = ((ObjectId)post.get("_id")).toString();	
		//String content = (String) post.get("text");	
	}


	/**
	 * Modify the post description (text content) 
	 * @param id
	 * @param desc
	 * @throws DBException
	 * @throws MongoException */
	public static void ModifyPost(String id,String desc) throws DBException,MongoException{
		collection.update(new BasicDBObject().append("_id",new ObjectId(id)),
				new BasicDBObject().append("$set",new BasicDBObject().append("desc",desc)));

		//DBObject post=collection.findOne(old);
		//String id = ((ObjectId)post.get("_id")).toString();	
		//String content = (String) post.get("text");			
		//FastMapReduce.realTimedMapReduceUpdate(content,id,false);
	}



	/**
	 * Return the list of user's  posts  
	 * @param uid
	 * @return
	 * @throws DBException */
	public static DBCursor userPosts(String uid) throws DBException {  
		return collection.find(
				new BasicDBObject()
				.append("authid",uid)
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}
	
	
	/**
	 * Return the list of user's  posts  
	 * @param uid
	 * @return
	 * @throws DBException */
	public static DBCursor userPostsSQLMODO(String query,String uid) throws DBException {  
		return collection.find(
				new BasicDBObject()
				.append("desc",
						new BasicDBObject()
						.append("$regex","/"+query+"/")
						.append("$options","i")
						)
				.append("authid",uid)
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}



	/**
	 * Return the list of user's friends posts  
	 * @param friendSet
	 * @return
	 * @throws DBException */
	public static DBCursor friendsPosts(Set<String>friendSet) throws DBException{ 
		return collection.find(
				new BasicDBObject()
				.append("authid",
						new BasicDBObject ("$in",friendSet))
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}
	
	/**
	 * Return the list of user's friends posts  
	 * @param friendSet
	 * @return
	 * @throws DBException */
	public static DBCursor friendsPosts(String uid) throws DBException{ 
		try {return friendsPosts(FriendDB.friendsSet(uid));}
		catch (JSONException e) {throw new DBException(DBToolBox.getStackTrace(e));}}
	
	
	
	/**
	 * Return the list of user's friends posts  
	 * @param friendSet
	 * @return
	 * @throws DBException */
	public static DBCursor friendsPostsSQLMODO(String query,Set<String>friendSet) throws DBException{ 
		return collection.find(
				new BasicDBObject()
				.append("desc",
						new BasicDBObject()
						.append("$regex","/"+query+"/")
						.append("$options","i")
						)
				.append("authid",
						new BasicDBObject ("$in",friendSet))
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}


	/**
	 * Return the list of user's friends posts  
	 * @param friendSet
	 * @return
	 * @throws DBException */
	public static DBCursor friendsPostsSQLMODO(String query,String uid) throws DBException{ 
		try {return friendsPostsSQLMODO(query,FriendDB.friendsSet(uid));}
		catch (JSONException e) {throw new DBException(DBToolBox.getStackTrace(e));}}



	/**
	 * Return the list of public posts according to the user's friends and identity  
	 * @param friendSet
	 * @return
	 * @throws DBException */
	public static DBCursor publicPosts(String uid) throws DBException{ 
		try {Set<String>friendSet=FriendDB.friendsSet(uid);
		friendSet.add(uid);
		return collection.find(
				new BasicDBObject()
				.append("authid",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject ("$in",friendSet)))
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}
		catch (JSONException e) {throw new DBException(DBToolBox.getStackTrace(e));}}
	
	/**
	 * Return the list of public posts according to the user's friends and identity  
	 * @param friendSet
	 * @return
	 * @throws DBException */
	public static DBCursor publicPostsSQLMODO(String query,String uid) throws DBException{ 
		try {Set<String>friendSet=FriendDB.friendsSet(uid);
		friendSet.add(uid);
		return collection.find(
				new BasicDBObject()
				.append("desc",
						new BasicDBObject()
						.append("$regex","/"+query+"/")
						.append("$options","i")
						)
				.append("authid",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject ("$in",friendSet)))
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}
		catch (JSONException e) {throw new DBException(DBToolBox.getStackTrace(e));}}






	/**
	 * Return the post's author 
	 * @param id
	 * @return
	 * @throws DBException */
	public static String postAuthor(String id) throws DBException{ 
		DBObject post=collection.findOne(
				new BasicDBObject().append("_id", new ObjectId(id)));
		if(post!=null)
			return (String)post.get("authid");
		return null;}


	/**
	 * Return the description of a post (text content)
	 * @param id
	 * @return */
	public static String postDesc(String id) {
		DBObject post=collection.findOne(
				new BasicDBObject().append("_id",new ObjectId(id)));	
		if(post!=null)
			return (String)post.get("desc");
		return null;}


	/**
	 * Return the Coordinates of a post (text location)
	 * @param id
	 * @return */
	public static Coordinates postLocation(String id) {
		DBObject post=collection.findOne(
				new BasicDBObject().append("_id",new ObjectId(id)));	
		if(post!=null)
			return new Coordinates((double)post.get("lon"),(double)post.get("lat"));
		return null;}



	/**
	 * ADMINISTRATOR FUNCTIONALITY or sub function for search in general and 
	 * SearchPostsDB in particular
	 * Return all the posts in database
	 * @return
	 * @throws DBException */
	public static DBCursor allPosts() throws DBException{  
		return collection.find();}


	/**
	 * (**Generic function**)
	 * ADMINISTRATOR FUNCTIONALITY or sub function for search in general and 
	 * SearchPostsDB in particular
	 * Return all the posts in database
	 * @return
	 * @throws DBException */
	public static DBCursor featuredPosts(BasicDBObject feature) throws DBException{  
		return collection.find(feature);}














	/**
	 * Add a like to a post 
	 * @param id
	 * @throws DBException
	 * @throws MongoException */@Deprecated
	 public static void like(String id,String uid, int nbStars) throws DBException,MongoException{
		 collection.update(
				 new BasicDBObject()
				 .append("_id",new ObjectId(id)),
				 new BasicDBObject()
				 .append("$addToSet",
						 new BasicDBObject()
						 .append("stars",
								 new BasicDBObject()
								 .append("authid", uid)
								 .append("nbstars", nbStars))));}

	 public static void main(String[] args) throws DBException {
		 addPost("d910952c404b4b6cca5d6f61a5ab9df0","lol desc",48.799, 2.322);
	 }
}