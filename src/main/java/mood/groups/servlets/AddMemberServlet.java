package mood.groups.servlets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;

public class AddMemberServlet extends PostServlet {
	private static final long serialVersionUID = 1L;
	public AddMemberServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"id","member"}));}

	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response,
								 Map<String, String> params)throws Exception {
		//response.getWriter().print(Groups.addMember(
	//			request.getParameter("id"),	request.getParameter("member")));
		return new JSONObject();
	}

}
