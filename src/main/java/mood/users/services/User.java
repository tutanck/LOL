package mood.users.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.aj.jeez.tools.MapRefiner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mood.users.db.UserPlacesProfileDB;
import mood.friends.db.FriendDB;
import mood.users.db.UserDB;
import mood.users.utils.InputType;
import regina.AbsentKeyException;
import regina.JSONRefiner;
import regina.THINGS;
import tools.db.DBToolBox;
import tools.general.PatternsHolder;
import tools.db.DBException;
import tools.services.ServiceCodes;
import tools.mailing.SendEmail;
import tools.services.ServiceCaller;
import tools.services.ServicesToolBox;
import tools.services.ShouldNeverOccurException;
import tools.lingua.Lingua;
import tools.lingua.StringNotFoundException;

/**
 * @author AJoan
 * Service classes are much more meaningful now , because DB access is automatic
 * This classes will take more significant decision on how their process and dispatch incoming data
 * to DB instead of just forwarding the DataBus as fast as possible without proper inspection.*/
public class User{

	private static String caller=User.class.getName();
	private static DBCollection collection = UserDB.collection;

	/**
	 * @description 
	 * Users registration service : register a new user
	 * @param map
	 * @return
	 * @throws DBException 
	 * @throws JSONException 
	 * @throws ShouldNeverOccurException 
	 * @throws AbsentKeyException */
	public static JSONObject registration(
			JSONObject params
			) throws DBException, JSONException, ShouldNeverOccurException, AbsentKeyException {

		String nexturl="/Momento/signin.jsp";

		if(!PatternsHolder.isValidWord(params.getString("username")))
			return ServicesToolBox.alert(ServiceCodes.INVALID_USERNAME_FORMAT);

		if(THINGS.exists(JSONRefiner.slice(params,new String[]{"username"}),collection,caller))
			return ServicesToolBox.alert(ServiceCodes.USERNAME_IS_TAKEN);

		if(!PatternsHolder.isValidEmail(params.getString("email")))
			return ServicesToolBox.alert(ServiceCodes.INVALID_EMAIL_FORMAT);

		if(THINGS.exists(JSONRefiner.slice(params,new String[]{"email"}),collection,caller))
			return ServicesToolBox.alert(ServiceCodes.EMAIL_IS_TAKEN);

		//TODO check pass format

		THINGS.add(JSONRefiner.slice(params,
				new String[]{"username","pass","email"})
				.put("confirmed", false)
				.put("regdate", new Date()),
				collection,caller);

		/*try { TODO uncomment
			SendEmail.sendMail(
					params.get("email"),
					Lingua.get("welcomeMailSubject","fr-FR"),
					Lingua.get("welcomeMailMessage","fr-FR")
					+basedir+"accountconfirmation?ckey="+params.get("uid"));
		}catch (StringNotFoundException e) { 
			System.out.println("Dictionary Error : Mail not send");
			e.printStackTrace();
		} */

		return ServicesToolBox.answer(
				new JSONObject()
				.put("nexturl",nexturl),				
				ServiceCaller.whichServletIsAsking().hashCode());
	}


	/**
	 * @description  Users login service : Connects user into online mode
	 * @param params
	 * @return
	 * @throws DBException 
	 * @throws JSONException  
	 * @throws ShouldNeverOccurException 
	 * @throws AbsentKeyException */
	public static JSONObject login(
			JSONObject params
			) throws DBException, JSONException, ShouldNeverOccurException, AbsentKeyException {

		String nexturl="/Momento/momento.jsp";

		DBObject user;

		switch (PatternsHolder.determineFormat(params.getString("username"))) {

		case EMAIL:
			System.out.println("input format : "+InputType.EMAIL);//Debug

			Map<String,String> usernameToEmail=new HashMap<>();
			usernameToEmail.put("username","email");
			JSONObject byEmail= JSONRefiner.renameJSONKeys(params,usernameToEmail);

			if (THINGS.exists(JSONRefiner.slice(
					byEmail,new String[]{"email","pass"}),collection,caller))
				user = THINGS.getOne(JSONRefiner.slice(	
						byEmail,new String[]{"email"}),collection,caller);
			else return ServicesToolBox.alert(ServiceCodes.WRONG_LOGIN_PASSWORD);
			break;

		case NUMS:
			System.out.println("input format : "+InputType.NUMS);//Debug

			Map<String,String> usernameToPhone=new HashMap<>();
			usernameToPhone.put("username","phone");
			JSONObject byPhone= JSONRefiner.renameJSONKeys(params,usernameToPhone);

			if (THINGS.exists(JSONRefiner.slice(
					byPhone,new String[]{"phone","pass"}),collection,caller))
				user= THINGS.getOne(JSONRefiner.slice(
						byPhone,new String[]{"phone"}),collection,caller);
			else return ServicesToolBox.alert(ServiceCodes.WRONG_LOGIN_PASSWORD);
			break;	 

		case AWORD:
			if(THINGS.exists(JSONRefiner.slice(
					params,new String[]{"username", "pass"}),collection, caller))
				user = THINGS.getOne(JSONRefiner.slice(
						params,new String[]{"username"}),collection,caller);
			else return ServicesToolBox.alert(ServiceCodes.WRONG_LOGIN_PASSWORD);
			break;

		default:
			System.out.println("input format : "+InputType.UNKNOWN);//Debug
			return ServicesToolBox.alert(ServiceCodes.INVALID_USERNAME_FORMAT);
		}

		if(!THINGS.exists(new JSONObject()
				.put("_id", (String) user.get("_id"))
				.put("confirmed", true)
				,collection, caller))
			return ServicesToolBox.alert(ServiceCodes.USER_NOT_CONFIRMED);

		//create or recover session and get current (new or old)sessionKey
		//String sessionKey =SessionManager.session(uid); TODO 

		return ServicesToolBox.answer(
				new JSONObject()
				.put("nexturl",nexturl)
				//.put("skey",sessionKey)TODO
				.put("username",user.get("username")),
				ServiceCaller.whichServletIsAsking().hashCode());
	}




	/**
	 * @description update user's profile
	 * @param map
	 * @return
	 * @throws DBException 
	 * @throws JSONException */
	public static JSONObject updateProfile(JSONObject params) throws DBException, JSONException {
		String nexturl="/Momento/showprofile";

		String _id= SessionManager.sessionOwner(params.get("skey"));

		if(params.has("email") && THINGS.exists(JSONRefiner.slice(params,
				new String[]{"email"})
				.put("_id",
						new JSONObject()
						.put("$ne",_id) )
				,collection,caller))
			return ServicesToolBox.alert(ServiceCodes.EMAIL_IS_TAKEN);

		if(params.has("phone") && THINGS.exists(JSONRefiner.slice(params,
				new String[]{"phone"})
				.put("_id",
						new JSONObject()
						.put("$ne",_id) )
				,collection,caller))
			return ServicesToolBox.alert(ServiceCodes.PHONE_IS_TAKEN);

		//Branch the json (dissociate) like separating the yolk from the egg white
		List<JSONObject> node = JSONRefiner.branch(params, new String[]{"skey","places"});	

		//Update of user profile in users collection
		THINGS.putOne(new JSONObject().put("_id", _id),node.get(1),collection,caller);
		//Update of user profile in Mongo database
		//service return is ignored(internal call)
		UserPlacesProfile.updatePp(_id, node.get(0).get("places")); 

		return ServicesToolBox.answer(
				new JSONObject()
				.put("nexturl",nexturl),
				ServiceCaller.whichServletIsAsking().hashCode());
	}




	/**
	 * @description return user's complete profile information 
	 * @param map
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject getProfile(JSONObject params) throws DBException, JSONException {	
		String _id = SessionManager.sessionOwner(params.get("skey"));

		JSONObject clean = JSONRefiner.clean(params, new String[]{"skey"});
		//Trick : like fb, an user can see his profile as someone else
		//uther as a contraction of user-other (other user)
		if(params.has("uther")) 
			clean.put("_id", params.get("uther"));
		else
			clean.put("_id",_id);

		DBObject user=  THINGS.getOne(clean, collection, caller);
		return ServicesToolBox.answer(
				new JSONObject()
				.put("username",user.get("username"))
				.put("email",user.get("email"))
				.put("firstname",user.get("firstname"))
				.put("lastname",user.get("lastname"))
				.put("birthdate",user.get("birthdate"))
				.put("phone",user.get("phone"))
				.put("places",UserPlacesProfileDB.getPp(_id)),
				ServiceCaller.whichServletIsAsking().hashCode());
	}





	/**
	 * @description return username , firstname and lastname, etc 
	 * @param uid
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject getShortInfos(JSONObject params) throws DBException, JSONException {
		//Here branch is used to slurp url_parameters (to evict skey)
		List<Map<String,String>> node = MapRefiner.branch(params, new String[]{"skey"});
		//uther as a contraction of user-other (other user)
		node.get(0).put("uid", params.get("uther"));
		CSRShuttleBus dataSet = CRUD.CRUDPull(THINGS.getTHINGS(
				MapRefiner.subMap(node.get(0),new String[]{"uid"}),table));
		ResultSet rs=dataSet.getResultSet();
		try {
			if(rs.next())
				return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI
						,new JSONObject()
						.put("username",rs.getString("username"))
						.put("firstname",rs.getString("firstname"))
						.put("lastname",rs.getString("lastname"))
						,null,ServiceCaller.whichServletIsAsking().hashCode());
			else throw new DBException("@User/getUsername :"
					+ " Database is inconsistant : user infos must exit for given skey!");
		} catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		finally {dataSet.close();}}


	public static JSONObject searchUser(JSONObject params) 
			throws DBException, JSONException {
		CSRShuttleBus dataSet=UserDB.searchUser(SessionManager.sessionOwner(skey),query);
		JSONArray jar=new JSONArray();
		ResultSet rs=dataSet.getResultSet();
		try {
			while(rs.next()){
				String type=FriendDB.status(
						SessionManager.sessionOwner(skey),rs.getString("uid"));	
				jar.put(new JSONObject()
						.put("uid",rs.getString("uid"))
						.put("type",(type==null)?"user":type)
						.put("username",rs.getString("username"))
						.put("firstname",rs.getString("firstname"))
						.put("lastname",rs.getString("lastname")));}

			return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
					new JSONObject().put("users",jar), 
					null, ServiceCaller.whichServletIsAsking().hashCode());
		}catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		finally {dataSet.close();}}


	/**
	 * @description send an email with MD5 generated temporary access key for access recover to the user
	 * @param params
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject accessRecover(JSONObject params) throws DBException, JSONException {
		String nexturl="/Momento/signin.jsp";
		//speedy bus for data set map transportation  (email)
		Map<String,String>swiftBus2 = new HashMap<>();
		swiftBus2.put("email", params);
		System.out.println("swiftBus2 : "+swiftBus2);

		//Verify if user email exists
		if(!THINGS.matchTHINGS(swiftBus2,table,caller))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD
					,null,"Unknown email address !",ServiceCodes.UNKNOWN_EMAIL_ADDRESS);

		//Generate temporary key (sequence of 32 hexadecimal digits) using MD5 hashes algorithm 
		String secret = DBToolBox.generateMD5ID();

		//speedy bus for data set map transportation  (password)
		Map<String,String>swiftBus = new HashMap<>();
		swiftBus.put("pass", secret);

		//TODO verif pk swiftbus2 s'efface
		System.out.println("swiftBus2 : "+swiftBus2);
		swiftBus2.put("email", params);
		System.out.println("swiftBus2 : "+swiftBus2);
		//Update of user profile in database (reset password : strong security policy)
		THINGS.updateTHINGS(swiftBus,swiftBus2,table,caller);

		//Send an email to the applicant
		try {SendEmail.sendMail(params,Lingua.get("NewAccessKeySentSubject","fr-FR"),
				Lingua.get("NewAccessKeySentMessage","fr-FR")+ secret);}
		catch (StringNotFoundException e) { 
			System.out.println("Dictionary Error : Mail not send");
			e.printStackTrace();}
		return ServicesToolBox.reply(ServiceCodes.STATUS_GOODnBAD,
				new JSONObject()
				.put("nexturl",nexturl)				
				,"We just sent you an email with intructions to reset your password.",
				ServiceCaller.whichServletIsAsking().hashCode());}


	/**
	 * @description  Users logout service : Disconnects user from online mode
	 * @param params
	 * @return
	 * @throws DBException 
	 * @throws JSONException */
	public static JSONObject logout(JSONObject params) throws DBException, JSONException {
		String nexturl="/Momento/signin.jsp";
		SessionManager.closeSession(params.get("skey"));
		return ServicesToolBox.answer(
				new JSONObject()
				.put("nexturl",nexturl),
				ServiceCaller.whichServletIsAsking().hashCode());
	}

	/**
	 * @description confirm a user account (email is verified)
	 * @param uid
	 * @return 
	 * @throws ShouldNeverOccurException
	 * @throws DBException 
	 * @throws JSONException */
	public static JSONObject confirmUser(JSONObject params) throws ShouldNeverOccurException, DBException, JSONException{ 
		String nexturl="/Momento/signin";
		if(!UserDB.uidExists(params))
			throw new ShouldNeverOccurException("SNO Error : User ID is unknown!");
		UserDB.confirmUser(params);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject()
				.put("nexturl",nexturl)				
				,null,ServiceCaller.whichServletIsAsking().hashCode());}



	public static void main(String[] args) throws DBException, JSONException {
		/*Map<String, String[]> test=new HashMap<>();
		test.put("username", new String[]{"louis","hd"});
		test.put("pass", new String[]{"fearness12","tove"});
		test.put("email", new String[]{"tutanck@gmail.com"});
		test.put("uid", new String[]{"hardtofindeuid"});
		test.put("skey", new String[]{"hardtobrokekey"});
		//		System.out.println("registration : "+registration(test)+"\n");
		//System.out.println("login : "+login(test)+"\n");
		//System.out.println("updateProfile : "+updateProfile(test)+"\n");
		System.out.println("accessRecover : "+accessRecover("tutanck@gmail.com")+"\n");
		//System.out.println("logout : "+logout(test)+"\n");*/
		searchUser("h","j");
	}

}
