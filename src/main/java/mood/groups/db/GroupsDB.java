package mood.groups.db;

import java.util.Date;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
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
public class GroupsDB{

	private static DBCollection collection = DBConnectionManager.getMongoDBCollection("Groups");

	/**
	 * Add a new group to groups database 
	 * @param gname
	 * @param ownerID
	 * @param membersID
	 * @throws DbException */
	public static void openGroup(String gname,String ownerID, Set<String>membersID){
		collection.insert(
				new BasicDBObject()
				.append("name",gname)
				.append("owner",ownerID)
				.append("date",new Date()) 
				.append("status","open")
				.append("members",BasicDBListFI.addAll(membersID)));}


	/**
	 * Close a group identified by gid
	 * @param gid
	 * @throws DbException
	 * @throws MongoException */
	public static void closeGroup(String gid) {
		collection.update(
				new BasicDBObject()
				.append("_id", new ObjectId(gid))
				,new BasicDBObject()
				.append("$set",
						new BasicDBObject()
						.append("status","closed")));}

	/**
	 * Add members to the group identified by gid 	
	 * @param idGroup
	 * @param membersIdList
	 * @throws DbException
	 * @throws MongoException */
	public static void addMember(String gid, String memberID) throws DbException{
		//$addToSet do not add the item to the given field if it already contains it
		collection.update(
				new BasicDBObject()
				.append("_id",new ObjectId(gid))
				,new BasicDBObject()
				.append("$addToSet"
						,new BasicDBObject()
						.append("members",memberID)));}


	/**
	 * DelMembers (from the selected group)
	 * @param idGroup
	 * @param membersIdList
	 * @throws DbException
	 * @throws MongoException */
	public static void deleteMember(String gid,String memberID){
		collection.update(
				new BasicDBObject()
				.append("_id",new ObjectId(gid))
				,new BasicDBObject()
				.append("$pull"
						,new BasicDBObject("members", memberID)));}


	/**
	 * Returns open(active) groups owned by the user identified by uid
	 * @param uid
	 * @return
	 * @throws DbException */
	public static DBCursor userGroups(String uid) throws DbException {  
		return collection.find(
				new BasicDBObject()
				.append("owner", uid)
				.append("status","open"));}


	/**
	 * Return a BasicDBList of group's members
	 * @param gid
	 * @return */
	public static BasicDBList groupMembers(String gid) { 
		DBObject dbo = collection.findOne(
				new BasicDBObject().append("_id",new ObjectId(gid)));
		if(dbo!=null)
			return (BasicDBList) dbo.get("members");
		return null;
	}


	/**
	 * Return Group's owner uid
	 * @param gid
	 * @return
	 * @throws DbException	*/
	public static String groupOwner(String gid) {  
		DBObject dbo = collection.findOne(
				new BasicDBObject().append("_id",new ObjectId(gid)));
		if(dbo!=null)
			return (String) dbo.get("owner");
		return null;
	}


	/**
	 * Return Group's name from gid
	 * @param gid
	 * @return */
	public static String groupName(String gid) {
		DBObject dbo = collection.findOne(
				new BasicDBObject().append("_id",new ObjectId(gid)));
		if(dbo!=null)
			return (String) dbo.get("name");
		return null;
	}

	/**
	 * Return Group's status from gid
	 * @param gid
	 * @return */
	public static String groupStatus(String gid) {
		DBObject dbo = collection.findOne(
				new BasicDBObject().append("_id",new ObjectId(gid)));
		if(dbo!=null)
			return (String) dbo.get("status");
		return null;
	}


	/**
	 * ADMINISTRATOR FUNCTIONALITY
	 * Returns the groups the user belongs 
	 * Note : the user will never know groups he belongs  
	 * @param uid
	 * @return
	 * @throws DbException */
	public static DBCursor userMembership(String uid) throws DbException{  
		return collection.find(
				new BasicDBObject().append(
						"members"
						,new BasicDBObject (
								"$in"
								,BasicDBListFI.add(uid))));}


	/**
	 * ADMINISTRATOR FUNCTIONALITY
	 * Return Head informations about all existing groups in database
	 * @return
	 * @throws DbException */
	public static DBCursor groupsHeadInfos() throws DbException{  
 		return collection.find();}
}