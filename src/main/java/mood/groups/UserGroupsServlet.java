package mood.groups;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Groups;
import servlets.tools.templates.online.OnlineGetServlet;

public class UserGroupsServlet extends OnlineGetServlet {
	private static final long serialVersionUID = 1L;
	public UserGroupsServlet() {super();}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, 
			Map<String, String> params)throws Exception {
		response.getWriter().print(Groups.userGroups(
				params.get("skey")));}

}
