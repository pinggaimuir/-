package cn.futures.data.importor.crawler.ymtCrawler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.crawler.usdaCrawler.USDAOnlineData;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.ImgRecogManager;

public class YMTSupplyDataFetch {
	private static final String className = YMTSupplyDataFetch.class.getName();
	private String url = "http://www.ymt.com/gongying";
	private DataFetchUtil fetchUtil = new DataFetchUtil(true);
	private Log logger = LogFactory.getLog(YMTSupplyDataFetch.class);
	private String[] filters = {"ul","class","table-box-con"};
	private String[] filterPhone = {"div","class","section"};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_YMT_SUPPLY_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("一亩田供应数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到一亩田供应数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				date = DateTimeUtil.addDay(date, -1);
				fetch(date);
			}else{
				logger.info("抓取一亩田供应数据的定时器已关闭");
			}
		}
	}
	
	/**
	 * 报价时间，产地，品种，规格，单价（元），供应商，电话
	 * @param date
	 */
	public void fetch(Date date){
		logger.info("=====开始抓取一亩田供应数据=====");
		for(String bigClass:ParamsMap.ymtcomKindReflectMap.keySet()){
			Map<String, String> smallClass2code = ParamsMap.ymtcomKindReflectMap.get(bigClass);
			for(String smallClass:smallClass2code.keySet()){
				String contentSave = "";
				String code = smallClass2code.get(smallClass);
				boolean lastPage = false;
				for(int page=0;page<100;page++){
					String pageUrl = url+"_"+code+"_0"+"/"+page*20;
					String contentTmp = fetchUtil.getPrimaryContent(0, pageUrl, "utf-8", smallClass, filters, null, 0);
					if(contentTmp != null && !contentTmp.equals("")){
						if(contentTmp.indexOf("暂时无此数据，点击返回")!=-1){
							logger.info(bigClass+smallClass+"没有数据需要抓取");
							break;
						}
						String pageContent = "";
						String[] lis = contentTmp.split("<li>");
						for(int i=1;i<lis.length;i++){
							String listContent = "";
							String li = lis[i];
							String[] spans = li.split("<span");
							for(int ii=2;ii<8;ii++){
								if(("<"+spans[1]).replaceAll("<[^>]+>", "").replaceAll("&nbsp;", "").trim().indexOf("天前")!=-1){
									lastPage = true;
									break;
								}
								if(spans[ii].indexOf("class=\"tab-tit\"") != -1) continue;
								if(spans[ii].indexOf("spec-info") != -1){
									String keyStr = ("<"+spans[ii]).replaceAll("<[^>]+>", "").trim().replaceAll("&nbsp;", "").replaceAll("(\\s+)", ",");
									if(keyStr.split(",").length>1){
										listContent += keyStr.split(",")[0]+"\t";
									}else{
										listContent += keyStr+"\t";
									}
									continue;
								}
								String keyStr = ("<"+spans[ii]).replaceAll("<[^>]+>", "").trim().replaceAll("&nbsp;", "").trim();
								listContent += keyStr+"\t";
							}
							if(lastPage) break;
							logger.info("详细信息抓取");
							String detail = fetchDetail(li);
							pageContent += listContent+ detail+"\n";
						}
						if(!pageContent.equals(""))
							contentSave += pageContent;
					}else{
						logger.error("没有抓取到"+bigClass+smallClass+"第"+page+"页的内容");
					}
					if(lastPage) {
						if(!contentSave.equals("")){
							String dirString = Constants.YMTSUPPLY_ROOT+Constants.FILE_SEPARATOR + bigClass + Constants.FILE_SEPARATOR + smallClass + Constants.FILE_SEPARATOR;
							saveToTxt(contentSave, dirString, smallClass, date);
						}else{
							logger.info(bigClass+smallClass+"没有数据需要保存");
						}
						break;
					}
				}
			}
		}
	}
	
	public void saveToTxt(String content, String dirString, String varName, Date date){
		try {
			String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
			if (new File(dirString + timeInt + ".txt").exists())
				logger.warn("Overwrite: "+ varName);
			FileStrIO.saveStringToFile(content, dirString,	timeInt + ".txt");
			//FileStrIO.appendStringToFile(content, dirString, varName + ".txt");
			logger.info("data saved: " + varName);
		} catch (IOException e) {
			logger.error("IOException while saving "+varName+" data:", e);
		}
		logger.info("====== "+ varName + " fetched ======");
	}
	/**
	 * 抓取供应商详情信息
	 * @param content
	 * @return
	 */
	private String fetchDetail(String content){
		String result = "";
		String comp = "href=\"([^\"]+)\"";
		List<String> matchStr = fetchUtil.getMatchStr(content, comp, null);
		if(matchStr.size()>0){
			//抓供应商详情
			String 	detailContent = fetchUtil.getPrimaryContent(0, matchStr.get(0), "utf-8", "供应商详情", filterPhone, null, 0);
			if(detailContent != null && !detailContent.equals("")){
				//一亩田来源的供应商电话号码已经不提供
				/*String phoneContent = detailContent.split("<div class=\"relate-info\">")[1];
				String compTmp = "src=\"([^\"]+)\"(.+)([0-9|\\*]{11})";
				int[] index = {1,3};
				List<String> matchPhone = fetchUtil.getMatchStr(phoneContent, compTmp, index);
				if(matchPhone.size()>0){
					String phone = matchPhone.get(1);
					String imgUrl = matchPhone.get(0);
		            String sName = imgUrl.substring(imgUrl.lastIndexOf("/")+1, imgUrl.length());  
		            logger.info("电话号码图片下载");
					ImgRecogManager.makeImg(imgUrl, Constants.YMTPHONE_ROOT+Constants.FILE_SEPARATOR, sName);
					logger.info("电话号码图片下载完成");
					try {
						BufferedImage image = ImageIO.read(new File(Constants.YMTPHONE_ROOT+Constants.FILE_SEPARATOR+sName));
						String recogResult = ImgRecogManager.process(image);
						if(recogResult == null){
							result += phone+"\t";
						}else{
							if(phone.indexOf("*") != -1){
								int start = phone.indexOf("*");
								int end = phone.lastIndexOf("*");
								result += phone.substring(0,start) + recogResult.substring(start, end+1) + phone.substring(end+1,phone.length())+"\t";
							}else{
								result += recogResult+"\t";
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("电话号码解析异常");
					}
				}else{
					logger.error("没有匹配到供应商电话号码");
				}*/
				//具体的地址
				String detailAddr = detailContent.split("<div class=\"tit-view\">")[1];
				result += detailAddr.substring(detailAddr.indexOf("<span class=\"text\">")+19, detailAddr.indexOf("</span>")).replaceAll("&nbsp;", "").trim()+"\t";
				//详细描述
				String detailDesc = detailContent.split("<div class=\"box-1\">")[1];
				result += detailDesc.substring(0, detailDesc.indexOf(" </div>")).replaceAll("<([^>]+)>", "").trim().replaceAll("(\\s+)", ",")+"\t";
			}else{
				logger.error("没有抓取到供应商详细信息");
			}
		}else{
			logger.error("没有匹配到供应商详细信息");
		}
		return result;
	}
	
	public static void main(String[] args){
		new YMTSupplyDataFetch().start();
	}
}
