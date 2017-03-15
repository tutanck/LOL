package mapreduce;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import db.tools.DBConnectionManager;
import db.tools.DBToolBox;
import db.tools.DbException;
import db.tools.QueryString;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author AJoan */
public class MapReduce {

	private static String dfdb=DFDB.bd;
	private static String tfdb=TFDB.bd;	

	public static List<ObjetRSV> pertinence(String query,DBCursor cursor) throws DBException {
		Set<String> querywords =QueryString.wordSet(query,QueryString.mrpattern);	
 		List<ObjetRSV> results = new ArrayList<ObjetRSV>();
		try{
			Connection sqldbconect = DBConnectionManager.getMySQLDBConnection();
			while(cursor.hasNext()){
				DBObject doc = cursor.next();
				String docID = ((ObjectId)doc.get("_id")).toString();

				//Calculation of the post's score
				Double score = 0.0;
				for(String word : querywords){
					//Recovering the word's tf
					Statement s = sqldbconect.createStatement();
					ResultSet rs = s.executeQuery("SELECT tf FROM "+tfdb+" WHERE"
							+ " word = \""+word+"\" AND docID=\""+ docID +"\"");
					if(!rs.next()) //If the word is unknown in the local document dictionary 
						continue; //continue with the next word
					double tf =rs.getDouble("tf");
					s.close();

					//Recovering the word's df
					s = sqldbconect.createStatement();
					rs = s.executeQuery("SELECT df FROM "+dfdb+" WHERE word = \"" + word+"\"");
					rs.next();
					double df =rs.getDouble("df");
					s.close();
					/** Be aware : 
					 * DBCursor.count(): Counts the number of objects matching the query.
					 * This does not take limit/skip into consideration.
					 * DBCursor.size(): Counts the number of objects matching the query.
					 * This does take limit/skip into consideration*/
					score = score + tf * Math.log(cursor.count()/df); 
					/** This suppose cursor contains all the results without limit/skip
					 * Limit will be in the display function (client side) */
				}
				results.add(new ObjetRSV(doc,score));}			
		}catch(SQLException e){throw new DBException(DBToolBox.getStackTrace(e));}

		//results reverse sort
		Collections.sort(results,Collections.reverseOrder());
		List<ObjetRSV> pertinentResults=new ArrayList<ObjetRSV>();
		//Creation of the relevant results list (score > 0)
		for(ObjetRSV orsv : results)
			if(orsv.getRsv()>0)
				pertinentResults.add(orsv);	
		return pertinentResults;}
	
	
}