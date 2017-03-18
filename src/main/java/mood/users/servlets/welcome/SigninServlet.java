package mood.users.servlets.welcome;

import fr.aj.jeez.servlet.offline.OfflinePostServlet;
import mood.users.services.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;


/**
 * Created by Joan on 12/03/2017.
 */

@WebServlet(name = "SigninServlet" ,urlPatterns={"/signin"})
public class SigninServlet extends OfflinePostServlet{

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException { //did : deviceID
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"username","pass","did"}));}

	@Override
	public JSONObject doBusiness(
			HttpServletRequest request,
			HttpServletResponse response,
			JSONObject params
			)throws Exception {
		return  User.login(params);
	}
}
