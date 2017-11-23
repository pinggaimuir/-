package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.UnitConvUtil;

/**
 * 海关总署：进出口数据
 * @author ctm
 *
 */
public class CustomsIOData {
	private static final String className = CustomsIOData.class.getName();
	private Log logger = LogFactory.getLog(CustomsIOData.class);
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String url = "http://www.customs.gov.cn/publish/portal0/tab49667/";
	private Map<String, String> imp_varAcnName_map = new HashMap<String, String>(){
		{
//			put("稻谷和大米", "稻谷-进口-千吨");
			put("花生油", "花生油-进口-吨");
			put("棉纱线", "棉纱-进口单价,纱进口量-万吨");
			put("氮、磷、钾复合肥", "复合肥-进口量,进口额-万吨");
			put("尿素", "氮肥-尿素进口单价,尿素进口量,尿素进口额-万吨");
			put("氯化钾", "钾肥-氯化钾进口单价,氯化钾进口额-万吨");//特殊
			put("硫酸钾", "钾肥-硫酸钾进口单价,硫酸钾进口量,硫酸钾进口额-吨");
		}
	};
	private Map<String, String> exp_varAcnName_map = new HashMap<String, String>(){
		{
			put("稻谷和大米", "稻谷-出口-千吨");
			put("花生油", "花生油-出口-吨");
			put("菜子油和芥子油", "菜籽油-出口-吨");
			put("豆油", "豆油-出口-吨");
			put("玉米", "玉米-出口-万吨");
			put("棉纱线", "棉纱-出口单价,纱出口量-万吨");
			put("活猪（种猪除外）", "生猪-活猪出口量（种猪除外）,活猪出口额（种猪除外）-万头");
			put("鲜蛋", "禽蛋-鲜蛋出口量,鲜蛋出口额-百万个");
			put("氮、磷、钾复合肥", "复合肥-出口量,出口额-万吨");
			put("尿素", "氮肥-尿素出口单价,尿素出口量,尿素出口额-万吨");
			put("氯化钾", "钾肥-氯化钾出口单价,氯化钾出口量,氯化钾出口额-吨");
			put("硫酸钾", "钾肥-硫酸钾出口单价,硫酸钾出口量,硫酸钾出口额-吨");
			put("茶叶", "中国茶叶-出口,出口额,出口单价-吨");
			put("服装及衣着附件","服装-出口总值");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_CUSTOM_IMPOREXP)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("海关总署：进出口数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到海关总署：进出口数据的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetch(date);
			}else{
				logger.info("抓取海关总署：进出口数据的定时器已关闭");
			}
		}
	}
	
	private void fetch(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String monthStr = DateTimeUtil.formatDate(DateTimeUtil.addMonth(date, -1), "yyyy年M月");
		String timeIntSave = DateTimeUtil.formatDate(DateTimeUtil.addMonth(date, -1), "yyyyMM");
		logger.info("======开始抓取海关总署进出口数据"+timeInt+"=====");
		String[] filters = {"table", "id", "ess_ctr126625_ListC_Info_LstC_Info"};
		int[] matchGroup = {1,3};
		String contents = fetchUtil.getPrimaryContent(0, url, "utf-8", "海关总署进出口", filters, null, 0);
		if(contents != null && !contents.equals("")){
			String[] lis = contents.split("	<li class=\"liebiaoys24\">");
			for(String li:lis){
				String comp="href=\"([^\"]+)\"(.+)"+monthStr+"(进口|出口)主要商品量值表（美元值）";
				List<String> results = fetchUtil.getMatchStr(li, comp, matchGroup);
				if(results.size()>0){
					fetchIO(results, timeIntSave);
				}
			}
		}
	}
	private void fetchHis(){
		String url = "http://www.customs.gov.cn/publish/portal0/tab49667/module126625/page%p%.htm";
		String[] filters = {"table", "id", "ess_ctr126625_ListC_Info_LstC_Info"};
		int[] matchGroup = {1,3,4};
		for(int page=1;page<33;page++){
			String contents = fetchUtil.getPrimaryContent(0, url.replace("%p%", page+""), "utf-8", "海关总署进出口", filters, null, 0);
			if(contents != null && !contents.equals("")){
				String[] lis = contents.split("	<li class=\"liebiaoys24\">");
				for(String li:lis){
					String comp="href=\"([^\"]+)\"(.+)(\\d{4}年\\d+月)(出口)主要商品量值表（美元值）";
					List<String> results = fetchUtil.getMatchStr(li, comp, matchGroup);
					if(results.size()>0){
						String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(results.get(1), "yyyy年M月"), "yyyyMM");
						results.remove(1);
						fetchIO(results, timeInt);
					}
				}
			}
		}
	}
	private void fetchIO(List<String> results, String timeInt) {
		String[] filterIO = {"span","id","zoom"};
		Map<String, String> dataMap = new HashMap<String, String>();
		Map<String, String> expOrImpMap = new HashMap<String, String>();
		String expOrImpStr;
		String urlTmp = results.get(0);
		String pageUrl = url+urlTmp.substring(urlTmp.lastIndexOf("/")+1);
		String contentIO = fetchUtil.getPrimaryContent(0, pageUrl, "utf-8", "海关总署进出口", filterIO, null, 0);
		if(contentIO != null && !contentIO.equals("")){
			contentIO = contentIO.replaceAll("(<[^>]+>)", ";").replaceAll("&nbsp;","").replaceAll("(\\s+)", "").replaceAll("([;]+)", ";");
			String[] fields = contentIO.split(";");
			if(results.get(1).equals("进口")){
				expOrImpMap = imp_varAcnName_map;
				expOrImpStr = "进口";
			}else{
				expOrImpMap = exp_varAcnName_map;
				expOrImpStr = "出口";
			}
			for(int i=0;i<fields.length;i++){
				String code = fields[i].trim();
				if(expOrImpMap.keySet().contains(code)){
					dataMap.clear();
					String[] varAcnNames = expOrImpMap.get(code).split("-");
					String varName = varAcnNames[0];
					int varId = Variety.getVaridByName(varName);
					String[] cnNames = varAcnNames[1].split(",");
					if(varAcnNames.length==2){//服装及衣着附件特殊
						String moneyTmp = fields[i+3].replaceAll(",","").trim();
						dataMap.put("全国", moneyTmp);
						dao.saveOrUpdateByDataMap(varId, cnNames[0], Integer.parseInt(timeInt), dataMap);
						continue;
					}
					String dbUnit = varAcnNames[2];
					String unit = fields[i+1].trim();//网页上数据单位
					String amountTmp = fields[i+2].replaceAll(",","").trim();//数量
					String moneyTmp = fields[i+3].replaceAll(",","").trim();//金额
					String amount,money;
					if(!unit.equals(dbUnit)){//数据单位换算
						Double convTmp = UnitConvUtil.getConvData(Double.parseDouble(amountTmp), unit, dbUnit);
						if(convTmp == null){
							logger.info(code+"单位换算失败，不能保存");
							continue;
						}
						amount = convTmp+"";
					}else{
						amount = amountTmp+"";
					}
					money = Double.parseDouble(moneyTmp)/10+"";//金额单位换算  千美元->万美元
					for(String cnName:cnNames){
						if(cnName.indexOf(expOrImpStr+"额")!=-1){
							dataMap.put("全国", money);
						}else if(cnName.indexOf(expOrImpStr+"量")!=-1){
							dataMap.put("全国", amount);
						}else  if(cnName.indexOf(expOrImpStr+"单价")!=-1){//单位：美元/吨
							if(Double.parseDouble(amount)>0){
								Double val = (Double.parseDouble(money)*1e4)/(Double.parseDouble(amount)*UnitConvUtil.unitMap.get(dbUnit));
								dataMap.put("全国", val+"");
							}else{
								dataMap.put("全国", "0");
							}
						}else{
							dataMap.put("全国", amount);
						}
						logger.info(varName+cnName+dataMap.get("全国"));
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
					}
				}
			}
			
		}		
	}

	public static void main(String[] args){
//		new CustomsIOData().fetch(new Date());
		new CustomsIOData().start();
	}
}
