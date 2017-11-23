package web;

import domain.Stock;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2016/9/27.
 */
@WebServlet(name ="ajaxMinWindowWervlet",urlPatterns = {"/ajaxMinWindowWervlet"})
public class ajaxMinWindowWervlet extends HttpServlet {
    //保存股票的对象
    private Map<String, Stock> stocks;
    public void init() throws ServletException {
        stocks=new HashMap();
        //创建股票
        Stock zhy=new Stock("s1001","中石油",3000.0,2992.1);
        Stock pfyh=new Stock("s1002","浦发银行",23.22,24.60);
        stocks.put(zhy.getId(),zhy);
        stocks.put(pfyh.getId(),pfyh);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json;charset=utf-8");
        //计算随机数
        double zh=Math.random()*20;
        double pf=Math.random()*0.5;
        /*通过随机数是奇数还是偶数来自行设置股票应该正价还是减少*/
        boolean zhFlag=((int)(Math.random()*10))%2==0;
        boolean pfFlag=((int)(Math.random()*10))%2==0;
        Stock zhy=stocks.get("s1001");
        Stock pfyh=stocks.get("s1002");
        double temp;
        if(zhFlag){
            temp=zhy.getNow()+zh;
        }else{
            temp=zhy.getNow()-zh;
        }
        zhy.setNow((int)(temp*100)/100.0);
        if(pfFlag){
            temp=pfyh.getNow()+pf;
        }else{
            temp=pfyh.getNow()-pf;
        }
        pfyh.setNow((int)(temp*100)/100.0);
//        String name=request.getParameter("name");

//        name= URLDecoder.decode(name,"utf-8");

        JSONObject json= JSONObject.fromObject(stocks);
        response.getWriter().write(json.toString());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
