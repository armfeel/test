package upload.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaiduTest2 extends HttpServlet {
	
	public static String api_key = "QvyA9rdwi6exhWOaV0sQNSbx";
	
	//http://openapi.baidu.com/oauth/2.0/login_success
	//#expires_in=2592000&
	//access_token=3.78846dbd73877f2e28bba323d9f22f21.2592000.1356849475.2367156752-426627&
	//session_secret=af351b7f83ea4a1b40effc63c1aa7bed&
	//session_key=94rkXGWE90YMQq8AC6MfHYvluCsQk5OgHHXKDeWOqx0pGrup193tKIVk5YMbkobWXp%2Bk6nHYScJkQgXpiBK4qJQLfaOwnTM%3D&scope=basic+netdisk
	
	public static String secret_key = "OMw7lGrTcu1lVbxkt922u8aYe99b82oD";
	
	String redirect_uri = "http://agent.youshang.com/federation/forConnect/baiduLogin.do?loginType=kuaijifree3";
	
	//http://agent.youshang.com/federation/forConnect/baiduLogin.do?loginType=kuaijifree3
	
	private static final long serialVersionUID = 1L;
       
    public BaiduTest2() {
    	
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			deal(request,response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			deal(request,response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void deal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		
		String code = request.getParameter("code");
		if(code==null||"".equals(code)) {
			throw new ServletException("code is null");
		}
		HttpGet get = new HttpGet("https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code="+code+"&client_id="+api_key+"&client_secret="+secret_key+"&redirect_uri="+URLEncoder.encode(redirect_uri, "utf-8"));
		String result = TestUtil.sendHttpRequest(get);
		System.out.println(result);
		if(result!=null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(result);
			
			if(json.get("access_token")!=null)  {
				String token = (String) json.get("access_token");
				String session_secret = (String) json.get("session_secret");
				String session_key = (String) json.get("session_key");
				request.getSession().setAttribute("access_token", token);
				request.getSession().setAttribute("session_secret", session_secret);
				request.getSession().setAttribute("session_key", session_key);
				System.out.println("success!!");
			}
		}
		
		response.sendRedirect("/");
	
		
	}



}
