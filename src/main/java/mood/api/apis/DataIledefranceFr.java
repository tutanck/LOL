package mood.api.apis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import db.mongo.PostsDB;
import db.sqldb.business.UserDB;
import db.tools.ApiGetter;
import db.tools.ApiRegister;
import db.tools.DBToolBox;
import db.tools.DbException;

public class DataIledefranceFr {

	private static final String name=DataIledefranceFr.class.getSimpleName();
	private static final DBCollection collection = PostsDB.collection;
	private static String UID;
	private static boolean registred;
	
	private static String baseUrl="http://data.iledefrance.fr/api/records/1.0/search/?"
			+ "dataset=evenements-publics-cibul&facet=updated_at&facet=tags"
			+ "&facet=department&facet=region&facet=city&facet=date_start"
			+ "&facet=date_end&pretty_print=true&geofilter.distance=";

	/**
	 * Add a new post recovered from an external API in database 
	 * @param jsonObj
	 * @throws DbException 
	 * @throws JSONException */
	public static void storeApiAsPosts(double lat,double lon,int radius,int rows)
			throws DbException {
		if(!registred)
			registred=ApiRegister.registr(name);
		if(!registred) return; //try next time you will be called 

		UID= UserDB.getUidByUsername(name);
		
		BasicDBObject bdbo=new BasicDBObject();
		try{
			//get api's data
			JSONObject jsondata=ApiGetter.getJSONData(baseUrl,lat,lon,radius,rows);
			//System.out.println(jsondata+"\n\n\n\n\n");

			//records
			JSONArray records =  (JSONArray) jsondata.get("records");
			//System.out.println("recodrs : "+records);
			for (int i = 0; i < records.length(); i++) {

				//record
				JSONObject record = records.getJSONObject(i);

				//api's post identification informations
				String recordid=record.getString("recordid");
				String datasetid=record.getString("datasetid");
				String recordtimestamp=record.getString("record_timestamp");
				bdbo.append("authid",UID);
				bdbo.append("datasetid",datasetid);
				bdbo.append("recordid",recordid);
				bdbo.append("record_timestamp",recordtimestamp);

				//fields
				JSONObject fields =  (JSONObject) record.get("fields");

				//realtime :  date comparison
				if(fields.has("date_end")){
					Date currentDate =new Date();

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",// TODO HH:mm:ss later
							Locale.FRANCE);

					try {
						if(currentDate.compareTo(
								sdf.parse(fields.getString("date_end")))>0){
							/*System.out.println("Event "+recordid+" is outdated: "
								+fields.getString("date_end"));*/continue;}

						if(fields.has("date_start"))
							if(currentDate.compareTo(
									sdf.parse(fields.getString("date_start")))<0){
								/*System.out.println("Event "+recordid+" is to come up: "
									+fields.getString("date_start"));*/continue;}
					} catch (ParseException e) {e.printStackTrace(); 
					//skip this problematic record
					continue;}
				}

				//post's position
				if(fields.has("latlon")){
					JSONArray latlon = fields.getJSONArray("latlon");
					bdbo.append("lat",latlon.getDouble(0));
					bdbo.append("lon",latlon.getDouble(1));
				}

				//post's description
				//' <br> ' br because \n is not interpreted by the navigator 
				//and ' ' around br because mapreduce doesn't recogized it as a separator
				String desc="";
				if(fields.has("title"))
					desc+=fields.getString("title")+" <br> ";
				if(fields.has("description"))
					desc+=fields.getString("description")+" <br> ";
				if(fields.has("free_text"))
					desc+=fields.getString("free_text")+" <br> ";
				if(fields.has("placename"))
					desc+=fields.getString("placename")+" <br> ";
				if(fields.has("space_time_info"))
					desc+=fields.getString("space_time_info")+" <br> ";
				if(fields.has("pricing_info"))
					desc+= fields.getString("pricing_info")+" <br> ";
				if(fields.has("dist"))
					desc+="Distance :"+fields.getString("dist")+"m  <br> ";
				if(fields.has("tags"))
					desc+="Tags :"+fields.getString("tags")+" <br> ";
				if(fields.has("link"))
					desc+= "Website : "+fields.getString("link")+" <br> ";
				bdbo.append("desc",desc);

				//post's date
				if(fields.has("updated_at"))
					bdbo.append("date",fields.getString("updated_at"));
				else bdbo.append("date",recordtimestamp);


				//post's Admin informations
				if(fields.has("address"))
					bdbo.append("address",fields.getString("address"));
				if(fields.has("date_start"))
					bdbo.append("date_start",fields.getString("date_start"));
				if(fields.has("date_end"))
					bdbo.append("date_end",fields.getString("date_end"));
				if(fields.has("image")) //TODO let moldus access it
					bdbo.append("image", fields.getString("image"));
				if(fields.has("city"))
					bdbo.append("city", fields.getString("city"));
				if(fields.has("department"))
					bdbo.append("department", fields.getString("department"));				
				if(fields.has("lang"))
					bdbo.append("lang",fields.getString("lang"));
				if(fields.has("region"))
					bdbo.append("region",fields.getString("region"));
				if(fields.has("program_uid"))
					bdbo.append("program_uid",fields.getString("program_uid"));
				if(fields.has("uid"))
					bdbo.append("uid", fields.getString("uid"));
				//if(fields.has("timetable"))
				//bdbo.append("timetable", fields.getString("timetable"));

				//update in db
				collection.update(new BasicDBObject()
						.append("authid",UID)
						.append("recordid",recordid)
						.append("datasetid",datasetid)
						,bdbo,true,false);
				System.out.println("\n rom data.iledefrance.fr --: "+bdbo+"\n");
			}
		} catch (JSONException e) 
		{throw new DbException(DBToolBox.getStackTrace(e));}
	}

	public static void main(String[] args) throws DbException, JSONException {
		//storeApiAsPosts(48.8471036, 2.3574989999999616, 1000000, 500);}
		collection.remove(new BasicDBObject().append("authid", "data.iledefrance.fr"));}

}