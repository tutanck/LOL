package mood.messenger.db;

import java.util.Arrays;
import java.util.Date;

import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;

import db.tools.BasicDBListFI;
import db.tools.DBConnectionManager;
import db.tools.DbException;

public class MessengerDB {

	private static DBCollection collection = DBConnectionManager.getMongoDBCollection("Messenger");

	/**
	 * Add a new message between two users 
	 * @param sender
	 * @param recipient
	 * @param message
	 * @throws DBException*/
	public static void newMessage(String sender,String recipient,String message) {
		collection.insert(
				new BasicDBObject()
				.append("sender",sender)
				.append("recipient",recipient)
				.append("message",message)
				.append("date",new Date()));}

	/**
	 * Chronological shuffled List of message between two users (A & B)
	 * @param talkerA
	 * @param talkerB
	 * @return
	 * @throws DBException */
	public static DBCursor messages(String talkerA, String talkerB){  
		return collection.find(
				new BasicDBObject()
				.append("$or",BasicDBListFI.addAll(Arrays.asList(new BasicDBObject[]{
						new BasicDBObject()
						.append("sender",talkerB)
						.append("recipient",talkerA)
						,new BasicDBObject()
						.append("sender",talkerA)
						.append("recipient",talkerB)}))));}
	
	/**
	 * Chronological shuffled List of message involving user
	 * @param userID
	 * @return
	 * @throws DBException */
	public static DBCursor messages(String userID){  
		return collection.find(
				new BasicDBObject()
				.append("$or",BasicDBListFI.addAll(Arrays.asList(new BasicDBObject[]{
						new BasicDBObject()
						.append("sender",userID)
						,new BasicDBObject()
						.append("recipient",userID)}))));}
	
	public static void main(String[] args) throws MongoException, DBException, JSONException {
//		MessengerDB.collection.drop();//reset : determinism required for tests
//		newMessage("lola58", "jo42", "ohayo jo");
//		newMessage("jo42", "lola58", "kombawa lola");
//		System.out.println(messages("lola58", "jo42"));
		System.out.println(messages("d910952c404b4b6cca5d6f61a5ab9df0"));
	}
	
}