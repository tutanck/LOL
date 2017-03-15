package mapreduce;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoException;

import db.mongo.PostsDB;
import db.sqldb.creator.tabledefs.DFDef;
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
idf (w) = log ( |D| / |dj : W in dj|)
ou |D| =  nombre total de documents dans le corpus�
& dj nombre de documents ou le terme(mot) w apparait 
TF-IDF : 
Le poids d'un mot dans un document est defini par :
tf-idf(w,d)=tf(w,d)*idf (w)
*/


public class DFDB{

	/**Mongo*/
 	static DBCollection collection = PostsDB.collection;

	/**SQL*/
	static String bd=DFDef.table;
	
	/**wifi*/
	private static String wifi = "desc"; //WIFI : WorkItemFieldInput
	
	/**
	 * Document Frequency BackEnd work
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 * @throws JSONException */
	private static List<JSONObject> DFList() throws  JSONException {
		/**Mongo Side : MR (MapReduce) program to define what to do in database
		 * map is a function applied on each document (here post) of the collection
		 * => 'this' represent the current document being analyzed
		 * first get workItem field from document(on mongoDB)
		 * then you need a workItem text values standardization : Upper/lower to
		 * lower only 
		 * then you also need a workItem text values standardization : evict 
		 * non-digit/non-word
		 **********Filter method :
		 * The filter() method creates and returns a new array containing all
		 *  elements of the original array for which the callback function 
		 *  returns true (@see Mozzilla developpers)
		 * The Filter() calls the callback function provided for each element of an array,
		 *  and constructs a new array for all items for which the callback call
		 *  returns true or a value equivalent to true in a boolean context
		 * The callback function (or predicate) applied to each element of the array.
		 *  This function is called with the following arguments: 
		 *  *element (The element to be treated)
		 *  *index (The element's index)
		 *  This function returns true if the item should be retained 
		 *  for the result table and false otherwise. 
		 *  Return : A new array containing the elements that meet the filter condition
		 *  (here return the entire array because callback condition is always satisfied)
		 *  ******************************************************************************
		 *  Finally emit double output(word,1(emit value))
		 *  here key is a Word & its value is 1*/
		//TODO Gerer les mots accentue et et ameliorer le filtre de separation (ne va pas du tout)  
		String map ="function () {var cont = this."+wifi+";"
				+ "cont = cont.toLowerCase();"
				+ "var Words = cont.split("+QueryString.tfdfpattern+");"
				+ "dictionary = Words.filter(function(elem,pos) "
				+ "{return Words.indexOf(elem)==pos;});"
				+ "for(var i in dictionary){if(dictionary[i]!=\"\")"
				+ "emit(dictionary[i],1);}}";

		/**Reduce is a function applied on each (Key,value) result that shuffle
		 * words by grouping identical values key and apply a treatment on these
		 *  common values key  */
		String reduce = "function(key,values){total=0;"
				+ "for(var i in values){total+=values[i];}return total}";

		MapReduceOutput out = collection.mapReduce(
				new MapReduceCommand( collection, map, reduce,null,
						MapReduceCommand.OutputType.INLINE, null ));

		List<JSONObject> l = new ArrayList<JSONObject>();
		for (DBObject o:out.results())
			//dFrequency total value		
			//object_id of double entry table
			l.add(new JSONObject()
					.put("df",o.get("value"))
					.put("word", o.get("_id")));
		return l;}
	
	

	/**
	 * Filling of the SQL DF table 
	 * @throws JSONException
	 * @throws DBException */
	public static void updateDF() throws JSONException, DBException {
		List<JSONObject> l = DFList(); //Get Document Frequency list
		try {
			Connection c = DBConnectionManager.getMySQLDBConnection();
			//Emptying the table
			Statement ds = c.createStatement();
			ds.executeUpdate("DELETE FROM "+bd+" ;");
			ds.close();
			//TODO use crud pull
			for (int i=0;i<l.size();i++){
				//df total value for an document entry(docID,word)
				String query = "INSERT INTO "+bd+" VALUES ('"+l.get( i ).getString("word")+"',"
				+l.get( i ).getInt("df")+",NOW());";
				Statement is = c.createStatement();
				is.executeUpdate(query);
				is.close();	
			}c.close();}
		catch (SQLException e){
			throw new DBException("Error while filling Document Frequency table ("+bd+") : " 
					+ DBToolBox.getStackTrace(e));}}	
	
	
  	public static void main(String[] args) throws JSONException, DBException {		 
			System.out.println("** "+DFList()+" **");
			updateDF();}
}