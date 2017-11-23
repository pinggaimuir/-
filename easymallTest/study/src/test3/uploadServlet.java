package test3;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by tarena on 2016/9/13.
 */
@WebServlet(name = "uploadServlet")
public class uploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        Map<String,Object> map=new HashMap();
        DiskFileItemFactory factory=new DiskFileItemFactory();
        factory.setSizeThreshold(1024*10);
        factory.setRepository(new File("/WEB-INF/tmp"));
        ServletFileUpload upload=new ServletFileUpload(factory);
        upload.setFileSizeMax(1024*1024*2);
        upload.setSizeMax(1024*1024*10);
        upload.setHeaderEncoding("utf-8");
        try {
            List<FileItem> list= upload.parseRequest(request);
            for(FileItem item:list){
                if(item.isFormField()){
                    String name=item.getFieldName();
                    String value=item.getString("utf-8");
                    map.put(name,value);
                }else{
                    String path="/WEB-INF/upload";
                    String fname=item.getName();
                    fname= UUID.randomUUID().toString()+fname;
                    String hash=Integer.toHexString(fname.hashCode());
                    for(char c:hash.toCharArray()){
                        path+="/+c";
                    }
                    new File(getServletContext().getRealPath(path+fname)).mkdir();
                    InputStream in=item.getInputStream();
                    OutputStream out=new FileOutputStream(path+fname);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
