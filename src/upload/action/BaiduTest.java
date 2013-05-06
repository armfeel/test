package upload.action;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaiduTest extends HttpServlet {
	
	public static String api_key = "QvyA9rdwi6exhWOaV0sQNSbx";
	
	//http://openapi.baidu.com/oauth/2.0/login_success
	//#expires_in=2592000&
	//access_token=3.78846dbd73877f2e28bba323d9f22f21.2592000.1356849475.2367156752-426627&
	//session_secret=af351b7f83ea4a1b40effc63c1aa7bed&
	//session_key=94rkXGWE90YMQq8AC6MfHYvluCsQk5OgHHXKDeWOqx0pGrup193tKIVk5YMbkobWXp%2Bk6nHYScJkQgXpiBK4qJQLfaOwnTM%3D&scope=basic+netdisk
	
	public static String secret_key = "OMw7lGrTcu1lVbxkt922u8aYe99b82oD";
	
	String redirect_uri = "oob";//"http://agent.youshang.com/file/test?action=access";
	
	
	private static final long serialVersionUID = 1L;
       
    public BaiduTest() {
    	
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
		redirect_uri = "http://agent.youshang.com/federation/forConnect/baiduLogin.do?loginType=kuaijifree3";
		String rediirect = URLEncoder.encode(redirect_uri, "utf-8");
		System.out.println(rediirect);
		String action = request.getParameter("action");
		if("upload".equals(action)) {
			upload(request,response);
		}
		else if("oauth".equals(action))  {
			String url = "https://openapi.baidu.com/oauth/2.0/authorize?response_type=code&scope=netdisk&client_id="+api_key+"&redirect_uri="+rediirect;
			response.sendRedirect(url);
		}
		else if("oauth2".equals(action))  {
			String url = "https://openapi.baidu.com/oauth/2.0/authorize?response_type=code&client_id="+api_key+"&redirect_uri="+rediirect;
			response.sendRedirect(url);
		}
		else if("access".equals(action))  {
			String code = request.getParameter("code");
			if(code==null||"".equals(code)) {
				throw new ServletException("code is null"); 
			}
			HttpGet get = new HttpGet("https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code="+code+"&client_id="+api_key+"&client_secret="+secret_key+"&redirect_uri="+rediirect);
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
				else {
					
				}
			}
		}
		
		else if("query".equals(action)) { 
			String token = (String) request.getSession().getAttribute("access_token");
			HttpGet get = new HttpGet("https://pcs.baidu.com/rest/2.0/pcs/quota?method=info&access_token="+token);
			String result = TestUtil.sendHttpRequest(get);
			System.out.println(result);
			response.getWriter().println(result);
		}
		
		else if("list".equals(action)) { 
			String token = (String) request.getSession().getAttribute("access_token");
			String result = TestUtil.getList("/apps/免费财务软件/","time", "asc", "0-9", token);
			System.out.println(result);
			response.getWriter().println(result);
		}
		else if("download".equals(action)) {
			String token = (String) request.getSession().getAttribute("access_token");
			String result = TestUtil.download("/apps/免费财务软件/commons-fileupload-1.2.2.jar","c:/commons-fileupload-1.2.2.jar",token);
			System.out.println(result);
			response.getWriter().println(result);
		}
		
	
		
	}


	private void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = (String) request.getSession().getAttribute("access_token");
		String result = TestUtil.upload("C:\\Users\\kk\\Desktop\\commons-fileupload-1.2.2.jar", "/apps/免费财务软件/","commons-fileupload-1.2.2.jar",token);
		System.out.println(result);
		response.getWriter().println(result);
	}
	
	public static void upload(HttpServletRequest request) throws IOException {
		request.getInputStream();
	}
	

}
