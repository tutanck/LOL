package mood.users.servlets.pp;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import mood.users.services.UserPlacesProfile;

import org.json.JSONObject;


public class UpdatePpServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public UpdatePpServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
 		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"places"}));}

	@Override
	public JSONObject doBusiness(
			HttpServletRequest request,
			HttpServletResponse response,
			JSONObject params
			)throws Exception {
return UserPlacesProfile.updatePp(params);
	}
}