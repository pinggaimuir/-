package cn.bric.crawler;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.RecordCrawlResult;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 抓取来源：http://www.icdx.co.id/marketdata/historicaldata?product=cpotr&year=2017&month=02
 * 抓取内容：油脂油料-印尼棕榈油-印度尼西亚原产品和衍生交易所收盘价
 * 抓取频次：每日下下班前 17:00。
 * Created by 高健 on 2017/3/21.
 */
public class ICDXDataFetch {
    public ICDXDataFetch(){}

    private String varName = "印尼棕榈油";//品种名
    private String cnName = "印度尼西亚原产品和衍生交易所收盘价";//指标名
    private String className=ICDXDataFetch.class.getName();

    private final Logger LOG = Logger.getLogger(ICDXDataFetch.class);

    private DAOUtils dao = new DAOUtils();

    private SimpleDateFormat format=new SimpleDateFormat("MMM dd, yyyy",new Locale("English"));
    private SimpleDateFormat format2=new SimpleDateFormat("yyyyMMdd");

    //定时爬虫的开始方法
//    @Scheduled(cron = CrawlScheduler.CRON_ICDCDATATETCH)
    public void start(){
        //从数据库表bric_crawler_table中查询该爬虫是否已标记为关闭
        String switchFlag = new CrawlerManager().selectCrawler("印度尼西亚原产品和衍生交易所收盘价", className.substring(className.lastIndexOf(".")+1));
        if(switchFlag == null){
            LOG.info("没有获取到印度尼西亚原产品和衍生交易所收盘价爬虫的定时器配置");
            RecordCrawlResult.recordFailData(className, null, null, "没有获取到印度尼西亚原产品和衍生交易所收盘价爬虫的定时器配置");
        }else{
            if(switchFlag.equals("1")){
                try{
                    //要爬取的目标网址
                    String url="http://www.icdx.co.id/marketdata/historicaldata?product=cpotr&year=2017&month=02";
                    try {
                        LOG.info("********"+new Date()+"印度尼西亚原产品和衍生交易所收盘价爬虫开始*******");
                        Document document=Jsoup.connect(url)
                                .timeout(5000)
                                .get();
                        //解析网页
                        this.parsePage(document);
                        LOG.info("********"+new Date()+"印度尼西亚原产品和衍生交易所收盘价爬虫爬取入库完毕*******");
                    } catch (Exception e) {
                        LOG.error("*******链接页面失败************    "+url);
                    }
                } catch(Exception e){
                    LOG.error("发生未知异常：", e);
                    RecordCrawlResult.recordFailData(className, null, null, "发生未知异常");
                }
            }else{
                LOG.info("抓取印度尼西亚原产品和衍生交易所收盘价的定时器已关闭");
                RecordCrawlResult.recordFailData(className, null, null, "抓取印度尼西亚原产品和衍生交易所收盘价的定时器已关闭");
            }
        }
    }
    //测试方法
    public static void main(String[] args) {
        new ICDXDataFetch().start();
    }

    /**
     * 解析页面
     * @param document jsoup的dom
     */
    public void parsePage(Document document) throws Exception {
        JXDocument doc=new JXDocument(document);
        try {
            //TimeInt
            List<String> timeInts = this.getFieldsByXpath(doc, "//div[@style='font-weight:bold; margin-left:50px;']/text()");
            //月份数据
            int n = 0;
            for (int i = 0; i < timeInts.size(); i++) {
                //存储一行数据的map
                Map<String, String> dataMap = new HashMap<>();

                List<String> fields = this.getFieldsByXpath(doc, "//div[@id='marketquotetable']/div[" + (n += 2) + "]/table/tbody/tr/td[5]/text()");
                String timeIntStr = strToDateString(timeInts.get(i)) + "";

                String[] columns = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月",};

                for (int j = 0; j < fields.size(); j++) {
                    dataMap.put(columns[i], fields.get(i));
                }
                //插入数据库
                dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), dataMap);
            }
        }catch (Exception e){
            RecordCrawlResult.recordFailData(ICDXDataFetch.class.getName(), varName, cnName, "爬取页面出错！");
        }

    }

    /**
     * 通过xpath获取页面相应的元素列表的文本内容
     * @param document
     * @param xpath
     * @return
     */
    public List<String> getFieldsByXpath(JXDocument document,String xpath){
        List<String> fields=new ArrayList<>();
        try {
            List list=document.sel(xpath);
            for(Object o:list){
                fields.add(o.toString());
            }
        } catch (XpathSyntaxErrorException e) {
            LOG.error("解析xpath:  "+xpath+"   错误！！！！！！！！！----------------------------");
        }
        return fields;
    }

    /**
     * 将网页html下载到本地
     */
    public void downloadPage(String html){

        try {
            OutputStream output=new FileOutputStream("\\test.html",true);
            output.write(html.getBytes());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 Feb 19，2017格式的字符串转换为int型时间数字串
     * @param str 需要格式化的字符串
     * @return 时间数字
     * @throws Exception
     */
    public int strToDateString(String str) throws Exception {
        try {
            Date date=format.parse(str);
            return Integer.parseInt(format2.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("时间字符串解析错误！！");
        }
    }

}
