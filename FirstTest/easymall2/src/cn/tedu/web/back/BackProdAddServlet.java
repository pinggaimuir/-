package cn.tedu.web.back;

import cn.tedu.domain.Product;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
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
 * Created by tarena on 2016/9/6.
 */
@WebServlet(name = "BackProdAddServlet")
public class BackProdAddServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //用来保存表单项的信息
        try {
        Map<String,String> map=new HashMap<String,String>();
        map.put("id", UUID.randomUUID().toString());
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //设置缓存区大小
        factory.setSizeThreshold(1024*20);
        //设置临时文件的存储路径
        factory.setRepository(new File(getServletContext().getRealPath("/WEB-INF/tmp/")));
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
                    for(char c:hash.toCharArray()){
                        path=path+"/"+c;
                    }
                    //创建文件夹
                    new File(getServletContext().getRealPath(path)).mkdirs();
                    InputStream in=item.getInputStream();
                    OutputStream out=new FileOutputStream(getServletContext().getRealPath(path+fname));
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
            //将商品保存到数据库
            Product prod=new Product();
            BeanUtils.populate(prod,map);
            ProdService service=BasicFactory.getFactory().getInstance(ProdService.class);
            service.addProduct(prod);
            //提示商品添加成功，跳转到后台商品查询
            response.getWriter().write("恭喜您添加成功@_@！");
            response.setHeader("Refresh","2;url="+request.getContextPath()+"/BackProdListServlet");
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
                request.setAttribute("msg","文件大小不能大于10M");
            request.getRequestDispatcher("/back/manageAddProd.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg",e.getMessage());
            request.getRequestDispatcher("/back/manageAddProd.jsp").forward(request,response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
