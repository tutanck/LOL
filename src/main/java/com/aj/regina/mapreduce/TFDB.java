package com.aj.regina.mapreduce;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

import db.mongo.PostsDB;
import db.sqldb.creator.tabledefs.TFDef;
import db.tools.DBConnectionManager;
import db.tools.DBToolBox;
import db.tools.DbException;
import db.tools.QueryString;

/**
 * @author AJoan
 * @see https://fr.wikipedia.org/wiki/TF-IDF
 * Principe : TF-IDF
 *  TF (Term Frequency) 
 *  IDF (Inverse Document Frequency) 
Le score de pertinence est obtenue par une ponderation des termes
appartenant au document d et a la requete q :
Si une requete contient le terme T, un document a d'autant plus
de chances d'y repondre qu'il contient ce terme et que la frequence du
terme au sein du document (TF) est grande. Neanmoins, si le
terme T est lui-meme tres frequent au sein du corpus, c'est-a-dire
qu'il est present dans de nombreux documents (e.g. les articles
definis - le, la, les), il est en fait peu discriminant. 
TF : Term Frequency
Soit un document d et un mot w, on definit tf (d,w) par :
tf (d,w) = frequence de w dans d.
IDF : Inverse Document Frequency
Soit un mot w, on definit idf (w) par :
idf (w) = log ( |D| / |dj : w in dj|)
ou |D| =  nombre total de documents dans le corpus�
& dj nombre de documents ou le terme(mot) w apparait 
TF-IDF : 
Le poids d'un mot dans un document est defini par :
tf-idf(w,d)=tf(w,d)*idf (w)
 */


public class TFDB{

	/**Mongo*/
	static DBCollection collection = PostsDB.collection;

	/**SQL*/
	static String bd=TFDef.table;

	/**wifi*/
	private static String wifi = "desc"; //WIFI : WorkItemFieldInput


	/**
	 * Term Frequency BackEnd work
	 * @return
	 * @throws JSONException */
	private static List<JSONObject> TFList() throws JSONException {
		/**Mongo Side : MR (MapReduce) program to define what to do in database
		 * map is a function applied on each document (here post) of the collection
		 * => 'this' represent the current document being analyzed
		 * first get workItem field from document(on mongoDB)
		 * then you need a workItem text values standardization : Upper/lower to
		 * lower only 
		 * then you also need a workItem text values standardization : evict 
		 * non-digit/non-word
		 * Finally we choose to do a triple output emit(docID,Word,1(emit value))
		 * here key is a couple (docID,Word) & its value is 1*/
		//TODO Gerer les mots accentue et et ameliorer le filtre de separation (ne va pas du tout)
		String map ="function(){var cont=this."+wifi+";cont=cont.toLowerCase();"
				+ "var Words=cont.split("+QueryString.tfdfpattern+");"
				+ "for(var i in Words){if(Words[i]!=\"\")"
				+ "emit({mongodocid:this._id,word:Words[i]},1);}}";

		/**Reduce is a function applied on each (Key,value) result that shuffle
		 * words by grouping identical values key and apply a treatment on these
		 *  common values key  */
		String reduce = "function(key,values){total=0;"
				+ "for(var i in values){total+=values[i];}return total}";

		MapReduceOutput out = collection.mapReduce(
				new MapReduceCommand(collection, map, reduce,null
						, MapReduceCommand.OutputType.INLINE, null));

		List<JSONObject> l = new ArrayList<JSONObject>();
		for (DBObject o : out.results()) 	
			//TFrequency total value
			//object_id of triple entry table 
			l.add(new JSONObject()
					.put("tf",o.get("value"))
					.put("key",o.get("_id")));
		return l;}



	/**
	 * Filling of the SQL TF table 
	 * @throws JSONException
	 * @throws DBException */
	public static void updateTF() throws  JSONException, DBException {
		try {
			Connection c = DBConnectionManager.getMySQLDBConnection();
			//Emptying the table
			Statement ds = c.createStatement();
			ds.executeUpdate("DELETE FROM "+bd+" ;");
			ds.close();
			List<JSONObject> l = TFList(); //Get Term Frequency list
			for (int i=0;i<l.size();i++) {
				BasicDBObject key = (BasicDBObject)l.get(i).get("key");
				// updating indexes with TF total value for an document entry(docID,word)
				String query = "INSERT INTO "+bd+" VALUES ('"+key.getString("word")+"','"
						+((ObjectId)key.get("mongodocid")).toString()+"',"
						+l.get(i).getInt("tf")+",NOW());";
				Statement is = c.createStatement();
				is.executeUpdate(query);
				is.close();	
			}c.close();}
		catch (SQLException e){
			throw new DBException("Error while filling Term Frequency table ("+bd+") :" 
					+ DBToolBox.getStackTrace(e));}}	


	public static void main(String[] args) throws JSONException, DBException {
		System.out.println("** "+TFList()+" **");
		updateTF();}

}