package mood.searchposts.services;

import org.json.JSONException;
import org.json.JSONObject;

import db.tools.DbException;

/**ICI ON VA FAIRE UN AIGUILLEUR DE RECHERCHE QUI VA GERER LES HASHTAG , LA RECHERCHE DE LIEUX DE POSTS ET DE PERSONNES 
 * ETC EN FONCTION D UNE ANALYSE SYNTAXIQUE DES QUERY (@ , # , *, etc) 
 * LATER
 * @author  Anagbla Jean */
public class Search {	

	/**methode explode : 
	 * une methode ki va explode et verifier si il ya des tag(sujet)
	 *  ou des personnes,lieux , document , position etc a chercher
	 *  NOTE POUR MOI MEME  : UTILISER JAVA REFLECT ET  
	 *  STACKTRACEELEMENT[] STACKTRACEELEMENTS = THREAD.CURRENTTHREAD().GETSTACKTRACE()
ACCORDING TO THE JAVADOCS:
THE LAST ELEMENT OF THE ARRAY REPRESENTS THE BOTTOM OF THE STACK, WHICH IS THE LEAST RECENT METHOD INVOCATION IN THE SEQUENCE. 
A STACKTRACEELEMENT HAS GETCLASSNAME(), GETFILENAME(), GETLINENUMBER() AND GETMETHODNAME().
YOU WILL HAVE TO EXPERIMENT TO DETERMINE WHICH INDEX YOU WANT (PROBABLY STACKTRACEELEMENTS[1] OR [2]).
http://stackoverflow.com/questions/421280/how-do-i-find-the-caller-of-a-method-using-stacktrace-or-reflection
	 *   POUR RETROUVER LA SERVLET APPELANTE ET SE PASSER DU PASSAGE DE SKEY EN PARAM ET RETROUVER DIRECTEMENT LA CLE DE SESSION OU L UID
	 *   DEPUIS HTTP SESSION DEPUIS TOUS LES SERVICES DE HAUT NIVEAU (LEVEL1 SERVICE INTERFACE )
	 *  */
	public static JSONObject search(/*String key,*/String query) throws JSONException, DbException{	return null;}

	public static void main(String[] args) {}

}