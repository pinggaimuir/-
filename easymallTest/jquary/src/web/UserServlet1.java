package web;

import domain.User4;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/28.
 */
@WebServlet(name="UserServlet1",urlPatterns = {"/UserServlet1"})
public class UserServlet1 extends HttpServlet {
    private List<User4> user4List =new ArrayList();
    public void init() throws ServletException {
        User4 user1=new User4("男",23,1001,"高健");
        User4 user2=new User4("男",23,1002,"小雨");
        User4 user3=new User4("男",22,1003,"老胡");
        user4List.add(user1);
        user4List.add(user2);
        user4List.add(user3);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.setContentType("text/html;charset=utf-8");
//        resp.getWriter().write("<tr><td>1001</td><td>高</td><td>男</td><td>23</td></tr>");
        JSONArray jsonArray=JSONArray.fromObject(user4List);
        resp.getWriter().write(jsonArray.toString());
    }
}
