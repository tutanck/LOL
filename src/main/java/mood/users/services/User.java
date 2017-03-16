package mood.users.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import fr.aj.jeez.tools.MapRefiner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mood.users.db.UserPlacesProfileDB;
import mood.friends.db.FriendDB;
import mood.users.db.UserDB;
import regina.JSONRefiner;
import regina.THINGS;
import tools.db.DBToolBox;
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
	 * @throws ShouldNeverOccurException */
	public static JSONObject registration(
			JSONObject params
			) throws DBException, JSONException, ShouldNeverOccurException {

		String nexturl="/Momento/signin.jsp";

		//check if username exists
		if(THINGS.exists(JSONRefiner.slice(
				params,
				new String[]{"username"}),
				collection,caller))
			return ServicesToolBox.alert(
					"This username is taken!",
					ServiceCodes.USERNAME_IS_TAKEN);

		//check if email is used
		if(THINGS.exists(JSONRefiner.slice(
				params,
				new String[]{"email"}),
				collection,caller))
			return ServicesToolBox.alert(
					"This email address is used!",
					ServiceCodes.EMAIL_IS_TAKEN);

		//add user in database
		THINGS.add(JSONRefiner.slice(
				params,
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
	 * @throws JSONException  */
	public static JSONObject login(
			JSONObject params
			) throws DBException, JSONException {

		String nexturl="/Momento/momento.jsp";

		DBObject user =null;

		//check username & password existence and compatibility
		if(THINGS.exists(JSONRefiner.slice(
				params,
				new String[]{"username", "pass"}), 
				collection, caller)){

			System.out.println(1);//Debug

			//Get uid (user ID) from associated username
			user = THINGS.getOne(JSONRefiner.slice(
					params,new String[]{"username"}),
					collection,caller);
		}
		//otherwise check email & password existence and compatibility
		else{
			Map<String,String> usernameToEmail=new HashMap<>();
			usernameToEmail.put("username","email");

			JSONObject byEmail =  
					JSONRefiner.renameJSONKeys(
							params,
							usernameToEmail);

			if (THINGS.exists(JSONRefiner.slice(
					byEmail,
					new String[]{"email","pass"}),
					collection,caller)){
				System.out.println(2);//Debug

				//Get uid (user ID) from associated email
				user = THINGS.getOne(JSONRefiner.slice(
						byEmail,
						new String[]{"email"}),
						collection,caller);

			}
			//otherwise check phone & password existence and compatibility
			else{
				Map<String,String> usernameToPhone=new HashMap<>();
				usernameToPhone.put("username","phone");

				JSONObject byPhone=  
						JSONRefiner.renameJSONKeys(
								params,
								usernameToPhone);

				if (THINGS.exists(JSONRefiner.slice(
						byPhone,
						new String[]{"phone","pass"}),
						collection,caller)){

					System.out.println(3);//Debug
					//Get uid (user ID) from associated phone number
					user= THINGS.getOne(JSONRefiner.slice(
							byPhone,
							new String[]{"phone"}),
							collection,caller);

				}
				else return ServicesToolBox.alert(
						"Wrong login or password !",
						ServiceCodes.WRONG_LOGIN_PASSWORD);
			}
			String uid=(String) user.get("_id");

			if(!UserDB.isConfirmed(uid))
				return ServicesToolBox.alert(
						"You have not confirmed your account!",
						ServiceCodes.USER_NOT_CONFIRMED);

			System.out.println("UID="+uid);//Debug

			//create or recover session and get current (new or old)sessionKey
			String sessionKey =SessionManager.session(uid);

			return ServicesToolBox.answer(
					new JSONObject()
					.put("nexturl",nexturl)
					.put("skey",sessionKey)
					.put("username",UserDB.getUsernameById(uid)),
					ServiceCaller.whichServletIsAsking().hashCode());
		}
	}



	/**
	 * @description update user's profile
	 * @param map
	 * @return
	 * @throws DBException 
	 * @throws JSONException */
	public static JSONObject updateProfile(JSONObject params) throws DBException, JSONException {
		String nexturl="/Momento/showprofile";

		//check if new email is used
		String emailIsTaken="Select * from "+table+" where email='"+params.get("email")+"' "
				+ "AND username <> '"+params.get("username")+"' ;";
		if(CRUD.CRUDCheck(emailIsTaken, caller))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD,null,
					"Email already used!",ServiceCodes.EMAIL_IS_TAKEN);

		//check if new email is used
		String phoneIsTaken="Select * from "+table+" where phone='"+params.get("phone")+"' "
				+ "AND username <> '"+params.get("username")+"' ;";
		if( CRUD.CRUDCheck(phoneIsTaken, caller))
			return ServicesToolBox.reply(ServiceCodes.STATUS_BAD,null,
					"Phone Number already used!",ServiceCodes.EMAIL_IS_TAKEN);

		//Branch the map (dissociate) like separating the yolk from the egg white
		List<Map<String,String>> node = MapRefiner.branch(params, new String[]{"skey","places"});	

		//get user ID corresponding to current session 
		String skey=node.get(1).get("skey");
		String uid = SessionManager.sessionOwner(skey);
		if(uid==null)
			throw new 
			DBException("@User/updateProfile : Database is inconsistant :"
					+ " uid must exit for given skey!");

		//speedy bus for dataset map transportation  
		Map<String,String>swiftBus = new HashMap<>();
		swiftBus.put("uid", uid);

		//Update of user profile in SQL database
		THINGS.updateTHINGS(node.get(0),swiftBus,table,caller);
		//Update of user profile in Mongo database
		//service return is ignored(internal call)
		UserPlacesProfile.updatePp(skey, node.get(1).get("places")); 

		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
				new JSONObject()
				.put("nexturl",nexturl)
				,null,ServiceCaller.whichServletIsAsking().hashCode());}




	/**
	 * @description return user's complete profile information 
	 * @param map
	 * @return
	 * @throws DBException
	 * @throws JSONException */
	public static JSONObject getProfile(JSONObject params) throws DBException, JSONException {	
		//Here branch is used to slurp url_parameters (to evict skey)
		List<JSONObject> node = JSONRefiner.branch(params, new String[]{"skey"});

		//Trick : like fb, an user can see his profile as someone else
		//uther as a contraction of user-other (other user)
		if(params.containsKey("uther")) 
			node.get(0).put("uid", params.get("uther"));
		else
			node.get(0).put("uid", SessionManager.sessionOwner(node.get(1).get("skey")));

		CSRShuttleBus dataSet = CRUD.CRUDPull(THINGS.getTHINGS(
				MapRefiner.subMap(node.get(0),new String[]{"uid"}),table));
		ResultSet rs=dataSet.getResultSet();
		try {
			if(rs.next())
				return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI,
						new JSONObject()
						.put("username",rs.getString("username"))
						.put("email",rs.getString("email"))
						.put("firstname",rs.getString("firstname"))
						.put("lastname",rs.getString("lastname"))
						.put("birthdate",rs.getString("birthdate"))
						.put("phone",rs.getString("phone"))
						.put("places",UserPlacesProfileDB.getPp(
								node.get(0).get("uid")))
						,null,ServiceCaller.whichServletIsAsking().hashCode());
			else throw new 
			DBException("@User/getProfile : Database is inconsistent :"
					+ " user infos must exit for given skey!");}
		catch (SQLException e) {throw new DBException(DBToolBox.getStackTrace(e));}
		finally {dataSet.close();}}




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
