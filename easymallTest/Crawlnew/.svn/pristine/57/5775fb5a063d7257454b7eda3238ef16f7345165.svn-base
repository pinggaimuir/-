package cn.futures.data.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class MyWebDriver {
	
	private Log logger = LogFactory.getLog(MyWebDriver.class);
	
	/**
	 * 当网页数据是通过js动态生成时，通过Selenium库获取HTML页面内容
	 * @param url
	 * @param enableJavaScript
	 * @return
	 */
	public String getHtmlBySelenium(String url, boolean enableJavaScript){
		String htmlContent = "";
		try{
			WebDriver driver = new HtmlUnitDriver(enableJavaScript);
			driver.get(url);
			htmlContent = driver.getPageSource();
		}catch(Exception e){
			logger.warn("通过WebDriver获得网页html内容时异常");
		}
		return htmlContent;
	}
}
