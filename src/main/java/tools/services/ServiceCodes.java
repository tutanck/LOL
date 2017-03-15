package tools.services;

public interface ServiceCodes {
	/**
	 * STATUS CODES */
	int _ISSUE=-1;
	int _KANPEKI=0;
	int _WARNING=1; 
	
	/**
	 * BUSINESS ERRORS CODES */
	
	/* Taken resources -1*/
	int USERNAME_IS_TAKEN=-11;
	int EMAIL_IS_TAKEN = -12;
	int PHONE_IS_TAKEN = -13;
	
	/* unknown resources -2*/
	int UNKNOWN_EMAIL_ADDRESS=-21;
	int WRONG_LOGIN_PASSWORD=-22;
	int UNKNOWN_USERID = -23;
	
	/*bad context*/
	int USER_CONNECTED = -31;  
	int USER_DISCONNECTED=-32; //attention http TODO a voir
	int USER_NOT_CONFIRMED = -33;
	
	/*admin error*/
	int MRP_DOUBLING=-111;
	
}
