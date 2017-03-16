package tools.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

/** 
 * @author AJoan
 * **@goodToKnow ! FLUENT STYLE CODE*/
public class ServicesToolBox {

	/**
	 * @description 
	 * Return a predefined JSONObject containing
	 * the service's {result}, 
	 * the servlet's {rpcode},
	 * and the {status} code [_WARNING],
	 * a warning {message}
	 * 
	 * @param status
	 * @param result
	 * @param message
	 * @param replycode
	 * @return 
	 * @throws ShouldNeverOccurException */
	public static JSONObject warn(
			Object result,
			String message,
			int replycode
			)throws JSONException, ShouldNeverOccurException{

		 if(message==null) 
			throw new 
			ShouldNeverOccurException("In Warning responses, message part should never be null");

		return new JSONObject()
				.put("status",ServiceCodes._WARNING)
				.put("rpcode",replycode)
				.put("message",message)
				.put("result",result); //if result is null is should disappear from the org.json's response
	}


	/**
	 * @description 
	 * Return a predefined JSONObject containing
	 * the service's {result}, 
	 * the servlet {rpcode},
	 * and the status code [_KANPEKI] 
	 * 
	 * @param status
	 * @param result
	 * @param message
	 * @param replycode
	 * @return 
	 * @throws ShouldNeverOccurException */
	public static JSONObject answer(
			Object result,
			int replycode
			)throws JSONException, ShouldNeverOccurException{

		return new JSONObject()
				.put("status",ServiceCodes._KANPEKI)
				.put("rpcode",replycode)
				.put("result",result);
	}



	/**
	 * @description 
	 * Return a predefined JSONObject containing 
	 * the servlet's {rpcode},
	 * and the {status} code [_ISSUE]
	 * 
	 * @param status
	 * @param result
	 * @param message
	 * @param replycode
	 * @return 
	 * @throws ShouldNeverOccurException */
	public static JSONObject alert(
			int replycode
			)throws JSONException, ShouldNeverOccurException{

		return new JSONObject()
				.put("status",ServiceCodes._ISSUE)
				.put("rpcode",replycode);
	}



	/**
	 * @description 
	 * Return a predefined JSONObject containing
	 * information about the internal error that occurred.
	 * Only useful on admin mode
	 * iserror = (internal server error)'s acronym   
	 * @param thr
	 * @return */
	public static JSONObject iserror(Throwable thr) throws JSONException{
		return new JSONObject().
				put("iserror",getStackTrace(thr))
				.put("errorpage","/Momento/err.jsp");
	}


	
	/**
	 * TODO work while is run as java application but not on server
	 * no error , silent execution with no visible effects 
	 * @param e	 */
	/*public static void logStackTrace(Exception e){  //Dont work on ovh
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		File f=new File("./logs");
		if(!f.exists()) f.mkdir();
		FileWriter fw=null;
		try {fw=new FileWriter("./logs/errorLog"+DBToolBox.getCurentTimeStamp().toString().replace(":", "_")+".log");
		fw.write(sw.toString());// stack trace in the log file
		} catch (IOException e1) {e1.printStackTrace();}
		finally {try {fw.close();} catch (IOException e1) {e1.printStackTrace();}}
	}*/

	
	
	/**
	 * @description  Generate an integer ID using Random.nextInt() Method   
	 * @return */
	public static int generateSimpleIntId() {return new Random().nextInt(Integer.MAX_VALUE);}


	
	
	/**
	 * Return the complete StackTrace of the throwable as String
	 * @param thr
	 * @return */
	public static String getStackTrace(Throwable thr){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		thr.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
	}

	
	
	
	/**
	 * @description return the current time in format day/month/year/ hour:minutes:seconds
	 * @return */
	public static String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/ HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date); //21/02/2015/ 13:52:11
	}

	
	

	/**
	 * @description return the current timestamp
	 * @return */
	public static Timestamp getCurentTimeStamp(){
		return new Timestamp(new Date().getTime());
	}


	
	/**
	 * @description Generate key(sequence of 32 hexadecimal digits)
	 *  with The 128-bit MD5 hash algorithm
	 * @return */
	public static String generateMD5ID(){
		String hashtext=null;
		try {
			MessageDigest m =MessageDigest.getInstance("MD5");
			m.reset();
			m.update(getCurentTimeStamp().toString().getBytes());
			hashtext=new BigInteger(1,m.digest()).toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 )
				hashtext = "0"+hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();hashtext=generateHomeMadeID();}
		return  hashtext;
	}

	
	
	/**
	 * @description  Home made 32 characters ID generated
	 * @return
	 */
	public static String generateHomeMadeID()  {
		char[] tab = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < 32; i++)
			sb.append( tab[r.nextInt( tab.length )] );
		return sb.toString();
	}


	
	
	public static void main(String[] args) {
		System.out.println(getCurrentTime());
		System.out.println(generateMD5ID());
		System.out.println(generateSimpleIntId());
	}
}
