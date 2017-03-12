package mood.api.services;

import org.json.JSONException;
import org.json.JSONObject;

import db.api.ApiDB;
import db.tools.DbException;
import services.tools.ServiceCaller;
import services.tools.ServiceCodes;
import services.tools.ServicesToolBox;

public class ApiServices {
	
	/**LATER OPTIMISATION : MAP D API AVEC UN BOOLEAN QUI DIT QUI EST POSSIBLE 
	 * DE RAFRAICHIR LAPI
	 * + UNE ROUTINE PAR API QUI REMET LE BOOLEAN ALIVE(DATA IS UP TO DATE) 
	 * A FALSE AU BOUT D UN CERTAIN TEMPS 
	 * @throws JSONException 
	 * **/

	/**
	 * Update Apis data 
	 * @param key
	 * @param lat
	 * @param lon
	 * @param radius
	 * @param rows
	 * @return
	 * @throws DbException
	 * @throws JSONException */
	public static JSONObject updateApisData(String key,//not used for now but maybe later
			double lat,double lon,int radius,int rows)
			throws DbException, JSONException{
		ApiDB.updateApisData(lat, lon, radius, rows);
		return ServicesToolBox.reply(ServiceCodes.STATUS_KANPEKI 
				,null,null,ServiceCaller.whichServletIsAsking().hashCode()); }
	
}


