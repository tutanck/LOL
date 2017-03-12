package mood.users.welcome;

import fr.aj.jeez.servlet.basic.PostServlet;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Joan on 12/03/2017.
 */

@WebServlet(name = "SigninServlet" ,urlPatterns={"/signin"})
public class SigninServlet extends PostServlet{

    @Override
    public JSONObject doBusiness(HttpServletRequest request, HttpServletResponse response, Map<String, String> params)
            throws Exception {
        //JSONObject res=User.logout(params);
return new JSONObject();
    }
}
