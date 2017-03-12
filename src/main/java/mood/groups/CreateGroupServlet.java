package mood.groups;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.business.Groups;
import servlets.tools.templates.online.OnlinePostServlet;

public class CreateGroupServlet extends OnlinePostServlet {
	private static final long serialVersionUID = 1L;
	public CreateGroupServlet() {super();}

	@Override
	public void init() throws ServletException {
		super.init();
		super.epn=new HashSet<>(Arrays.asList(new String[]{"name","members"}));}

	@Override
	public void doBusiness(HttpServletRequest request, HttpServletResponse response, 
			Map<String, String> params)throws Exception {
		response.getWriter().print(Groups.createGroup(
				params.get("skey"),
				request.getParameter("name"),
				request.getParameter("members")));}

}
