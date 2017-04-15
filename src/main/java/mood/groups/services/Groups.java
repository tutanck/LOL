package mood.groups.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.mongo.GroupsDB;
import db.sqldb.business.UserDB;
import db.tools.DbException;
import db.tools.QueryString;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;
import services.tools.SessionManager;


/**
 * No heavy error management,
 * the server ensures that the outputs are always limited 
 * to what the client has the right to do in the current context
 * @author Anagbla Jean 
 * **@goodToKnow ! FLUENT STYLE CODE*/
public class Groups {

	private static int maxInOne=50;

	/**
	 * Create a new group owned by the user
	 * @param skey
	 * @param name
	 * @param members
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject createGroup(String skey,String name,String members) 
			throws DBException,JSONException{		
		GroupsDB.openGroup(name,UserSession.sessionOwner(skey),QueryString.wordSet(members,",")); 
		return  ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
				, null, null, ServiceCodes.NOERROR);}


	/**
	 * Return user's owned groups list
	 * @param skey
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject userGroups(String skey) throws DBException, JSONException{
		JSONArray jar=new JSONArray();
		DBCursor cursor = GroupsDB.userGroups(UserSession.sessionOwner(skey));
		cursor.sort(new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne);
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			jar.put(new JSONObject()
					//.put("status",dbo.get("status"))//should always be open //Debug
					.put("id",dbo.get("_id"))
					.put("name",dbo.get("name"))
					.put("owner",dbo.get("owner"))
					.put("date",dbo.get("date"))
					.put("members",dbo.get("members")));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,jar,null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * Delete a group owned by the user
	 * @param skey
	 * @param groupID
	 * @return
	 * @throws JSONException
	 * @throws DBException */
	public static JSONObject deleteGroup(String groupID) throws JSONException, DBException{
		GroupsDB.closeGroup(groupID); 
		return  ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,null,null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * Return the list of group's members 
	 * @param groupID
	 * @return
	 * @throws JSONException
	 * @throws DBException */
	public static JSONObject groupMembers(String groupID) throws JSONException, DBException{
		BasicDBList members= GroupsDB.groupMembers(groupID);
		JSONArray jar = new JSONArray();
		for(Object member : members){
			jar.put(new JSONObject()
			.put("id",(String)member)
			.put("member",UserDB.getUsernameById((String)member)));}
		return  ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,jar,null,
				ServiceCaller.whichServletIsAsking().hashCode());}



	/**
	 * Add a member to the group 
	 * @param skey
	 * @param groupID
	 * @param member
	 * @return
	 * @throws JSONException
	 * @throws DBException */
	public static JSONObject addMember(String groupID,String member) throws JSONException, DBException {
		GroupsDB.addMember(groupID, member);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,null,null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * Remove a member from the group
	 * @param skey
	 * @param groupID
	 * @param member
	 * @return
	 * @throws JSONException */
	public static JSONObject removeMember(String groupID,String member) throws JSONException {
		GroupsDB.deleteMember(groupID, member);		
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,null,null,
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * ADMINISTRATOR FUNCTIONALITY
	 * Returns the groups the user belongs 
	 * Note : the user will never know groups he belongs
	 * @param uid
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject userMembership(String uid) throws DBException, JSONException{
		JSONArray jar=new JSONArray();
		DBCursor cursor = GroupsDB.userMembership(uid);
		cursor.sort( new BasicDBObject("date",-1));
		cursor.limit(maxInOne);
		while (cursor.hasNext()){
			DBObject dbo=cursor.next();
			jar.put(new JSONObject()
					.put("id",dbo.get("_id"))
					.put("name",dbo.get("name"))
					.put("owner",dbo.get("owner"))
					.put("date",dbo.get("date"))
					.put("status",dbo.get("status"))
					.put("members",dbo.get("members")));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,jar,null,
				ServiceCaller.whichServletIsAsking().hashCode());}
	
	
	/**
	 * ADMINISTRATOR FUNCTIONALITY
	 * Return Head informations about all existing groups in database
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	private static JSONObject groupsHeadInfos() throws DBException, JSONException {
		DBCursor cursor =GroupsDB.groupsHeadInfos();
		cursor.sort(new BasicDBObject("date",-1)); 
		cursor.limit(maxInOne);
		JSONArray jar=new JSONArray();
		while (cursor.hasNext()){			 
			DBObject dbo=cursor.next();			
			jar.put(new JSONObject()
			.put("id",dbo.get("_id")) 
			.put("name",dbo.get("name"))
			.put("owner",dbo.get("owner"))
			.put("date",dbo.get("date"))
			.put("status",dbo.get("status")));}
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,jar,null,
				ServiceCaller.whichServletIsAsking().hashCode());}
	


	public static void main(String[] args) throws DBException, JSONException{
		//collection.drop();//reset : determinism required for the tests
		//openGroup("amis", "Acl64",Arrays.asList(new String[]{"jhk84","dfldm8648"}));
		//openGroup("amis", "Aop64",Arrays.asList(new String[]{"joan527","jojo877587"}));
		//openGroup("amis", "Aop64",Arrays.asList(new String[]{"lolfgdsg727","jojo877587"}));
		String id="580a89f174a4f912f56a7568";
		System.out.println(userGroups("Aop64"));
		System.out.println(userMembership("jojo877587"));
		System.out.println(GroupsDB.groupName(id));
		System.out.println(GroupsDB.groupOwner(id));
		System.out.println(GroupsDB.groupStatus(id));
		System.out.println(groupMembers(id));
		addMember(id,"lola42");
		deleteGroup(id);
		removeMember(id,"lola42");
		System.out.println(userGroups("Aop64"));
		System.out.println(userMembership("jojo877587"));
		System.out.println(GroupsDB.groupName(id));
		System.out.println(GroupsDB.groupOwner(id));
		System.out.println(GroupsDB.groupStatus(id));
		System.out.println(groupMembers(id));
		System.out.println(groupsHeadInfos()+"\n");
	}
}