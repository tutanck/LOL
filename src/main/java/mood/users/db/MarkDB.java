package mood.users.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.sqldb.creator.tabledefs.MarksDef;
import db.sqldb.regina.CRUD;
import db.sqldb.regina.CSRShuttleBus;
import db.tools.DbException;
import services.tools.SessionManager;

/**
 * @author AJoan
 * THINGS's Father (old db programming model (without using ORM) )
 */
public class MarkDB {

	private final static String businessTable =MarksDef.table;	

	/**
	 * @METHODE_NAME 		 addMark
	 * @DESCRIPTION 		 add a new mark for an user in database    
	 * @param username
	 * @param skey
	 * @param mark
	 * @throws DBException
	 */
	public static void addMark(String username,String skey,double mark) throws DBException{ 
		String mid = SessionManager.sessionOwner(skey);
		String uid=UserDB.getUidByUsername(username);
		CRUD.CRUDPush(
				"INSERT INTO "+businessTable+" values ('"+uid+"', '"+mid+"' , '"+mark+"') ;"
				,"addMark");
	}

	/**
	 * @METHODE_NAME 		 updateMark
	 * @DESCRIPTION 		 update mark for an user in database (from the same user mid)    
	 * @param username
	 * @param skey
	 * @param mark
	 * @throws DBException
	 */
	public static void updateMark(String username,String skey,double mark) throws DBException{ 
		String uid = UserDB.getUidByUsername(username);
		CRUD.CRUDPush(
				"UPDATE "+businessTable+" SET mark='"+mark+"'  WHERE uid='"+uid+"' ;"
				,"updateMark");	 
	}


	/**
	 * @METHODE_NAME 		 userMarkIsGreaterThan
	 * @DESCRIPTION 		 check if user's mark is greater or equal than a criterion     
	 * @param username
	 * @param criterion
	 * @return
	 * @throws DBException
	 */
	public static boolean userMarkIsGreaterThan(String username,double criterion) throws DBException {
		String uid = UserDB.getUidByUsername(username);
		return CRUD.CRUDCheck(
				"SELECT * FROM "+businessTable+" WHERE uid= '"+uid+"' AND SUM(mark) >='"+criterion+"';"
				,"usernameIsTaken");
	}

	
	/**
	 * @METHODE_NAME 		 userMarkIsLowerThan
	 * @DESCRIPTION 		 check if user's mark is lower or equal than a criterion     
	 * @param username
	 * @param criterion
	 * @return
	 * @throws DBException
	 */
	public static boolean userMarkIsLowerThan(String username,double criterion) throws DBException {
		String uid = UserDB.getUidByUsername(username);
		return CRUD.CRUDCheck(
				"SELECT * FROM "+businessTable+" WHERE uid= '"+uid+"' AND SUM(mark) <='"+criterion+"';"
				,"usernameIsTaken");
	}
 

	/**
	 * @METHODE_NAME 		getMarkByUsername
	 * @DESCRIPTION 		return user mark from his username
	 * @param username
	 * @return
	 * @throws DBException
	 */
	public static Double getMarkByUsername(String username) throws DBException {
		String uid = UserDB.getUidByUsername(username);
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE uid= '"+ uid+ "' ;");
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getDouble("mark");}
		catch (SQLException e) {throw new DBException(
				"@?getMarkByUsername SQLError : " + e.getMessage());}
		return 0.0;//Not a result
	} 

	
	/**
	 * @METHODE_NAME 		getMarkByUID
	 * @DESCRIPTION 		return user's mark from his user ID address
	 * @param uid
	 * @return Double
	 * @throws DBException
	 */
	public static Double getMarkByUID(String uid) throws DBException {
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE uid="+uid+";");
		try { if (csr.getResultSet().next())
			return csr.getResultSet().getDouble("mark");}
		catch (SQLException e) {
			throw new DBException(
					"@?getMarkByUID SQLError : " + e.getMessage());}
		return 0.0;//Not a result
	}


	/**
	 * @METHODE_NAME 		getUIDListWhereMarkGreaterThan
	 * @DESCRIPTION 		return a list of users uid if their mark is greater than criterion
	 * @param criterion
	 * @return
	 * @throws DBException
	 */
	public static List<String> getUIDListWhereMarkGreaterThan(double criterion) throws DBException {
		List<String> list=new ArrayList<>();
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE mark >="+criterion+";"); //group by uid TODO
		try { while (csr.getResultSet().next())
			list.add(csr.getResultSet().getString("uid"));
		csr.close();}
		catch (SQLException e) {throw new DBException(
				"getUIDListWhereMarkGreaterThan SQL Error: " + e.getMessage());}
		return list;
	}
	
	/**
	 * @METHODE_NAME 		getUIDListWhereMarkLowerThan
	 * @DESCRIPTION 		return a list of users uid if their mark is lower than criterion
	 * @param criterion
	 * @return
	 * @throws DBException
	 */
	public static List<String> getUIDListWhereMarkLowerThan(double criterion) throws DBException {
		List<String> list=new ArrayList<>();
		CSRShuttleBus csr = CRUD.CRUDPull(
				"SELECT * FROM "+businessTable+" WHERE mark <="+criterion+";"); //group by uid TODO
		try { while (csr.getResultSet().next())
			list.add(csr.getResultSet().getString("uid"));
		csr.close();}
		catch (SQLException e) {throw new DBException(
				"getUIDListWhereMarkGreaterThan SQL Error: " + e.getMessage());}
		return list;
	}
}