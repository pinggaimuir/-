package August_24;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by tarena on 2016/8/26.
 */
@WebServlet(name = "ServletTest")
public class ServletTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        String username=request.getParameter("username");
//        System.out.println(username);
        /*这种方法只能解决post提交的数据，因为post中的参数在请求实体中，而get的参数在请求行url后*/
       // request.setCharacterEncoding("utf-8");



       // response.setContentType("text/html;charset=utf-8");
            //getParameterNames(String name) --------Enumeration<String>   获得所有参数的名字
        request.getRequestDispatcher("/loginServlet").include(request,response);
       // response.setContentType("text/html;charset=UTf-8");
        System.out.println("===========");
        Enumeration<String> names=request.getParameterNames();
        while(names.hasMoreElements()){
            String name= names.nextElement();
            String value=request.getParameter(name);
//            byte[] bytes=value.getBytes("iso-8859-1");
//            value=new String(bytes,"utf-8");
            response.getWriter().write("ServletTest2:"+name+":"+value+"<br>");
            System.out.println(name+":"+value);
        }

//        PrintWriter out=response.getWriter();
//        System.out.println("===========");
//        //getParaterMap()-------------Map<String,String[]> key:name   value:多值
//        Map<String,String[]> map=request.getParameterMap();
//        Set<Map.Entry<String,String[]>> set=map.entrySet();
//        for(Map.Entry<String,String[]> entry:set){
//            byte[] bytes=entry.getValue()[0].getBytes("iso8859-1");
//            String s=new String(bytes,"utf-8");
//            out.println("ServletTest2:"+entry.getKey()+"------"+s+"<br/>");
//
//        }
    }

}
