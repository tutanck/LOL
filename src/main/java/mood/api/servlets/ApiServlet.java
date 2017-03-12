package mood.api.servlets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;


public class ApiServlet extends PostServlet {
	private static final long serialVersionUID = 1L;

    public ApiServlet() {super();}
    
    @Override
	public void init() throws ServletException {
		super.init();
		super.epnIn=new HashSet<>(Arrays.asList(new String[]{"lat","lon","rad","row"}));}


	@Override
	public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
			throws Exception {
		/*response.getWriter().print(ApiServices.updateApisData(
				params.get("skey"),
				Double.parseDouble(request.getParameter("lat")),
				Double.parseDouble(request.getParameter("lon")),
				Integer.parseInt(request.getParameter("rad")),
				Integer.parseInt(request.getParameter("row"))));*/
		return new JSONObject();
				}
}
