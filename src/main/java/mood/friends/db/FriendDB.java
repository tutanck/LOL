package mood.friends.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import db.sqldb.creator.tabledefs.FriendsDef;
import db.sqldb.regina.CRUD;
import db.sqldb.regina.CSRShuttleBus;
import db.sqldb.regina.THINGS;
import db.tools.DBToolBox;
import db.tools.DbException;
import services.tools.MapRefiner;

public class FriendDB {

	public static String table=FriendsDef.table;

	/**
	 * @description return a Set <of user's friends ID> 
	 * @param map
	 * @return
	 * @throws JSONException 
	 * @throws DBException */
	public static Set<String> friendsSet(String uid) throws JSONException, DBException {
		Map<String,String> bigMap=new HashMap<>();
		bigMap.put("uid", uid);

		Set<String>friendsSet = new HashSet<>();
		CSRShuttleBus dataSet = CRUD.CRUDPull(THINGS.getTHINGS(JSONRefiner.subMap(
				bigMap,new String[]{"uid"}),table));
		ResultSet rs=dataSet.getResultSet();
		try {while(rs.next())
			friendsSet.add(rs.getString("fid"));}
		catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		dataSet.close();
		return friendsSet;}


	public static String status(String uid, String fid) throws DBException {
		String status="user";
		if(!CRUD.CRUDCheck( //any relationship ? which_status : user
				"SELECT * FROM "+table+" WHERE "
						+ "    ( uid='"+uid+"' AND fid='"+fid+"') "
						+ " OR ( uid='"+fid+"' AND fid='"+uid+"');"
						,FriendDB.class.getName())) return status;

		status= "friend"; //we suppose they're friends 		
		if(CRUD.CRUDCheck(
				"SELECT * FROM "+table+" WHERE "
						+ "    ( uid='"+uid+"' AND fid='"+fid+"' AND status = 'friend') "
						+ " OR ( uid='"+fid+"' AND fid='"+uid+"' AND status = 'friend');"
						,FriendDB.class.getName())) return status;
		//if not, one of them if waiting for reply
		return status = CRUD.CRUDCheck(
				"SELECT * FROM "+table+" WHERE "
						+ "    ( uid='"+uid+"' AND fid='"+fid+"' AND status = 'waiting' ) ;"
						,FriendDB.class.getName())?"pending" : "waiting"; }

	
	public static void main(String[] args) throws DBException {
		System.out.println(status("F","T"));}

}
