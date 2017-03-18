package mood.users.db;

import org.json.JSONObject;

import com.mongodb.DBCollection;

import fr.aj.regina.JSONRefiner;
import fr.aj.regina.THINGS;
import mood.users.services.User;
import tools.db.DBConnectionManager;

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
	
}