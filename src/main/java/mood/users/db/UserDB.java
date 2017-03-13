package mood.users.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import tools.db.DBConnectionManager;
import tools.db.DbException;


/**
 * @author AJoan
 */
public class UserDB {

	public static DBCollection collection = DBConnectionManager.getMongoDBCollection("users");

	/**
	 * @METHODE_NAME 		 addUser
	 * @DESCRIPTION 		 add a new user in users database    
	 * @param username
	 * @param pass
	 * @param email
	 * @throws DbException */
	public static void addUser(
			String username,
			String pass,
			String email
	) throws DbException{
		collection.insert(
				new BasicDBObject()
						.append("username",username)
						.append("pass",pass)
						.append("email",email)
						.append("_date","now()")
						.append("confirmed",false));
	}

	/**
	 * @METHODE_NAME 		 updateUserInfo
	 * @DESCRIPTION 		 update complete information on a new user in users database    
	 * @param username
	 * @param pass
	 * @param email
	 * @param lastname
	 * @param firstname
	 * @param birthdate
	 * @param phone
	 * @throws DbException
	 */
	public static void updateUserInfo(String uid, String username,String pass,String email,
									  String lastname,String firstname,String birthdate,String phone) throws DbException{
		CRUD.CRUDPush(
				"UPDATE "+businessTable+" SET username='"+username+"' ,"
						+ " pass='"+pass+"' , email='"+email+"' , lastname='"+lastname+"' , firstname='"+firstname+"' , "
						+ " birthdate='"+birthdate+"' , phone='"+phone+"' WHERE uid='"+uid+"' ;"
				,"updateUserInfo");
	}


	/**
	 * @METHODE NAME 	usernameIsTaken
	 * @DESCRIPTION 	check if username is already used by someone else 	 
	 * @param username
	 * @return
	 * @throws DbException
	 */
	public static boolean usernameIsTaken(String username) throws DbException {
		return CRUD.CRUDCheck(
				"SELECT * FROM "+businessTable+" WHERE USERNAME= '"+username+"' ;"
				,"usernameIsTaken");
	}


	/**
	 * @METHODE NAME 	matchUsernamePass
	 * @DESCRIPTION 	check login/password correspondence 
	 * @param username
	 * @param pass
	 * @return
	 * @throws DbException
	 */
	public static boolean passMatchUsername(String username,String pass) throws DbException {
		return CRUD.CRUDCheck(
				"SELECT * FROM "+businessTable+" WHERE USERNAME= '"+username+"' AND PASS= '"+pass+"';"
				,"passMatchUsername");
	}


	/**
	 * @METHODE NAME 	uidExists
	 * @DESCRIPTION 	check the presence of an uid (user ID) in users database
	 * @param uid
	 * @return
	 * @throws DbException
	 */
	public static boolean uidExists(String uid) throws DbException {
		return CRUD.CRUDCheck(
				"SELECT * FROM "+businessTable+" WHERE uid='"+uid+"' ;"
				,"uidExists");
	}

	/*
	 * It is possible to make the methods above much more generic in which case
	 * SQL would be completely hidden, by using a map and a map
	 * iterator to go through the Map entries to automatically build requests 
	 * @see THINGS
	 */

	/**
	 * @METHODE_NAME 		getUidByUsername
	 * @DESCRIPTION 		return user ID from his username
	 * @param username
	 * @return
	 * @throws DbException
	 */
	public static String getUidByUsername(String username) throws DbException {
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE username= '"+ username+ "' ;");
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getString("uid");}
		catch (SQLException e) {throw new DbException(
				"@?getUidByUsername SQLError : " + e.getMessage());}
		return "";//Not a result
	}

	/**
	 * @METHODE_NAME 		getUidByUsername
	 * @DESCRIPTION 		return user ID from his username
	 * @param where
	 * @param table
	 * @return
	 * @throws DbException
	 */
	public static String getUidByUsername(Map<String,String> where,String table) throws DbException {
		CSRShuttleBus csr = CRUD.CRUDPull(THINGS.getTHINGS(where,table));
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getString("uid");}
		catch (SQLException e) {throw new DbException(
				"@?getUidByUsername SQLError : " + e.getMessage());}
		return "";//Not a result
	}



	/**
	 * @METHODE_NAME 		getUsernameById
	 * @DESCRIPTION 		return username from his user ID address
	 * @param uid
	 * @return String
	 * @throws DbException
	 */
	public static String getUsernameById(String uid) throws DbException {
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE uid='"+uid+"';");
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getString("username");}
		catch (SQLException e) {
			throw new DbException(
					"@?getUsernameById SQLError : " + e.getMessage());}
		return "";//return null; vrmt pas fan
	}

	/**
	 * @METHODE_NAME 		getUsernameById
	 * @DESCRIPTION 		return username from his user ID address
	 * @param where
	 * @param table
	 * @return
	 * @throws DbException
	 */
	public static String getUsernameById(Map<String,String> where,String table) throws DbException {
		CSRShuttleBus csr = CRUD.CRUDPull(THINGS.getTHINGS(where,table));
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getString("username");}
		catch (SQLException e) {
			throw new DbException(
					"@?getUsernameById SQLError : " + e.getMessage());}
		return "";
	}


	/**
	 * METHODE NAME 		: getFirstnameById
	 * DESCRIPTION 			: return user's firstname from user id
	 * @param uid
	 * @return String
	 * @throws DbException
	 */
	public static String getFirstnameById(String uid) throws DbException {
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE uid='"+uid+"';");
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getString("firstname");
			csr.close();}
		catch (SQLException e) {throw new DbException(
				"getIdentitybyId SQL Error: " + e.getMessage());}
		return "";
	}

	/**
	 * METHODE NAME 		: getLastnameById
	 * DESCRIPTION 			: return user's firstname from user id
	 * @param uid
	 * @return String
	 * @throws DbException
	 */
	public static String getLastnameById(String uid) throws DbException {
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE uid='"+uid+"' ;");
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getString("lastname");
			csr.close();}
		catch (SQLException e) {throw new DbException(
				"getIdentitybyId SQL Error: " + e.getMessage());}
		return "";
	}


	/**
	 * METHODE NAME 		: confirmUser
	 * DESCRIPTION 			: confirm an user account (email is checked)
	 * @param id
	 * @throws DbException
	 * @throws SQLException	 */
	public static void confirmUser(String uid) throws DbException {
		CRUD.CRUDPush( "UPDATE USERS SET " +
				"status='confirmed' WHERE uid = '" + uid+ "' ;","confirmUser");	}


	/**
	 * METHODE NAME 		: isUserConfirmed
	 * DESCRIPTION 			: check if user account is confirmed (email is checked)
	 * @param id
	 * @return
	 * @throws DbException
	 * @throws SQLException	 */
	public static Boolean isConfirmed(String uid) throws DbException {
		return CRUD.CRUDCheck("SELECT * FROM USERS WHERE status='confirmed'"
				+ " AND uid = '" + uid+ "' ;","isUserConfirmed");}

	public static CSRShuttleBus searchUser(String uid,String query) throws DbException{
		return CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE username LIKE '"+query+"%' "
						+ "AND uid <> '"+uid+"' ;");}

}