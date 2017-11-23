package wusongSpider;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.Augmenter;
import wusongSpider.dao.HBaseDao;
import wusongSpider.domain.WuSongInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 高健 on 2017/3/9.
 */
public class WuSongSpider {
    public static void main(String[] args) {
        HBaseDao hBaseDao = new HBaseDao();
        System.setProperty("phantomjs.binary.path", "J:/soft/phantomjs-2.1.1/phantomjs-2.1.1-windows/bin/phantomjs.exe");
        WebDriver driver = new PhantomJSDriver();
        try {
            //抛异常前等待5秒
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            driver.get("http://www.itslaw.com/detail?judgementId=02025009-03d7-42c7-83dd-35a3de7a4e67&area=" +
                    "1&index=1&sortType=1&count=2931975&conditions=keyword%2B18880%2B3%2B%E5%80%9F%E8%B4%B7");
            Thread.sleep(5000);
            getScreenShot(driver, "jietu");
            WebElement htmlElement = driver.findElement(By.xpath("/html"));
            JXDocument document = new JXDocument(Jsoup.parse(htmlElement.getAttribute("outerHTML")));
            hBaseDao.insertData(parsePage(document));
            //获取页面数量
            String totalPageStr = getInfoByXpath(document, "//div[@class='col-md-8 list-nav']/span[@class='nav-link']/span[2]/text()");
            int totalPage = Integer.valueOf(totalPageStr);
            for (int i = 0; i < totalPage; i++) {
                driver.findElement(By.xpath("//nav/div/section[@class='header-11-sub dm-controlsView-holder']/div/div[@class='col-md-8 list-nav']/a[contains(span/text(),'下一篇')]")).click();
                Thread.sleep(5000);
                WebElement html = driver.findElement(By.xpath("/html"));
                document = new JXDocument(Jsoup.parse(html.getAttribute("outerHTML")));
                WuSongInfo wusong = parsePage(document);
                //存进hbase
                hBaseDao.insertData(wusong);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            hBaseDao.close();
            driver.close();
        }
    }
    //解析详情页面
    public static WuSongInfo parsePage(JXDocument document){
        WuSongInfo wusong=new WuSongInfo();
        //获取标题
        String title=getInfoByXpath(document,"//div[@class='list-item first-list-item']/div[1]/text()");
        wusong.setTitle(title);
        System.out.println(title);
        //法院
        String court=getInfoByXpath(document,"//span[@ng-if='judgementInfo.court.name']/text()");
        System.out.println(court);
        wusong.setCourt(court);
        //几审
        String trialRound=getInfoByXpath(document,"//span[@ng-if='judgementInfo.trialRound']/text()");
        System.out.println(trialRound);
        wusong.setTrialRound(trialRound);
        //caseNumber
        String caseNumber=getInfoByXpath(document,"//span[@ng-if='judgementInfo.caseNumber']/text()");
        System.out.println(caseNumber);
        wusong.setCaseNumber(caseNumber);
        //关键词
        String keyWord=getInfoListByXpath(document,"//span[@class='dtd-item ng-binding ng-scope']/text()");
        System.out.println(keyWord);
        wusong.setKeyWord(keyWord);
        //期刊号
        String issueNumber=getInfoByXpath(document,"//span[@ng-bind='judgementInfo.publishBatch']/text()");
        System.out.println("期刊号"+issueNumber);
        wusong.setIssueNumber(issueNumber);
//        //历审案例
//        String deskCase=document.sel("//span[@class='dtd-title']/span[@class='property-tag ng-binding']/text()").get(0).toString();
//        System.out.println(deskCase);
        //裁判摘要
        String judgementAbstract=getInfoByXpath(document,"//div[@ng-bind='judgementInfo.judgementAbstract']/text()");
        System.out.println("裁判摘要:"+judgementAbstract);
        wusong.setJudgementAbstract(judgementAbstract);
        //引用法规
        String regulationItem =getInfoListByXpath(document,"//div[@class='regulation-item']/span[@ng-bind='regulation.text']/text()");
        System.out.println("引用法规:"+regulationItem);
        wusong.setRegulationItem(regulationItem);
        //当事人信息
        String litigant=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='当事人信息']/section/span/span/text()");
        System.out.println("当事人信息:"+litigant);
        wusong.setLitigant(litigant);
        //审理经过
        String afterTheTrial=getInfoByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='审理经过']/section/span/span/text()");
        System.out.println("审理经过:"+afterTheTrial);
        wusong.setAfterTheTrial(afterTheTrial);
        //一审法院查明
        String courtIdentified=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='一审法院查明']/section/span/span/text()");
        System.out.println("一审法院查明:"+courtIdentified);
        wusong.setCourtIdentified(courtIdentified);
        //被上诉人辩称
        String argument=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='被上诉人辩称']/section/span/span/text()");
        System.out.println("被上诉人辩称："+argument);
        wusong.setArgument(argument);
        //本院查明
        String hasIdentified=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='本院查明']/section/span/span/text()");
        System.out.println("本院查明："+hasIdentified);
        wusong.setHasIdentified(hasIdentified);
        //本院认为
        String courtThink=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='本院认为']/section/span/span/text()");
        System.out.println("本院认为："+courtThink);
        wusong.setCourtThink(courtThink);
        //二审裁判结果
        String secondResult=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='二审裁判结果']/section/span/span/text()");
        System.out.println("二审裁判结果："+secondResult);
        wusong.setSecondResult(secondResult);
        //审判人员
        String theJudge=getInfoListByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='审判人员']/section/span/span/text()");
        System.out.println("审判人员："+theJudge);
        wusong.setTheJudge(theJudge);
        //裁判日期
        String refereeDate=getInfoByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='裁判日期']/section/span/span/text()");
//        Date date=stringToDate(refereeDate);
        System.out.println("裁判日期："+refereeDate);
        wusong.setRefereeDate(refereeDate);
        //书记员
        String clerk=getInfoByXpath(document,"//section[@ng-mouseup='showSearchDialogAfterSelectedTxt($event)']/article[header/text()='书记员']/section/span/span/text()");
        System.out.println("书记员："+clerk);
        wusong.setClerk(clerk);
        //案例自评
        String evaluation=getInfoByXpath(document,"//aside[@class='detail-left-siderbar']/article/article[@class='list-item']/div/text()");
        System.out.println("案例自评："+evaluation);
        wusong.setEvaluation(evaluation);
        return wusong;
    }
    //截图
    public static void getScreenShot(WebDriver driver,String name){
        WebDriver augmentDriver=new Augmenter().augment(driver);
        File output=((TakesScreenshot)augmentDriver).getScreenshotAs(OutputType.FILE);
        File file=new File("D:/",name+".jpg");
        try {
            FileUtils.copyFile(output,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //通过xpath获取节点列表
    public static String getInfoListByXpath(JXDocument document,String xpath){
        try {
            List list = document.sel(xpath);
            StringBuilder info=new StringBuilder();
            for(Object o:list){
                info.append(o.toString()+" ");
            }
            return info.toString();
        }catch (Exception e){
//            throw new RuntimeException("解析xpath出错：---------"+xpath+"----------");
            return "";
        }
    }
    //通过xpath获取单节点信息
    public static String getInfoByXpath(JXDocument document,String xpath){
        try {
            String info = document.sel(xpath).get(0).toString().trim();
            return info;
        }catch (Exception e){
//            throw new RuntimeException("解析xpath出错：---------"+xpath+"----------");
            return "";
        }
    }
    public static Date stringToDate(String dateString){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date=format.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("解析日期格式错误："+dateString);
        }
    }
    /**
     * 将网页html下载到本地
     */
    public static void downloadPage(WebDriver driver){

        try {
            OutputStream output=new FileOutputStream("test.html",true);
            output.write(driver.getPageSource().getBytes());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
