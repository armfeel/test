package upload.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Upload extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public Upload() {
    	
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			deal(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	private void deal(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		 DiskFileItemFactory factory = new DiskFileItemFactory();
		 ServletFileUpload upload = new ServletFileUpload(factory);
		 upload.setFileSizeMax(2000*1024);
		 List<FileItem> list = upload.parseRequest(request);
		 if(list!=null && list.size()>0) {
			 FileItem item = list.get(0);
			 InputStream in = item.getInputStream(); 
	         File f = new File("d:/temp", "test.txt");
	         FileOutputStream o = new FileOutputStream(f);
	         byte b[] = new byte[1024];
	         int n;
	         while ((n = in.read(b)) != -1) {
	             o.write(b, 0, n);
	         }
	         o.close();
	         in.close();
		 }
		 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InputStream in = request.getInputStream();
			byte b[] = new byte[1024];
	        int n;
	        while ((n = in.read(b)) != -1) {
	            String str = new String(b, 0, n);
	            System.out.println(str);
	            
	         }
	         in.close();
			deal(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
