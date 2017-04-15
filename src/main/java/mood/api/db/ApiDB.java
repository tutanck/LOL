package mood.api.db;

import db.api.apis.DataIledefranceFr;
import db.tools.DbException;

public class ApiDB {
	
	/**
	 * Update Apis data in database
	 * @param lat
	 * @param lon
	 * @param radius
	 * @param rows
	 * @throws DBException */
	public static void updateApisData(double lat,double lon,int radius,int rows)
			throws DBException{	
		DataIledefranceFr.storeApiAsPosts(lat,lon,radius,rows);}

}
