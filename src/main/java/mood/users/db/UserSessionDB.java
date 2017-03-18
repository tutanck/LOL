package mood.users.db;

import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.aj.utils.AbsentKeyException;
import com.aj.utils.JSONRefiner;
import com.aj.regina.THINGS;
import mood.users.services.User;
import tools.db.DBConnectionManager;
import tools.db.DBException;

/**
 * @author AJoan */
public class UserSessionDB {

	public static DBCollection collection = DBConnectionManager.getMongoDBCollection("session");

	private static String caller=UserSessionDB.class.getName();

	public static String uid (
			String token,
			String did //deviceID
			){
		return (String) THINGS.getOne(
				new JSONObject()
				.put("skey", skey(token,did)), collection, caller
				).get("skey");
	}


	public static String skey(
			String token,
			String did //deviceID
			){
		return DigestUtils.sha1Hex(token+did);
	}
	
	public static boolean sessionExists(
			JSONObject params
			) throws DBException, AbsentKeyException{
		return THINGS.exists(
				JSONRefiner.slice(params,new String[]{"skey"}), UserSessionDB.collection);
	}
	
}