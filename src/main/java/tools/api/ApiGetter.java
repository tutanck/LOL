package tools.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;

public class ApiGetter {

	/**URL pattern : 
	 * "http://data.iledefrance.fr/api/records/1.0/search/?
	 * dataset=evenements-publics-cibul&
	 * facet=updated_at&
	 * facet=tags&
	 * facet=department&
	 * facet=region&
	 * facet=city&
	 * facet=date_start&
	 * facet=date_end&
	 * pretty_print=true&
	 * geofilter.distance="+lat+"%2C+"+lon+"%2C"+rayon+"&rows="+row
	 * Get json data from url about api's knowledge on a place
	 * @param url
	 * @param lat
	 * @param lon
	 * @param radius
	 * @param rows
	 * @return */
	public static JSONObject getJSONData(String url, double lat, double lon,
			double radius, int rows){
		try {return new JSONObject(readUrl(
				url+lat+"%2C+"+lon+"%2C"+radius+"&rows="+rows));}
		catch (Exception e) {e.printStackTrace();return null;}}

	
	/**
	 * Just read raw string data from url  
	 * @param urlString
	 * @return
	 * @throws Exception */
	public static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new URL(urlString).openStream())
					);
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read); 
			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
