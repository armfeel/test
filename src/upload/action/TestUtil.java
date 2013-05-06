package upload.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class TestUtil {
	
	static final String PCS_REQUEST_URL = "https://pcs.baidu.com/rest/2.0/pcs";
	static final String KEY_METHOD = "method";
	static final String KEY_ACCESS_TOKEN = "access_token";
	static final String KEY_PATH = "path";
	static final String KEY_BY = "by";
	static final String KEY_ORDER = "order";
	static final String KEY_LIMIT = "limit";

	static String ACCESS_TOKEN = "Please use Oauth2 to get the access token";
	
	static String upload() {
		 return null;
	}
	
	
	static String download(String filePath,String localPath,String token) throws IOException {
		final String mMethod = "download";
	    final String mCommand = "file";
	    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair(KEY_METHOD, mMethod));
	    params.add(new BasicNameValuePair(KEY_ACCESS_TOKEN, token));
	    if(filePath!=null)
	        params.add(new BasicNameValuePair(KEY_PATH, filePath));
	    String url = PCS_REQUEST_URL + "/" + mCommand + "?" +buildParams(params);
	    HttpGet httpget = new HttpGet(url);
	    HttpClient client = TrustAllSSLSocketFactory.getNewHttpClient();
	    HttpResponse response = client.execute(httpget);
	    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			FileOutputStream out = new FileOutputStream(localPath);
			response.getEntity().writeTo(out);
			out.close();
			return "success";
		}
        return "error";
	    
	}
	
	
	public static String inputStreamToFile(InputStream is,String localPath) throws IOException {
		File file = new File(localPath); 
		file.mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		BufferedOutputStream buOut = new BufferedOutputStream(out);
		BufferedInputStream buIn = new BufferedInputStream(is);
		byte[] data = new byte[4096];
		int length;
		while ((length=buIn.read(data))!=-1) {
			buOut.write(data, 0, length);
		}
		buOut.flush();
		buOut.close();
		buIn.close();
		out.close();
		return "success";
	}


	static String getList(String path, String by, String order, String limit,String token)
	{
	    //set url params
	    final String mMethod = "list";
	    final String mCommand = "file";
	    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    if(path!=null)
	        params.add(new BasicNameValuePair(KEY_PATH, path));
	    params.add(new BasicNameValuePair(KEY_METHOD, mMethod));
	    params.add(new BasicNameValuePair(KEY_ACCESS_TOKEN, token));
	    if(by != null & (by.equals("time") || by.equals("name") || by.equals("size")))
	    {
	        params.add(new BasicNameValuePair(KEY_BY, by));
	    }
	    if(order != null & (order.equals("asc") || order.equals("desc")))
	    {
	        params.add(new BasicNameValuePair(KEY_ORDER, order));
	    }
	    if(limit != null)
	    {
	        Pattern pattern = Pattern.compile("^\\d+,\\d+$");
	        Matcher matcher = pattern.matcher(limit);
	        while (matcher.find()) { 
	            String[] nums = limit.split(",");
	            if(Integer.valueOf(nums[0]) < Integer.valueOf(nums[1]))
	                params.add(new BasicNameValuePair(KEY_LIMIT, limit));
	        }
	    }

	    //build url
	    String url = PCS_REQUEST_URL + "/" + mCommand + "?" + buildParams(params);
	    System.out.println(url);
	    HttpGet httpget = new HttpGet(url);
	    String response = sendHttpRequest(httpget);

	    return response;
	}

	static String upload(String path, String target,String flieName,String token)
	{
	    final String mMethod = "upload";
	    final String mCommand = "file";
	    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair(KEY_METHOD, mMethod));
	    params.add(new BasicNameValuePair(KEY_ACCESS_TOKEN, token));
	    params.add(new BasicNameValuePair("dir", target));
		params.add(new BasicNameValuePair("filename", flieName));
	    String url = PCS_REQUEST_URL + "/" + mCommand + "?" +buildParams(params);

	    HttpPost post = new HttpPost(url);
	    MultipartEntity reqEntity = new MultipartEntity();
	    FileBody bin = new FileBody(new File(path));
		reqEntity.addPart("uploadedfile", bin);
	    multipartAddKV(reqEntity, params);
		post.setEntity(reqEntity);
	    String response = sendHttpRequest(post);

	    return response;
	}
	
	static String buildParams(List<NameValuePair> urlParams){
	    String ret = null;
	    if(null != urlParams && urlParams.size() > 0){
	        try {
	            HttpEntity paramsEntity = new UrlEncodedFormEntity(urlParams, "utf8");
	            ret = EntityUtils.toString(paramsEntity);
	        } catch (UnsupportedEncodingException e1) {
	            e1.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return ret;
	}
	static String sendHttpRequest(HttpRequestBase request){
	    String resultJsonString = "";
	    if (null != request) {
	        // create client
	        HttpClient client = TrustAllSSLSocketFactory.getNewHttpClient();
	        try {
	            HttpResponse response = client.execute(request);
	            if (null != response) {
	                HttpEntity resEntity = response.getEntity();
	                resultJsonString = EntityUtils.toString(resEntity);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return resultJsonString;
	}
	
	private static void multipartAddKV(MultipartEntity reqEntity,List<NameValuePair> params) {
		for (NameValuePair kv : params) {
			StringBody body = null;
			try {
				body = new StringBody(kv.getValue());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			reqEntity.addPart(kv.getName(), body);
		}
		
	}





}
