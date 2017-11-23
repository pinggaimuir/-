package cn.tedu.web.back;

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
 * Created by tarena on 2016/9/7.
 */
@WebServlet(name = "BackProdListServlet")
public class BackProdListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> map=new HashMap<String,String>();
        String id= UUID.randomUUID().toString();
        map.put("id",id);
        try {
        response.setContentType("text/html;charset=utf-8");
        DiskFileItemFactory factory=new DiskFileItemFactory();
        factory.setSizeThreshold(1024*2);
        factory.setRepository(new File(getServletContext().getRealPath("/WEB-INF/tmp")));
        ServletFileUpload upload=new ServletFileUpload(factory);
        upload.setFileSizeMax(1024*1024*2);
        upload.setSizeMax(1024*1024*10);
        upload.setHeaderEncoding("utf-8");
            List<FileItem> list=upload.parseRequest(request);
            for(FileItem item:list){
                if(item.isFormField()){
                    String name=item.getFieldName();
                    String value=item.getString("utf-8");
                    map.put(name,value);
                }else{
                    String fname=item.getName();
                    fname=UUID.randomUUID().toString()+fname;
                    String path="/WEB-INF/upload";
                    String hash=Integer.toHexString(fname.hashCode());
                    for(char c:fname.toCharArray()){
                        path=path+"/"+c;
                    }
                    new File(getServletContext().getRealPath(path)).mkdirs();
                    InputStream in=item.getInputStream();
                    OutputStream out=new FileOutputStream(new File(getServletContext().getRealPath(path+fname)));
                    int len=-1;
                    byte[] bs=new byte[1024];
                    while((len=in.read(bs))!=-1){
                        out.write(bs,0,len);
                    }
                    map.put(item.getFieldName(),path+fname);
                    in.close();
                    out.close();
                    item.delete();
                }

            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
