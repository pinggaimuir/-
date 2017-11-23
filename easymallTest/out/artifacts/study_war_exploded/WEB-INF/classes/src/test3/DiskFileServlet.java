package test3;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by tarena on 2016/9/6.
 */
@WebServlet(name = "DiskFileServlet" )
public class DiskFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 /*       DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload sful=new ServletFileUpload(factory);
        try {
            List list=sful.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }*/
        try {
        //判断表单书否配置了enctype=multipart/form-data
        if(!ServletFileUpload.isMultipartContent(request)){
            throw new RuntimeException("请使用正确的表单参数上传");
        }
        //创建DiskFileItemFactory
        DiskFileItemFactory factory=new DiskFileItemFactory();
        factory.setSizeThreshold(10240);
        factory.setRepository(new File(getServletContext().getRealPath("/WEB-INF/tmp/")));
        //创建ServletFileUpload对象
        ServletFileUpload upload=new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");
        //设置单个文件的最大值
        upload.setFileSizeMax(1024*1024*2);
        //设置文件的总大小
        upload.setSizeMax(1024*1024*2);
        //获取所有的输入向

            List<FileItem> list=upload.parseRequest(request);
            for(FileItem item:list){
                if (item.isFormField()) {
                    String name=item.getFieldName();//非文件项属性的名称
                    String value=item.getString("utf-8");//非文件项的属性的值
                }else{
                    String filename=item.getName();//获取文件名
                    //ie部分版本的浏览器获取到的文件名中高欢客户端的本地路径，截取文件名
                    if(filename.indexOf("\\")!=-1){
                        filename=filename.substring(filename.lastIndexOf("\\")+1);
                    }
                    filename= UUID.randomUUID().toString()+"_"+filename;
                    String hash=Integer.toHexString(filename.hashCode());
                    String path="/WEN-INF/upload/";
                    for(char c:hash.toCharArray()){
                        path=path+"/"+c;
                    }

                    InputStream in=item.getInputStream();
                    OutputStream out=null;
                    out=new FileOutputStream(new File(request.getContextPath()+path+"/"+filename));
                    int len=-1;
                    byte[] bs=new byte[1024];
                    while((len=in.read(bs))!=-1){
                        out.write(bs,0,len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
