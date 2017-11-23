package SpiderAZ;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 游戏产品信息爬取
 *
 * Created by gao on 2017/2/21.
 */
public class SpiderProduct {

    public static void main(String[] args) throws FileNotFoundException, XpathSyntaxErrorException {
        //调用下面的方法获取配置文件中的信息
        Map<String, Object> map = getElement("spiderYXGCyy");
        //创建线程池
        ExecutorService pool= Executors.newFixedThreadPool(5);
        List urlNodes= (List) map.get("urls");
        for(Object ele:urlNodes){
            Element element= (Element) ele;
            String url=element.getText().trim();
            System.out.println(url);
            pool.submit(new Spider(map,url));
        }
//        // 停止加入新的线程
//        pool.shutdown();
//        while (!pool.isTerminated()) {
//            // 如果所有线程执行完成,那么挑出该循环.
//        }
    }
    /**
     * 获取配置文件中要爬取的信息
     *
     * @param targetNode 目标节点
     * @throws FileNotFoundException
     */
    public static Map<String, Object> getElement(String targetNode){
        try{
            SAXReader reader = new SAXReader();
            //获取配置文档dom树
            Document dom=reader.read(SpiderProduct.class.getClassLoader().getResource("SpiderUtils/BasProductPattern.xml").getPath());
            //获取目标配置节点
            Node target=dom.selectSingleNode("//"+targetNode);
            //获取起始url
            List urls=target.selectNodes("//"+targetNode+"//url");
            //获取网站源名称
            String source=target.selectSingleNode("//"+targetNode+"/source").getText();
            //获取ContentPath
            String contentPath=target.selectSingleNode("//"+targetNode+"/contentPath").getText();
            //图标
            String logo=target.selectSingleNode("//"+targetNode+"/logo").getText();
            //名称
            String pname=target.selectSingleNode("//"+targetNode+"/pname").getText();
            //版本
            String pversion=target.selectSingleNode("//"+targetNode+"/pversion").getText();
            //分类
            String sort=target.selectSingleNode("//"+targetNode+"/sort").getText();
            //时间
            String ptime=target.selectSingleNode("//"+targetNode+"/ptime").getText();
            //时间格式化字符串
            String formatStr=target.selectSingleNode("//"+targetNode+"/ptime/@formatStr").getText();
            //大小
            String size=target.selectSingleNode("//"+targetNode+"/size").getText();
            //系统
            String system=target.selectSingleNode("//"+targetNode+"/system").getText();
            //资费
            String charges=target.selectSingleNode("//"+targetNode+"/charges").getText();
            //作者
            String author=target.selectSingleNode("//"+targetNode+"/author").getText();
            //简介
            String introduction=target.selectSingleNode("//"+targetNode+"/introduction").getText();
            //游戏截图
            String screenShoots=target.selectSingleNode("//"+targetNode+"/screenShoots").getText();
            //详情页url
            String detailurl=target.selectSingleNode("//"+targetNode+"/detailurl").getText();
            //下一页
            String nextpage=target.selectSingleNode("//"+targetNode+"/nextpage").getText();

            //将上面读到的配置文件中的xpath信息返回main方法
            Map<String, Object>map=new HashMap<>();
            map.put("urls",urls);
            map.put("source",source);
            map.put("contentPath",contentPath);
            map.put("logo",logo);
            map.put("pname",pname);
            map.put("pversion",pversion);
            map.put("sort",sort);
            map.put("ptime",ptime);
            map.put("formatStr",formatStr);
            map.put("size",size);
            map.put("system",system);
            map.put("charges",charges);
            map.put("author",author);
            map.put("introduction",introduction);
            map.put("screenShoots",screenShoots);
            map.put("detailurl",detailurl);
            map.put("nextpage",nextpage);
            return map;
        }catch(DocumentException e){
            System.out.println("配置文件获取错误！");
            return null;
        }
    }
}

//爬虫内部线程类
class Spider implements Runnable{
    private Map<String, Object> map=null;
    private String startUrl;
    public Spider(Map<String, Object> map,String startUrl) {
        this.map=map;
        this.startUrl=startUrl;
    }
    //线程执行内容
    public void run() {
        JXDocument doc=null;
        org.jsoup.nodes.Document document=null;
        try{
            document=Jsoup.connect(startUrl).get();
            doc=new JXDocument(document);
        }catch(IOException e){
            e.printStackTrace();
        }

        while(true){
            //调用下面的方法获取详情页的url列表
            List<String>detailUrls=getDetailUrls(doc,map.get("detailurl").toString());
            for(String detailUrl:detailUrls){
                try{
                    org.jsoup.nodes.Document document1=Jsoup.connect(map.get("contentPath").toString()+detailUrl)
                            .userAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727)")
                            .get();
                    //解析详情页面并且存储进数据库
                    parsePage(new JXDocument(document1),map);
                    System.out.println(Thread.currentThread().getName());
                }catch(IOException e){
                        e.printStackTrace();
                }
            }
            String next=null;
            //进入下一页
            try{
                String nextUrl=null;
                try {
                    //若获取下一页失败，则停止当前线程
                    nextUrl = doc.sel(map.get("nextpage").toString()).get(0).toString();
                }catch (Exception e){
                    return;
                }
                next=map.get("contentPath").toString()+nextUrl;
                org.jsoup.nodes.Document nextdocument=Jsoup.connect(next)
                        .userAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727)")
                        .get();
                doc=new JXDocument(nextdocument);
            }catch(Exception e){
                System.err.println(Thread.currentThread().getName()+"进入下一页错误");
            }
        }
    }



    /**
     * 获取每页列表中的详情页的url
     */
    public  List<String>getDetailUrls(JXDocument document, String detailurl){
        List<Object>urllist= null;
        try {
            urllist = document.sel(detailurl);
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
        //存储每页中列表上跳转到详情页的链接
        List<String>detailUrls=new ArrayList<>();
        for(Object detailUrl:urllist){
            detailUrls.add((String)detailUrl);
        }
        return detailUrls;
    }

    /**
     * 解析页面获取相应的字段
     *
     * @param document
     */
    public  void parsePage(JXDocument document, Map map){
        String logo=null;
        String name=null;
        String version=null;
        String time=null;
        String size=null;
        String sys=null;
        String charges=null;
        String author=null;
        String introduction=null;
        String screenShots=null;
        String sort=null;
        String source=null;
        //创建数据库表对象
        BasProGameInfo gameInfo=new BasProGameInfo();
        try{
            //源网站名称
            if(StringUtils.isNoneEmpty(map.get("source").toString())){
                source=map.get("source").toString();
                System.out.println(source);
                gameInfo.setSource(source);
            }
            //图标logo
            if(StringUtils.isNoneEmpty(map.get("logo").toString())){
                logo=map.get("contentPath").toString()+document.sel(map.get("logo").toString()).get(0).toString();
                if(map.get("source").toString()=="安智市场"||"安智市场".equals(map.get("source").toString())){
                    logo=Jsoup.connect(logo).ignoreContentType(true).get().location();
                }
                System.out.println(logo);
                gameInfo.setLogo(logo);
            }
            //名称gname
            if(StringUtils.isNoneEmpty(map.get("pname").toString())){
                name=document.sel(map.get("pname").toString()).get(0).toString();
                System.out.println(name);
                gameInfo.setGname(name);
            }
            //版本version
            if(StringUtils.isNoneEmpty(map.get("pversion").toString())){
                version=document.sel(map.get("pversion").toString()).get(0).toString();
                version=version.substring(1,version.lastIndexOf(")"));
                System.out.println(version);
                gameInfo.setVersion(version);
            }
            //分类(网络类型)
            if(StringUtils.isNotEmpty(map.get("sort").toString())){
                sort = document.sel(map.get("sort").toString()).get(0).toString();
                //如果是安卓市场的信息，则需要做进一步处理
                if(map.get("source").toString()=="安智市场"||"安智市场".equals(map.get("source").toString())) {
                    sort=sort.split("\\：")[1];
                }
                System.out.println(sort);
                gameInfo.setNetwork_type(getNetType(sort));
            }
            //setWeb_update_time
            if(StringUtils.isNotEmpty(map.get("ptime").toString())){
                String timeStr=document.sel(map.get("ptime").toString()).get(0).toString().split("\\：")[1];
                time=timeFormat(timeStr,map.get("formatStr").toString());
                System.out.println(time);
                gameInfo.setWeb_update_time(time);
            }
            //大小gamesize
            if(StringUtils.isNotEmpty(map.get("size").toString())){
                size=document.sel(map.get("size").toString()).get(0).toString().split("\\：")[1];
                System.out.println(size);
                gameInfo.setGame_size(size);
            }
//            //系统
//            if(StringUtils.isNoneEmpty(map.get("system").toString())){
//                sys=document.sel(map.get("system").toString()).get(0).toString().split("\\：")[1];
//                System.out.println(sys);
//            }
            //资费charge_mode
            if(StringUtils.isNotEmpty(map.get("charges").toString())){
                charges=document.sel(map.get("charges").toString()).get(0).toString().split("\\：")[1];
                System.out.println(charges);
                gameInfo.setCharge_mode(charges);
            }
            //游戏研发公司（作者）develop_com
            if(StringUtils.isNotEmpty(map.get("author").toString())){
                author=document.sel(map.get("author").toString()).get(0).toString();
                if(map.get("source").toString()=="安智市场"||"安智市场".equals(map.get("source").toString())) {
                    author=author.split("\\：")[1];
                }
                System.out.println(author);
                gameInfo.setDevelop_com(author);
            }

            //简介g_desc
            if(StringUtils.isNotEmpty(map.get("introduction").toString())){
                introduction=document.sel(map.get("introduction").toString()).get(0).toString();
                System.out.println(introduction);
                gameInfo.setG_desc(introduction);
            }
            //游戏截图picture
            if(StringUtils.isNotEmpty(map.get("screenShoots").toString())){
                List<Object> liEles=document.sel(map.get("screenShoots").toString());
                //多个游戏截图用“,”分隔连接为一个StringBuffer
                StringBuffer screenBuffer=new StringBuffer();
                for(Object ele:liEles){
                    //如果是安智市场的图片url需要进一步访问，获取其真实url
                    if(map.get("source").toString()=="安智市场"||"安智市场".equals(map.get("source").toString())) {
                        String screenShot=map.get("contentPath").toString()+ele.toString();
                        screenShot = Jsoup.connect(screenShot)
                                .ignoreContentType(true)
                                .userAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727)")
                                .timeout(1500)
                                .get().location();
                        screenBuffer.append(screenShot+",");
                    }
                    screenBuffer.append(ele.toString()+",");
                }
                screenShots=screenBuffer.toString();
                System.out.println(screenShots);
                gameInfo.setPicture(screenShots);
            }
            System.out.println("----------------------------------------------------------------------");
            //存入数据库
            storeToDataBase(gameInfo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过xpath来获取相对应的网页信息
     */
    public String getInfomation(JXDocument document, String xpath){
        String information=null;
        if(StringUtils.isNotEmpty(xpath)){
            try {
                information=document.sel(xpath).get(0).toString();
            } catch (XpathSyntaxErrorException e) {
                System.err.println("解析xpth："+xpath+"出错了！！！");
            }
        }
        System.out.println(information);
        return information;
    }

    /**
     *将数据存入mysql数据库中
     */
    public void storeToDataBase(BasProGameInfo gameInfo){
        //网站源链接
        gameInfo.setUrl(startUrl);
        //uuid
        gameInfo.setUuid(UUID.randomUUID().toString());
        //用dao层接口插入数据库
//        ProGameInfoDao dao=new ProGameInfoDaoImpl();
//        dao.insertGame(gameInfo);
    }

    /**
     * 将时间字符串格式化为Date类型
     */
    public  String timeFormat(String time,String formatStr)throws ParseException{
            SimpleDateFormat parse=new SimpleDateFormat(formatStr);
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            Date dateTemp=parse.parse(time);
            String date=format.format(dateTemp);
            return date;
    }

    /**
     * 通过游戏分类字段获取对应的游戏的网络类型
     */
    public String getNetType(String sort){
        if(map.get("source").toString()=="游戏观察"){
            return "2";
        }
        if(sort.equals("网络游戏")||sort=="网络游戏"){
            return "2";
        }else{
            return "0";
        }
    }
}


