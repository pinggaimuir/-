import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java多线程爬虫，爬取博客园主页
 * Created by gao on 2016/11/24.
 */
public class WebCrawler {

    ArrayList<String> allUrlSet=new ArrayList();//所有的网页url
    ArrayList<String> notCrawlUrlSet=new ArrayList<String>();//未爬取过的url
    HashMap<String,Integer> depth=new HashMap<String,Integer>();//所有的网页的url深度
    int crawDepth=2;//爬虫深度为两层
    int threadCount=10;//线程数量
    int count=0;//表示有多少个线程处于wait状态
    public static final Object signal=new Object();//线程通信变量

    public static void main(String[] args){
        final WebCrawler wc=new WebCrawler();
        wc.addUrl("http://www.cnblogs.com",1);
        long start=System.currentTimeMillis();
        System.out.println("开始爬虫-----------------------------");
        wc.begin();

        while (true){
            if(wc.notCrawlUrlSet.isEmpty()&&Thread.activeCount()==1||wc.count==wc.threadCount){
                long end=System.currentTimeMillis();
                System.out.println("总共爬取了"+wc.allUrlSet.size()+"个网页");
                System.out.println("总耗时"+(end-start)/1000+"秒");
                System.exit(1);
            }
        }
    }

    private void begin(){
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable(){
                public void run(){
                    while(true){
                        String tmp=getAUrl();
                        if(tmp!=null){
                            crawler(tmp);
                        }else{
                            synchronized (signal){
                                try{
                                    count++;
                                    System.out.println("当前有"+count+"个线程在等待");
                                    signal.wait();
                                }catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            },"thread-"+i).start();
        }
    }

    public synchronized void addUrl(String url,int d){
            notCrawlUrlSet.add(url);
            allUrlSet.add(url);
            depth.put(url,1);
    }

    public synchronized String getAUrl(){
        if(notCrawlUrlSet.isEmpty()) {
            return null;
        }
        String tmpAUrl;
        tmpAUrl=notCrawlUrlSet.get(0);
        notCrawlUrlSet.remove(0);
        return tmpAUrl;
    }

    public void crawler(String sUrl){
        URL url;
        try{
            url=new URL(sUrl);
            URLConnection urlConnection=url.openConnection();
            urlConnection.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream is=url.openStream();
            BufferedReader bReader=new BufferedReader(new InputStreamReader(is));
            StringBuffer sb=new StringBuffer();
            String line;
            //爬取数据加入StringBuffer中
            while((line=bReader.readLine())!=null){
                sb.append(line);
                sb.append("/r/n");
            }

            int d=depth.get(sUrl);
            System.out.println("爬取网页"+sUrl+"成功，深度为"+d+"是有线程"+Thread.currentThread().getName()+"爬取");
            if(d<crawDepth){
                //解析网页内容，从中提取链接
                parseContext(sb.toString(),d+1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void parseContext(String context,int dep){
        String regex="<a href.*?/a>";
        String s="fdfd<title>我 是</title><a href=\"http://www.iteye.com/blogs/tag/Google\">Google</a>fdfd<>";
        Pattern pt=Pattern.compile(regex);
        Matcher mt=pt.matcher(context);
        while (mt.find()){
            Matcher myurl=Pattern.compile("href=\".*?\"").matcher(mt.group());
            while (myurl.find()){
                String str=myurl.group().replaceAll("href=\"|\"","");
                if(str.contains("http:")){
                    //如果提取出的url是新的则加入爬取爬取队列
                    if(!allUrlSet.contains(str)){
                        addUrl(str,dep);
                    }
                    if(count>0){//如果有等待的线程，则唤醒
                        synchronized(signal){
                            count--;
                            signal.notify();
                        }
                    }
                }
            }
        }
    }
}
