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
	 * @throws DbException */
	public static void updateApisData(double lat,double lon,int radius,int rows)
			throws DbException{	
		DataIledefranceFr.storeApiAsPosts(lat,lon,radius,rows);}

}
