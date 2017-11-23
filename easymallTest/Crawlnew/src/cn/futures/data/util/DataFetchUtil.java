package cn.futures.data.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;
import cn.futures.data.util.ProxyManagerUtil;

public class DataFetchUtil {
	
	private static Logger logger = Logger.getLogger(DataFetchUtil.class);
	private MyHttpClient myHttpClient = new MyHttpClient();
	private MyWebDriver myWebDriver = new MyWebDriver();
	private boolean useProxy = false;
	
	public DataFetchUtil(){}
	
	public DataFetchUtil(boolean enabledProxy){
		useProxy = enabledProxy;
	}
	
	/**
	 * 非数字的字符串处理（直接截取从开始位置到第一个字母出现位置的字符串）
	 */
	public String getDigitByStr(String str){
		Matcher matcher = Pattern.compile("[^.0-9]").matcher(str);
		if(matcher.find()){
			String digitStr = str.substring(0, matcher.start());
			if(digitStr.equals("")){
				return null;
			}else{
				return digitStr;
			}
		}
		return str;
	}
	
	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str){
		if(str.equals("")) return false;
	    Pattern pattern = Pattern.compile("[.|0-9|-]*"); 
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	    	return false; 
	    } 
	    return true; 
	}
	
	/**
	 * 当网页数据是通过js动态生成时，通过Selenium库获取HTML页面内容，再通过parser解析html页面内容
	 * @param pageURL   网页链接
	 * @param encoding  编码方式
	 * @param enableJavaScript 是否启用JavaSript
	 * @return parser
	 */
	private Parser getParserBySelenium(String pageURL, String varName, String encoding, boolean enableJavaScript) { 
		logger.info("尝试下载页面："+pageURL);
		Parser parser;
		int i=0;
		while(true){
			try{ 
				String htmlContent = myWebDriver.getHtmlBySelenium(pageURL, enableJavaScript);
				saveHtml(pageURL, varName, htmlContent);
				parser = Parser.createParser(htmlContent, encoding); 
				logger.info("下载完成");
				return parser;
			}catch(Exception e){
				i++;
				if(i>5){
					logger.warn("重试了5次了，还是不行，退出");
					return null;
				}
				logger.warn("Parser解析WebDriver抓取的网页时异常", e);
			}
		}
	}
	/**
	 * get请求访问https链接获取HTML页面内容，再通过parser解析html页面内容
	 * @param url   网页链接
	 * @param varName 品种名
	 * @param encoding  编码方式
	 * @return parser
	 */
	private Parser getHttpsGetParser(String url, String varName, String encoding) { 
		logger.info("尝试下载页面："+url);
		Parser parser;
		int i=0;
		while(true){
			try{ 
				String htmlContent = MyHttpClient.getStringByHttps(url, encoding);
				saveHtml(url, varName, htmlContent);
				parser = Parser.createParser(htmlContent, encoding); 
				logger.info("下载完成");
				return parser;
			}catch(Exception e){
				i++;
				if(i>5){
					logger.warn("重试了5次了，还是不行，退出");
					return null;
				}
				logger.warn("Parser解析抓取的网页时异常", e);
			}
		}
	}

	/**
	 * 通过HttpClient获取HTML页面内容，再解析
	 */
	private Parser getParser(String pageURL, String varName, String encoding) { 
		logger.info("尝试下载页面："+pageURL);
		Parser parser;
		HashSet<String> proxyUnusable = new HashSet<String>();
		int proxyTryTimes = 0;	//尝试的代理的次数
		//利用HttpClient得到页面内容
		String proxyStr = "";
		if(useProxy){
			proxyStr = ProxyManagerUtil.getProxyStr();
			proxyUnusable.add(proxyStr);
			proxyTryTimes++;
		}
		while(true){
			logger.info("使用代理：" + proxyStr);
			String htmlContent = myHttpClient.getHtmlByHttpClient(pageURL, encoding, proxyStr);
			if("0123".contains(htmlContent) || htmlContent.indexOf("an error occurred while processing this directive") != -1){
				if(useProxy){
					while(proxyUnusable.contains(proxyStr) && proxyTryTimes <= 30){//最多换30次代理
						ProxyManagerUtil.changeProxy();
						proxyStr = ProxyManagerUtil.getProxyStr();
						proxyTryTimes++;
					}
					proxyUnusable.add(proxyStr);
					if(proxyTryTimes < 30){//最多 换30次代理
						continue;
					}
				}
				return null;
			}
			saveHtml(pageURL, varName, htmlContent);
			parser = Parser.createParser(htmlContent, encoding); 
			logger.info("下载完成");
			return parser;
		}
	}	
	
	/**
	 * 获取指定网页的内容
	 * @param pageUrl
	 * @param varName 品种名
	 * @param encoding 网页编码
	 * @author bric_yangyulin
	 * @date 2016-05-25
	 * */
	public String getContent(String pageURL, String varName, String encoding){
		 
		logger.info("尝试下载页面："+pageURL);
		Set<String> proxyUnusable = new HashSet<String>();
		int proxyTryTimes = 0;	//尝试的代理的次数
		//利用HttpClient得到页面内容
		String proxyStr = "";
		if(useProxy){
			proxyStr = ProxyManagerUtil.getProxyStr();
			proxyUnusable.add(proxyStr);
			proxyTryTimes++;
		}
		while(true){
			logger.info("使用代理：" + proxyStr);
			String htmlContent = myHttpClient.getHtmlByHttpClient(pageURL, encoding, proxyStr);
			if("0123".contains(htmlContent) || htmlContent.indexOf("an error occurred while processing this directive") != -1){
				if(useProxy){
					while(proxyUnusable.contains(proxyStr) && proxyTryTimes <= 30){//最多换30次代理
						ProxyManagerUtil.changeProxy();
						proxyStr = ProxyManagerUtil.getProxyStr();
						proxyTryTimes++;
					}
					proxyUnusable.add(proxyStr);
					if(proxyTryTimes < 30){//最多 换30次代理
						continue;
					}
				}
				return null;
			}
			saveHtml(pageURL, varName, htmlContent);
			logger.info("下载完成");
			return htmlContent;
		}
	
	}
	/**
	 * 
	 * @param getHTMLMethod  判断获取HTML页面内容的方法0：HttpClient 1:WebDriver 2：get请求访问https链接
	 * @param url            网页链接
	 * @param encoding       编码方式
	 * @param varName        品种名称
	 * @param filters         过滤属性，固定格式：【标签名，子标签名】或者【标签名，属性，属性值】
	 * @param rowColChoose   选择节点的行列号：【"0","110"】表示抛弃第一行抛弃第三列
	 * 						 rowColChoose=null表示不查找行列值，直接返回html值
	 * @param elementAtIndex 元素所在位置，默认0
	 * @return
	 */
	public String getPrimaryContent(int getHTMLMethod, String url, String encoding, String varName, String[] filters, 
			String[] rowColChoose, int elementAtIndex){
		logger.info("尝试下载页面getPrimaryContent："+url);
		Parser parser = new Parser();
		NodeList tableList;
		try {
			if(getHTMLMethod == 0){
				parser = getParser(url, varName, encoding);
			}else if(getHTMLMethod == 1){
				parser = getParserBySelenium(url, varName, encoding, true);
			}else if(getHTMLMethod == 2){
				parser = getHttpsGetParser(url, varName, encoding);
			}else{
				parser = null;
			}
			if(parser == null){
				logger.error("抓取 "+varName+" 时出错。");
				return null;
			}
			TagNameFilter tableFilter = new TagNameFilter(filters[0]);
			AndFilter andFilter = null;
			if(filters.length == 2){
				andFilter = new AndFilter(tableFilter, new HasChildFilter(new TagNameFilter(filters[1])));
			}else if(filters.length == 3){
				andFilter = new AndFilter(tableFilter, new HasAttributeFilter(filters[1], filters[2]));
			}
			NodeList nodes = null;
			if(andFilter != null){
				nodes = parser.extractAllNodesThatMatch(andFilter);
			}else{
				nodes = parser.extractAllNodesThatMatch(tableFilter);
			}
			if(nodes.size() != 0){
				tableList = nodes.elementAt(elementAtIndex).getChildren();
			}else{
				logger.error("抓取"+varName+"时未找到过滤节点");
				return null;
			}
			if(rowColChoose == null){
				return tableList.toHtml();
			}
			return HtmlNodeListUtil.table2Str_SpecifyRowsCols(tableList, rowColChoose[0], rowColChoose[1]);
		} catch (ParserException e) {
			logger.error("ParserException while fetching "+varName+":", e);
		} catch (NullPointerException e) {
			logger.error("error kind:"+varName, e);
		}
		return null;
	}
	/**
	 * 将p添加到list,前提是p的开盘价与收盘价均不为零
	 * @param list
	 * @param p
	 */
	public void addList(List<MarketPrice> list, MarketPrice p){
		if (p == null) {
			return;
		}
		if (CrawlerUtil.isEmpty(p)){
			logger.info("skip empty");
			return;
		}
		list.add(p);
	}
	
	/**
	 * 通过正则表达式匹配需要的字符串
	 * @param contents 被查找的字符串
	 * @param compStr  正则表达式
	 * @param index    返回的字符串序号
	 * @return
	 */
	public List<String> getMatchStr(String contents, String compStr, int[] index){
		List<String> results = new ArrayList<String>();
		Pattern pattern = Pattern.compile(compStr);
		Matcher matcher = pattern.matcher(contents);
		if(matcher.find()){
			if(index == null)
				results.add(matcher.group(1));
			else{
				for(Integer i:index){
					results.add(matcher.group(i));
//					logger.info(matcher.group(i));
				}
			}
		}else{
			  logger.error(" 通过正则表达式未匹配到需要的字符串");  
		}
		return results;
	}
	
	/**
	 * 
	 * @param getHTMLMethod
	 * @param url
	 * @param encoding
	 * @param varName
	 * @param filters
	 * @param rowColChoose
	 * @param elementAtIndex
	 * @param params
	 * @return
	 */
	public String getPostPrimaryContent(int getHTMLMethod, String url, String encoding, String varName, String[] filters, 
			String[] rowColChoose, int elementAtIndex, Map<String, String> params){
		Parser parser = new Parser();
		NodeList tableList;
		try {
			logger.info(url);
			String htmlContent = null;
			if(getHTMLMethod == 0){
				htmlContent = myHttpClient.getPostHtmlByHttpClient(url, params, encoding);
			}
			if(htmlContent.equals("")){
				logger.info("抓取的HTML为空");
				return null;
			}
			parser = Parser.createParser(htmlContent, encoding);
			if(parser == null){
				logger.error("抓取 "+varName+" 时出错。");
				return null;
			}
			saveHtml(url, varName, htmlContent);
			TagNameFilter tableFilter = new TagNameFilter(filters[0]);
			AndFilter andFilter = null;
			if(filters.length == 2){
				andFilter = new AndFilter(tableFilter, new HasChildFilter(new TagNameFilter(filters[1])));
			}else if(filters.length == 3){
				andFilter = new AndFilter(tableFilter, new HasAttributeFilter(filters[1], filters[2]));
			}
			NodeList nodes = null;
			if(andFilter != null){
				nodes = parser.extractAllNodesThatMatch(andFilter);
			}else{
				nodes = parser.extractAllNodesThatMatch(tableFilter);
			}
			if(nodes.size() != 0){
				tableList = nodes.elementAt(elementAtIndex).getChildren();
			}else{
				logger.error("抓取"+varName+"时未找到过滤节点");
				return null;
			}
			if(rowColChoose == null){
				return tableList.toHtml();
			}
			return HtmlNodeListUtil.table2Str_SpecifyRowsCols(tableList, rowColChoose[0], rowColChoose[1]);
		} catch (ParserException e) {
			logger.error("ParserException while fetching "+varName+":", e);
		} catch (NullPointerException e) {
			logger.error("error kind:"+varName, e);
		}
		return null;
	}
	
	/**
	 * 将tableList网页内容保存在以网页链接命名的文本文件中，以备后期查看
	 */
	public static void saveHtml(String url, String varName, String html){
		String dateStr = DateTimeUtil.formatDate(new Date(), "yyyy:MM:dd HH:mm:ss");
		String dirString = Constants.SAVEDHTML_ROOT+Constants.FILE_SEPARATOR+DateTimeUtil.formatDate(new Date(), "yyyyMMdd")+Constants.FILE_SEPARATOR;
		try {
			if (new File(dirString + varName + ".txt").exists())
				logger.warn("Overwrite html: "+ varName);
//			html = html.replaceAll("<[^>]*>", "");
			FileStrIO.appendStringToFile(url + "("+dateStr+")=>" + html, dirString, varName + ".txt", null);
			//目前本程序只做到下载持仓数据，对于下载下来的数据还未进行处理。
			logger.info("html saved: " + varName + "@" + url);
		} catch (IOException e) {
			logger.error("IOException while saving " + varName +" data:", e);
		}
	}
	/**
	 * 将一整段内容按长度分割成多行内容
	 * @param contents
	 * @param size
	 * @return
	 */
	public String getLineContent(String contents, int size){
		String[] fields = contents.split(",");
		String sepStrs = "";
		String sepStr = "";
		for(int i=0;i<fields.length;i++){
			if(i>0 && i%size==0){
				sepStrs += sepStr+"\n";
				sepStr = fields[i]+",";
			}else{
				sepStr+=fields[i]+",";
			}
		}
		sepStrs += sepStr+"\n";
		return sepStrs;
	}
	/**
	 * 将一整段内容按长度分割成多行内容
	 * @param contents
	 * @param size
	 * @return
	 */
	public String getLineContent(String contents, int size, String needTime){
		String[] fields = contents.split(",");
		String sepStrs = "";
		String sepStr = "";
		for(int i=0;i<fields.length;i++){
			if(i>0 && i%size==0){
				if(sepStr.indexOf(needTime) == -1) break;
				sepStrs += sepStr+"\n";
				sepStr = fields[i]+",";
			}else{
				sepStr+=fields[i]+",";
			}
		}
		return sepStrs;
	}
	/**
	 * 获取网页完整内容
	 * int method 标识抓取网页的方法
	 * @param url
	 * @param encoding 编码
	 * @param fileName 保存的文件名
	 * */
	public String getCompleteContent(int method, String url, String encoding, String fileName){
		String content = null;
		if(method == 0){
			content = myHttpClient.getHtmlByHttpClient(url, encoding, "");
		} else if(method == 1) {
			content = myWebDriver.getHtmlBySelenium(url, true);
		} else if(method == 2) {
			content = MyHttpClient.getStringByHttps(url, encoding);
		}
		if(fileName != null){
			saveHtml(url, fileName, content);
		}
		return content;
	}
	/**
	 * 获取网页完整内容（当需要先登录或者需要推荐页面时）
	 * @param urlBef 推荐页面或者登录页面
	 * @param urlAfter 目的页面
	 * @param encoding 编码
	 * @param params 访问推荐页面时的参数（比如用户名、密码）
	 * */
	public String getCompleteContent(String urlBef, String urlAfter, String fileName, String encoding, Map<String, String> params){
		String cont = MyHttpClient.getResoucesByLoginCookies(urlBef, urlAfter, encoding, params);
		saveHtml(urlAfter, fileName, cont);
		return cont;
	}
	/**
	 * 获取网页完整内容
	 * int method 标识抓取网页的方法
	 * @param url
	 * @param encoding 编码
	 * @param fileName 保存的文件名
	 * @param params post请求参数列表
	 * */
	public String getCompleteContent(int method, String url, String encoding, String fileName, Map<String, String> params){
		String content = null;
		if(method == 0){
			content = myHttpClient.getPostHtmlByHttpClient(url, params, encoding);
		}
		saveHtml(url, fileName, content);
		return content;
	}
	
	/**
	 * @param tarUrl 目标地址
	 * @param postJson json格式参数
	 * @param contentType 如："application/json;charset=utf-8"
	 * @param encoding 编码
	 * @param fileName 文件名
	 * */
	public String httpsPostJson(String tarUrl, String postJson, String contentType, String encoding, String fileName){
		String content = null;
		content = MyHttpClient.httpsPostJson(tarUrl, postJson, contentType, encoding);
		saveHtml(tarUrl, fileName, content);
		return content;
	}
	
	/**
	 * 解析html源码
	 * */
	public String parseContent(String htmlContent, String encoding, String[] filters, int elementAtIndex, String[] rowColChoose){
		Parser parser = new Parser();
		NodeList tableList;
		parser = Parser.createParser(htmlContent, encoding);
		TagNameFilter tableFilter = new TagNameFilter(filters[0]);
		AndFilter andFilter = null;
		if(filters.length == 2){
			andFilter = new AndFilter(tableFilter, new HasChildFilter(new TagNameFilter(filters[1])));
		}else if(filters.length == 3){
			andFilter = new AndFilter(tableFilter, new HasAttributeFilter(filters[1], filters[2]));
		}
		NodeList nodes = null;
		if(andFilter != null){
			try {
				nodes = parser.extractAllNodesThatMatch(andFilter);
			} catch (ParserException e) {
				logger.error("解析时发生异常", e);
			}
		}else{
			try {
				nodes = parser.extractAllNodesThatMatch(tableFilter);
			} catch (ParserException e) {
				logger.error("解析时发生异常", e);
			}
		}
		if(nodes.size() != 0){
			tableList = nodes.elementAt(elementAtIndex).getChildren();
			if(rowColChoose == null){
				return tableList.toHtml();
			} else {
				return HtmlNodeListUtil.table2Str_SpecifyRowsCols(tableList, rowColChoose[0], rowColChoose[1]);
			}
		}else{
			logger.error("未找到过滤节点");
			return null;
		}
	}
}
