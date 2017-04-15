package mood.api.services;

import java.util.HashMap;
import java.util.Map;

import db.sqldb.creator.tabledefs.UsersDef;
import db.sqldb.regina.THINGS;
import services.tools.MapRefiner;
import tools.db.DBException;

/**
 * @author AJoan */
public class ApiRegister {

	private static String table=UsersDef.table;
	private static String caller=ApiRegister.class.getName();

	public static boolean registr(String apiUsername){
		Map<String, String> airbus=new HashMap<>();
		airbus.put("username",apiUsername);

		//check if api's username exists
		try {
			if(THINGS.matchTHINGS(airbus,table,caller))
				return true;//api already exists in db

			//generate uid for this api
			Map<String,String>uidMap=new HashMap<>();
			String uid;
			do{
				uidMap.clear(); //Reset uidMap
				uid=db.tools.DBToolBox.generateMD5ID();
				uidMap.put("uid",uid);
			}while(THINGS.matchTHINGS(uidMap, table, caller));		

			//add requirements for api to be recognized as user 
			airbus.put("uid", uid);
			airbus.put("status", "confirmed");

			//add user in database
			THINGS.addTHINGS(JSONRefiner.subMap(airbus,new String[]{
					"uid","username","status"}),table,caller);
		} catch (DBException e) {e.printStackTrace(); return false;}

		return true;}

}
