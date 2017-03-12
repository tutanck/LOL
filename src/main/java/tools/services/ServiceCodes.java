package tools.services;

public interface ServiceCodes { //TODO separer http et metier
	/**
	 * STATUS CODES */
	int STATUS_KANPEKI=0;
	int NOERROR = 0;
	int STATUS_GOODnBAD=1; //1 ((1||0)for 1:bad and 0:good) 
	int STATUS_BAD=-1;
	
	/**
	 * ERROR CODES */
	/* Taken resources -1*/
	int USERNAME_IS_TAKEN=-11;
	int EMAIL_IS_TAKEN = -12;
	int PHONE_IS_TAKEN = -13;
	/* unknown field value -2*/
	int UNKNOWN_EMAIL_ADDRESS=-21;
	int WRONG_LOGIN_PASSWORD=-22;
	int UNKNOWN_USERID = -23;
	/*bad context*/
	int USER_CONNECTED = -31;
	int USER_DISCONNECTED=-32;
	int USER_NOT_CONFIRMED = -33;
	/*actions denied*/
	int ACTION_DENIED=-91;
	int ROOT_ACTION_DENIED=-91;
	/*admin error*/
	int MRP_DOUBLING=-111;
	
	
}
