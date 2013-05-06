package upload.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaiduTest3
 */
public class BaiduTest3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	private static  Map<String,String> map = new ConcurrentHashMap<String,String>(); 

    public BaiduTest3() {
        super();
        map.put("kingdeetest504", "3.498d78fa8bb298c4713b871b3a82a6c5.2592000.1357261743.2367156752-426627");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		deal(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		deal(request, response);
	}
	
    private void deal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//request.setCharacterEncoding("utf-8");  
		String userName = request.getParameter("userName");
		String temp = new String(userName.getBytes("iso-8859-1"),"gb2312"); 
		String temp1 = new String(userName.getBytes("iso-8859-1"),"utf-8"); 
		String temp2 = new String(userName.getBytes("utf-8"),"gb2312"); 
		String temp3 = new String(userName.getBytes("gb2312"),"utf-8"); 
		String temp5 = new String(userName.getBytes("gb2312"),"iso-8859-1"); 
		String temp4 = new String(userName.getBytes("utf-8"),"iso-8859-1"); 
		System.out.println(userName);
		System.out.println(temp);
		System.out.println(temp1);
		System.out.println(temp2);
		System.out.println(temp3);
		System.out.println(temp5);
		System.out.println(temp4);
		if(userName!=null&&!userName.equals("")) {
			if(map.get(userName)!=null) {
				response.getWriter().print(map.get(userName));//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			}
		}
	}
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    	String rediirect = URLEncoder.encode("编码测试", "gb2312");
    	System.out.println(rediirect);
	}

}
