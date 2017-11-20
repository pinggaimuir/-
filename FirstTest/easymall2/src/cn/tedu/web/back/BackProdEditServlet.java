package cn.tedu.web.back;

import cn.tedu.domain.Product;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "BackProdEditServlet",urlPatterns = {"/BackProdEditServlet"})
public class BackProdEditServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        System.out.println(id);
        ProdService service= BasicFactory.getFactory().getInstance(ProdService.class);
        Product prod=service.findProdById(id);

       prod.setName(request.getParameter("name"));
        prod.setPrice(Double.parseDouble(request.getParameter("price")));
        prod.setCategory(request.getParameter("category"));
        prod.setPnum(Integer.parseInt(request.getParameter("pnum")));
        prod.setDescription(request.getParameter("description"));
        try {
            DiskFileItemFactory factory=new DiskFileItemFactory(1024*20,new File(getServletContext().getRealPath("/WEB-INF/tmp")));
            ServletFileUpload upload=new ServletFileUpload(factory);
            upload.setHeaderEncoding("utf-8");
            upload.setSizeMax(10*1024*1024);
            upload.setFileSizeMax(3*1024*1024);
                List<FileItem> list=upload.parseRequest(request);
            for(FileItem item:list){
                if(item.isFormField()){
                    String name=item.getFieldName();
                }else{
                    String fname=item.getName();
                    fname= UUID.randomUUID().toString()+fname;
                    String hash=Integer.toHexString(fname.hashCode());
                    String path="/WEB-INF/upload";
                    for(char c:hash.toCharArray()){
                        path=path+"/"+c;
                    }
                    new File(getServletContext().getRealPath(path)).mkdir();
                    InputStream in=item.getInputStream();
                    OutputStream out=new FileOutputStream(new File(path+fname));
                    int len=-1;
                    byte[] bs=new byte[1024];
                    while((len=in.read(bs))!=-1){
                        out.write(bs,0,len);
                    }
                    prod.setImgurl(path+fname);
                    in.close();
                    out.close();
                    item.delete();
                }
            }
            service.editProdById("id",prod);
            response.getWriter().write("恭喜您修改成功了！");
            response.setHeader("refresh","1;url="+request.getContextPath()+"/BackProdListServlet");
        } catch (FileUploadException e) {
            e.printStackTrace();
            request.setAttribute("msg",e.getMessage());
            request.getRequestDispatcher("/back/manageAddProd.jsp").forward(request,response);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
