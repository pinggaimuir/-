package cn.shoparound.utils;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * WebDriver实例相关
 * Created by 高健 on 2017/5/17.
 */
@Component
public class WebDriverUtil {

    public WebDriverUtil(){
        System.setProperty("webdriver.chrome.driver", "J:/soft/phantomjs-2.1.1/phantomjs-2.1.1-windows/bin/chromedriver.exe");
        System.setProperty("phantomjs.binary.path", "J:/soft/phantomjs-2.1.1/phantomjs-2.1.1-windows/bin/phantomjs.exe");
    }

    private static volatile  WebDriver driver = null;

    /**
     * 单例模式，获取WebDriver实例
     * @return
     */
    public WebDriver getDriver(){
        if (driver == null){
            synchronized(WebDriverUtil.class){
                if(driver == null){
                    driver=new ChromeDriver();
//                    driver = new PhantomJSDriver();
                }
            }
        }
        return driver;
    }

    /**
     * 通过webDriver获取对应的页面documet
     * @param timeout 爬取时抛异常前等待的时间
     * @return 爬取下的页面的jxdocument实例
     */
    public JXDocument getDocument(int timeout){
        //获取实例
        driver=this.getDriver();
        //抛异常前等待5秒
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        //获取页面
        WebElement htmlElement = driver.findElement(By.xpath("/html"));
        System.out.println(htmlElement.getText());
        Document doc=Jsoup.parse(htmlElement.getAttribute("outerHTML"));
        JXDocument document = new JXDocument(doc);
        return document;
    }

    //截图
    public  void getScreenShot(String name){
        WebDriver augmentDriver=new Augmenter().augment(driver);
        File output=((TakesScreenshot)augmentDriver).getScreenshotAs(OutputType.FILE);
        File file=new File("D:/",name+".jpg");
        try {
            FileUtils.copyFile(output,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
