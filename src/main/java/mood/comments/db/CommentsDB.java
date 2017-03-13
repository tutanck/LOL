package mood.comments.db;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import db.tools.BasicDBListFI;
import db.tools.DBConnectionManager;
import db.tools.DbException;

/**
 * @author Anagbla Jean */
public class CommentsDB {

	private static DBCollection collection = DBConnectionManager.getMongoDBCollection("Comments");

	/**
	 * Add a new comment for the post identified by pid
	 * @param uid
	 * @param com
	 * @param pid */
	public static void newComment(String uid, String com,String pid){
		collection.insert(
				new BasicDBObject()
				.append("pid",pid)
				.append("authid",uid)
				.append("date",new Date())
				.append("com",com));
	}


	/**
	 * Modify the comment content identified by cid
	 * @param cid
	 * @param com
	 * @throws DbException
	 * @throws MongoException */
	public static void modify(String cid,String com) throws DbException,MongoException{	
		collection.update(
				new BasicDBObject()
				.append("_id",new ObjectId(cid))
				,new BasicDBObject()
				.append("$set"
						,new BasicDBObject()
						.append("com",com)));}


	/**
	 * Delete the comment identified by cid
	 * @param cid */
	public static void cleanComment(String cid) {
		collection.update(
				new BasicDBObject()
				.append("_id",new ObjectId(cid))
				,new BasicDBObject()
				.append("$set",
						new BasicDBObject()
						.append("status","deleted")));}


	/**
	 * Return the comment author 
	 * @param cid
	 * @return */
	public static String commentAuthor(String cid) {		
		DBObject com=collection.findOne(
				new BasicDBObject()
				.append("_id",new ObjectId(cid)));
		if(com!=null)
			return (String) com.get("authid");
		return null;
	}


	/**
	 * Return the comment identified by cid
	 * @param cid
	 * @return */
	public static String getComment(String cid){
		DBObject com=collection.findOne(
				new BasicDBObject()
				.append("_id",new ObjectId(cid)));	
		if(com!=null)
			return (String) com.get("com");
		return null;
	}


	/**
	 * Return all comments associated to the post identified by pid
	 * @param pid
	 * @return
	 * @throws DbException */
	public static DBCursor postComments(String pid) throws DbException{		
		 return collection.find(
				new BasicDBObject()
				.append("pid",pid)
				.append("status",
						new BasicDBObject()
						.append("$not",
								new BasicDBObject()
								.append("$in",
										BasicDBListFI.add("deleted")))));}


	/**
	 * Delete all  post's comments
	 * @param pid */
	public static void deletePostComments(String pid) {
		collection.update(
				new BasicDBObject()
				.append("pid",new ObjectId(pid))
				,new BasicDBObject()
				.append("$set",
						new BasicDBObject()
						.append("status","deleted")),true,true);}


	/**
	 * ADMINISTRATOR FUNCTIONALITY
	 * Return all comments in database
	 * @return
	 * @throws DbException */
	public static DBCursor comnentsHeadInfos() throws DbException{ 
		return collection.find();}	
}