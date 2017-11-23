package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.MapInit;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;
import net.sf.json.JSONArray;

/**
 * 中央气象台数据
 * @author ctm
 *
 */
public class ChinaWeatherData {
	private static final String className = ChinaWeatherData.class.getName();
	private String url = "http://www.nmc.cn/publish/forecast/%provCode%/%city%.html";
	private String hour24Url = "http://www.nmc.cn/f/rest/passed/#cityCode";//获取指定城市的24小时曲线图数据的链接
	private Log logger = LogFactory.getLog(ChinaWeatherData.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private MyHttpClient httpClient = new MyHttpClient();
	private DAOUtils dao = new DAOUtils();
	
	@Scheduled
	(cron=CrawlScheduler.CRON_WEATHER_DATA)
	public void start(){
		try{
			String switchFlag = new CrawlerManager().selectCrawler("中央气象台数据", className.substring(className.lastIndexOf(".")+1));
			if(switchFlag == null){
				logger.info("没有获取到中央气象台数据的定时器配置");
				RecordCrawlResult.recordFailData(ChinaWeatherData.className, null, null, "没有获取到中央气象台数据的定时器配置");
			}else{
				if(switchFlag.equals("1")){
					Date date = new Date();
					Calendar calendar = Calendar.getInstance(Locale.CHINA);
					calendar.setTime(date);
					int am_pm = calendar.get(Calendar.AM_PM);//0上午1下午
					fetchCurrWeather(date, am_pm);
					if(am_pm == 0){//只在上午那次定时时保存到数据库中，保存的前一天的数据。
						Date yest = DateTimeUtil.addDay(date, -1);//前一天
						save2db(new File(Constants.WEATHER_ROOT), DateTimeUtil.formatDate(yest, "yyyyMMdd"));
					}
				}else{
					logger.info("抓取中央气象台数据的定时器已关闭");
					RecordCrawlResult.recordFailData(ChinaWeatherData.className, null, null, "抓取中央气象台数据的定时器已关闭");
				}
			}
		} catch(Exception e){
			logger.error("抓取中央气象台数据时发生未知异常", e);
			RecordCrawlResult.recordFailData(ChinaWeatherData.className, null, null, "\"抓取中央气象台数据时发生未知异常" + e.getMessage() + "\"");
		}
	}
	
	public void fetchCurrWeather(Date date, int am_pm){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		if(am_pm == 0){//上午
			timeInt = DateTimeUtil.formatDate(DateTimeUtil.addDay(date, -1), "yyyyMMdd");//昨天的时间序列
		}
		logger.info("========start fetch weather data=========");
		Set<String> provSet = MapInit.weather_prov2city_map.keySet();
		logger.info(provSet.size()+"个省份");
		int provIndex=0;
		for(String provTmp:provSet){
			provIndex ++;
			String[] provAndCode = provTmp.split("-");
			String prov = provAndCode[0];
			String provCode = provAndCode[1];
			Map<String, String> cityMap = MapInit.weather_prov2city_map.get(provTmp);
			for(String cityTmp:cityMap.keySet()){
				try {
					Thread.sleep((int)(Math.random() * 1500));
				} catch (InterruptedException e1) {
					logger.error(e1.getMessage());
				}
				try{
					String pageUrl = url.replaceAll("%provCode%", provCode).replaceAll("%city%", cityMap.get(cityTmp));
					logger.info("start to fetch "+prov+cityTmp+"@"+pageUrl);
					String contents = httpClient.getHtmlByHttpClient(pageUrl,"utf-8","");
//					FileStrIO.saveStringToFile(contents, "D:\\test\\", cityTmp + ".txt");
					if(!contents.equals("") && contents != null){
						if(contents.equals("0")) {
							RecordCrawlResult.recordFailData(className, prov, cityTmp, "网页不存在");
							continue;
						}
						/*若为下午的那次定时则需要解析日长*/
						if(am_pm == 1){
							try{
								this.parseDayLength(contents, prov, cityTmp, timeInt);
							} catch(Exception e) {
								logger.error("解析日长时发生异常。"+ e.getMessage());
								RecordCrawlResult.recordFailData(ChinaWeatherData.className, prov, cityTmp, "解析日长时发生异常");
							}
						}
						/*解析24小时实况曲线数据*/
						String data24hourStr = parseHour24(contents, provTmp, cityTmp, am_pm);
						String dirString = Constants.WEATHER_ROOT + Constants.FILE_SEPARATOR + prov + Constants.FILE_SEPARATOR + cityTmp + Constants.FILE_SEPARATOR;
						try {
							if(data24hourStr.equals("")){
								logger.info(prov+"-"+cityTmp+"天气数据不存在");
								RecordCrawlResult.recordFailData(className, prov, cityTmp, "基本天气数据不存在");
								continue;
							}
							FileStrIO.appendStringToFile(data24hourStr, dirString, timeInt + ".txt", Constants.ENCODE_GB2312);
							logger.info("data saved: " + prov+"-"+cityTmp+"\n");
						} catch (Exception e) {
							logger.error("Exception while saving "+prov+"-"+cityTmp+" data:", e);
							RecordCrawlResult.recordFailData(className, prov, cityTmp, "\"保存至文件时发生异常。" + e.getMessage() + "\"");
						}
					}else{
						logger.error(prov+"-"+cityTmp+"不存在");
						RecordCrawlResult.recordFailData(ChinaWeatherData.className, prov, cityTmp, "数据不存在");
					}
				} catch(Exception e){//防止没想到的异常发生而因某个城市影响到其它城市的气象数据抓取。
					logger.error(String.format("抓取%s%s的气象数据时发生异常：%s", prov, cityTmp, e.getMessage()));
					RecordCrawlResult.recordFailData(ChinaWeatherData.className, prov, cityTmp, "\"发生异常。" + e.getMessage() + "\"");
				}
			}
			logger.info("========第"+provIndex+"个省份"+prov+" finished=========");
		}
		logger.info("========fetch weather data finished=========");
	}
	
	public void save2db(File file, String date){
		Map<String, String[]> currDataMap = new HashMap<String, String[]>();
		double maxT,minT,avgT,maxH,minH,avgH,airP;
		double sumT,sumP,sumH,sumAirP;//温度，降水，湿度，气压
		sumT=sumP=sumH=sumAirP=0;
		maxT=minT=maxH=minH=0;
		for(File file2:file.listFiles()){			
			if (file2.isDirectory()) {
				save2db(file2,date);
			}else {
				try{
					boolean isBaseDataExist = true;//是否存在基本气象数据。基本气象数据指气温、降水、湿度、气压
					currDataMap.clear();
					String cnName = file2.getParentFile().getName();//中文名
					String varName = file2.getParentFile().getParentFile().getName();//品种名
					int timeInt = Integer.valueOf(file2.getName().split("\\.")[0]);	//前一天的时间序列
					if (timeInt==Integer.valueOf(date)) {
						Map<String, String> dataMap = new HashMap<String, String>();
							InputStreamReader inputStreamReader;
							try {
								inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.ENCODE_GB2312);
								BufferedReader reader = new BufferedReader(inputStreamReader);
								String str = "";
								while((str=reader.readLine())!=null){
									if(!str.equals("")){
										if(!str.startsWith("#")){
											String[] fields = str.split(",");
											if(fields[0].equals("") || fields.length==1) 
												continue;   
											if(fields[1].equals("_")){//没有抓到气象数据
												isBaseDataExist = false;
											} else {
												currDataMap.put(fields[0], fields);
											}
										} else {
											/*处理日照时长和风力数据*/
											String columnPair[] = str.split("：");
											if(columnPair.length == 2 && !columnPair[1].equals("null")&& !columnPair[1].equals("")){
												String columnName = columnPair[0].substring(1);
												dataMap.put(columnName, columnPair[1]);
											}
										}
									}
								}
								reader.close();
							}catch(Exception e){
								e.printStackTrace();
							}
							if(isBaseDataExist){
								int i=0;
								int num=currDataMap.size();
//								if(num<24) {
//									errWeaData = errWeaData + varName+cnName+timeInt+"\n";
//								}
								for(String[] tmpArr:currDataMap.values()){
									if(tmpArr.length < 4) continue;
									double tmpT = 0;//温度
									double tmpP = 0;//降水量
									double tmpH = 0;//湿度
									double tmpAirP = 0;//气压
									if(!tmpArr[1].equals("") && !tmpArr[1].equals("null"))
										tmpT = Double.parseDouble(tmpArr[1]);
									if(!tmpArr[2].equals("") && !tmpArr[2].equals("null")){
										tmpP = Double.parseDouble(tmpArr[2]);
										//降水量大于1000属于异常数据
										if(tmpP>1000) continue;
									}
									if(!tmpArr[3].equals("") && !tmpArr[3].equals("null"))
										tmpH = Double.parseDouble(tmpArr[3]);
									if(!tmpArr[4].equals("") && !tmpArr[4].equals("null"))
										tmpAirP = Double.parseDouble(tmpArr[4]);
									sumT += tmpT;
									sumP += tmpP;
									sumH += tmpH;
									sumAirP += tmpAirP;
									//选取最大值
									if((i++)==0){
										maxT=minT=tmpT;
										maxH=minH=tmpH;
									}else{
										if(tmpT>maxT) maxT=tmpT;
										else if(tmpT<minT) minT = tmpT;
										if(tmpH>maxH) maxH=tmpH;
										else if(tmpH<minH) minH=tmpH;
									}
								}
								avgT = sumT/num;//平均温度
								avgH = sumH/num;//平均湿度
								airP = sumAirP/num;//平均气压
								dataMap.put("最高气温", maxT+"");
								dataMap.put("最低气温", minT+"");
								dataMap.put("平均气温", avgT+"");
								dataMap.put("降水", sumP+"");
								dataMap.put("最大相对湿度", maxH+"");
								dataMap.put("最小相对湿度", minH+"");
								dataMap.put("平均相对湿度", avgH+"");
								dataMap.put("气压", airP+"");
							} else {
								dataMap.put("最高气温",null);
								dataMap.put("最低气温",null);
								dataMap.put("平均气温", null);
								dataMap.put("降水", null);
								dataMap.put("最大相对湿度", null);
								dataMap.put("最小相对湿度",null);
								dataMap.put("平均相对湿度", null);
								dataMap.put("气压", null);
							}
							dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
					}
				} catch(Exception e) {
					logger.error("保存中国天气数据时发生未知异常。" + file2.getAbsolutePath());
					RecordCrawlResult.recordFailData(className, null, null, file2.getAbsolutePath() + "\"入库时发生未知异常。" + e.getMessage() + "\"");
				}
			}
		}
	}
	
	/**
	 * 解析日长
	 * */
	public void parseDayLength(String contents, String prov, String cityTmp, String timeInt){
		//从html中解析出计算日照时长的两个必要参数值,其在html中的格式为“sunriseset(116.87, 40.38);”
		String compStr = "sunriseset\\((\\d+\\.\\d*),\\s*(\\d+\\.\\d*)\\)";
		int[] index = {0, 1, 2};
		List<String> list = RegexUtil.getMatchStr(contents, compStr, index);
		if(list != null && list.size() >= 2){
			Double lat = null;
			Double lng = null;
			String latStr = list.get(1);
			String lngStr = list.get(2);
			try{
				if(latStr != null){
					lat = Double.parseDouble(latStr);
				}
				if(lngStr != null){
					lng = Double.parseDouble(lngStr);
				}
			} catch(Exception e) {
				logger.error("获取的计算日长的参数有问题。" + e.getMessage());
			}
			if(lat != null && lng != null){
				String sunshine = this.sunriseset(lat, lng);//日照时长
				if(sunshine != null && !sunshine.isEmpty()){
					String dirString = Constants.WEATHER_ROOT + Constants.FILE_SEPARATOR + prov + Constants.FILE_SEPARATOR + cityTmp + Constants.FILE_SEPARATOR;
					try {
						FileStrIO.appendStringToFile("#日照时长：" + sunshine, dirString, timeInt + ".txt", Constants.ENCODE_GB2312);
					} catch (IOException e) {
						logger.error("保存日照时长时发生异常。" + e.getMessage());
					}
				} else {
					logger.error("计算日长数据失败。");
				}
			} else {
				logger.error("获取日长数据参数失败。");
			}
		} else {
			logger.error("未获取到计算日长的参数。");
		}
	}
	/**
	 * 根据js中的方法而来
	 * @param lat html中抓得的参数
	 * @param lng html中抓得的参数
	 * @return String 日照时长
	 * @description 该方法以及其调用的一些方法为从js中照搬过来，具体变量含义计算逻辑不甚明了。可用浏览器调试查看
	 * */
	public String sunriseset(double lat, double lng){
		String sunshine = null;//日照时长
		Date now = new Date();//当前日期
		Calendar cNow = Calendar.getInstance();
		cNow.setTime(now);
		int d = cNow.get(Calendar.DATE);//日
		int m = cNow.get(Calendar.MONTH) + 1;//月
		int y = cNow.get(Calendar.YEAR);//年
		int z = 8;
		Map<String, String> obj = cal(mdj(d, m, y, 0.0), z, lat, lng);
		String sunrise = obj.get("rise");
		String sunset = obj.get("set");
		logger.info("日出时刻： " + sunrise);
		logger.info("日落时刻： " + sunset);
		DateFormat format = new SimpleDateFormat("HH:mm");
		try {
			Date date1 = format.parse(sunset);
			Date date2 = format.parse(sunrise);
			sunshine = DateTimeUtil.cha(date2, date1, 0);
		} catch (ParseException e) {
			logger.error("解析日出日落时间时发生异常。", e);
		}
		
		return sunshine;
		
	}
	
	/**
	 * 根据js中的方法而来
	 * */
	public Map<String, String> cal(double mjday, int tz, double glong, double glat){
		double sglong, sglat, date, ym, yz, utrise = 0.0f, utset = 0.0f, j;
		double yp, nz, hour, z1, z2, xe, ye, iobj, rads = 0.0174532925;
		double[] quadout = null;
		String always_up = "日不落";
		String always_down = "日不出";
		String outstring = "";
	    Map<String, String> resobj = new HashMap<String, String>();

		double sinho = Math.sin(rads * -0.833);		
		sglat = Math.sin(rads * glat);
		double cglat = Math.cos(rads * glat);
		date = mjday - tz/24.0;

		boolean rise = false;
		boolean sett = false;
		boolean above = false;
		hour = 1.0;
		ym = sin_alt(2, date, hour - 1.0, glong, cglat, sglat) - sinho;
		if (ym > 0.0) above = true;
		while(hour < 25 && (sett == false || rise == false)) {
			yz = sin_alt(2, date, hour, glong, cglat, sglat) - sinho;
			yp = sin_alt(2, date, hour + 1.0, glong, cglat, sglat) - sinho;
			quadout = quad(ym, yz, yp);
			nz = quadout[0];
			z1 = quadout[1];
			z2 = quadout[2];
			xe = quadout[3];
			ye = quadout[4];
	 
			if (nz == 1) {
				if (ym < 0.0) {
					utrise = hour + z1;
					rise = true;
				}
				else {
					utset = hour + z1;
					sett = true;
				}
			} 
	 
			if (nz == 2) {
				if (ye < 0.0) {
					utrise = hour + z2;
					utset = hour + z1;
				}
				else {
					utrise = hour + z1;
					utset = hour + z2;
				}
			} 
	 
			ym = yp;
			hour += 2.0;
	 
		} 
	 
		if (rise == true || sett == true ) {
			if (rise == true) {
	            resobj.put("rise", hrsmin(utrise));
	            outstring += "&nbsp;&nbsp;日出时刻:" + hrsmin(utrise)+"<BR>";
	        }
			else{
	            outstring += "&nbsp;&nbsp;日不出或日不落"+"<BR>";	
	            resobj.put("pole", "日不出或日不落");
	        }
			double zt=getzttime(mjday, tz, glong);
	        resobj.put("center", hrsmin(zt));
			outstring+= "&nbsp;&nbsp;日中时刻:" + hrsmin(zt)+"<BR>";
			if (sett == true){ 
	            outstring += "&nbsp;&nbsp;日没时刻:" + hrsmin(utset);
	            resobj.put("set", hrsmin(utset));
	        }else {
	            outstring += "&nbsp;&nbsp;日不出或日不落";
	            resobj.put("pole", "日不出或日不落");
	        }
		}
		else {
			if (above == true){
	            outstring += always_up;
	            double zt=getzttime(mjday, tz, glong);
	            resobj.put("center", hrsmin(zt));
	            resobj.put("pole", "极昼");
	            outstring+="<BR>"+"&nbsp;&nbsp;日中时刻:"+hrsmin(zt);
			} else {
	            outstring += always_down;
	            resobj.put("pole", "极夜");
	        }
		}		
		return resobj;
	}
	
	/**
	 * 根据js中的方法而来，不明白在计算什么。
	 * */
	public double mdj(int day, int month, int year, double hour){
		if(month <= 2){
			month = month + 12;
			year = year - 1;
		}
		double a = 10000.0 * year + 100.0 * month + day;
		double b = 0.0d;
		if (a <= 15821004.1) {
			b = -2 * Math.floor((year + 4716)/4) - 1179;
		}
		else {
			b = Math.floor(year/400) - Math.floor(year/100) + Math.floor(year/4);
		}
		a = 365.0 * year - 679004.0;
		double rslt = (a + b + Math.floor(30.6001 * (month + 1)) + day + hour/24.0);
		return rslt;
		
	}
	
	/**
	 * 根据js而来
	 * */
	public double sin_alt(int iobj, double mjd0, double hour, double glong, double cglat, double sglat){
		double mjday, t, ra, dec, tau, salt, rads = 0.0174532925;
		double[] objpos = null;
		mjday = mjd0 + hour/24.0;
		t = (mjday - 51544.5) / 36525.0;
		if (iobj == 1) {
			objpos = minimoon(t);
					}
		else {
			objpos = minisun(t);
		}
		ra = objpos[2];
		dec = objpos[1];
		tau = 15.0 * (lmst(mjday, glong) - ra);
		salt = sglat * Math.sin(rads*dec) + cglat * Math.cos(rads*dec) * Math.cos(rads*tau);
		return salt;
	}
	
	/**
	 * 根据js而得
	 * */
	public double[] minisun(double t){
		double p2 = 6.283185307, coseps = 0.91748, sineps = 0.39778;
		double L, M, DL, SL, X, Y, Z, RHO, ra, dec;
		double[] suneq = new double[3];
		M = p2 * frac(0.993133 + 99.997361 * t);
		DL = 6893.0 * Math.sin(M) + 72.0 * Math.sin(2 * M);
		L = p2 * frac(0.7859453 + M / p2 + (6191.2 * t + DL)/1296000);
		SL = Math.sin(L);
		X = Math.cos(L);
		Y = coseps * SL;
		Z = sineps * SL;
		RHO = Math.sqrt(1 - Z * Z);
		dec = (360.0 / p2) * Math.atan(Z / RHO);
		ra = (48.0 / p2) * Math.atan(Y / (X + RHO));
		if (ra <0 ) ra += 24;
		suneq[1] = dec;
		suneq[2] = ra;
		return suneq;
	}
	
	/**
	 * 根据js而得
	 * */
	public double[] minimoon(double t){
		return null;
	}
	
	/**
	 * 根据js方法而来
	 * */
	public double frac(double x){
		double a;
		a = x - Math.floor(x);
		if (a < 0) a += 1;
		return a;
	}
	
	/**
	 * 根据js方法而来
	 * */
	public double lmst(double mjday, double glong){
		double lst, t, d;
		d = mjday - 51544.5;
		t = d / 36525.0;
		lst = range(280.46061837 + 360.98564736629 * d + 0.000387933 *t*t - t*t*t / 38710000);
		return (lst/15.0 + glong/15);
	}
	
	/**
	 * 根据js方法而来
	 * */
	public double range(double x){
		double a, b;
		b = x / 360;
		a = 360 * (b - ipart(b));
		if (a  < 0 ) {
			a = a + 360;
		}
		return a;
	}
	
	/**
	 * 根据js方法而来
	 * */
	public double ipart(double x){
		double a;
		if (x> 0) {
		    a = Math.floor(x);
		}
		else {
			a = Math.ceil(x);
		}
		return a;
	}
	
	/**
	 * 根据js方法而来
	 * */
	public double[] quad(double ym, double yz, double yp){
		double nz, a, b, c, dis, dx, xe, ye, z1 = 0.0f, z2 = 0.0f;
		double[] quadout = new double[5];
	 
		nz = 0;
		a = 0.5 * (ym + yp) - yz;
		b = 0.5 * (yp - ym);
		c = yz;
		xe = -b / (2 * a);
		ye = (a * xe + b) * xe + c;
		dis = b * b - 4.0 * a * c;
		if (dis > 0)	{
			dx = 0.5 * Math.sqrt(dis) / Math.abs(a);
			z1 = xe - dx;
			z2 = xe + dx;
			if (Math.abs(z1) <= 1.0) nz += 1;
			if (Math.abs(z2) <= 1.0) nz += 1;
			if (z1 < -1.0) z1 = z2;
		}
		quadout[0] = nz;
		quadout[1] = z1;
		quadout[2] = z2;
		quadout[3] = xe;
		quadout[4] = ye;
		return quadout;
	}
	
	public String hrsmin(double hours) {
		double hrs;
		int h, m;
		String dum;//时间（如"05:01"）
		hrs = Math.floor(hours * 60 + 0.5)/ 60.0;
		h = ((Double)(Math.floor(hrs))).intValue();//时
		String hStr = String.valueOf(h);
		m = ((Double)(Math.floor(60 * (hrs - h) + 0.5))).intValue();//分
		String mStr = String.valueOf(m);
		if(h<10){
			hStr="0"+hStr;
		}
		if(m<10){
			mStr="0"+mStr;
		}
		dum = hStr +":"+ mStr;
		
		//js中这一段不知道怎么处理，暂注释掉。
//		if (dum < 1000){
//			dum = "0" + dum;
//		}
//		if (dum <100){
//			dum = "0" + dum;
//		}
//		if (dum < 10){
//			dum = "0" + dum;
//		}
		return dum;
	}
	public double getzttime(double mjday, double tz, double glong){
		double sglong, sglat, date, ym, yz, utrise = 0.0f, utset = 0.0f, j;
		double yp, nz, hour, z1, z2, xe, ye, iobj, rads = 0.0174532925;
		double[] quadout = new double[5];
	   
		double sinho = Math.sin(rads * -0.833);		
		date = mjday - tz/24;
		hour = 1.0;
		ym = sin_alt(2, date, hour - 1.0, glong, 1, 0) - sinho;
	 
		while(hour < 25) {
			yz = sin_alt(2, date, hour, glong, 1, 0) - sinho;
			yp = sin_alt(2, date, hour + 1.0, glong, 1, 0) - sinho;
			quadout = quad(ym, yz, yp);
			nz = quadout[0];
			z1 = quadout[1];
			z2 = quadout[2];
			xe = quadout[3];
			ye = quadout[4];
	 
			if (nz == 1) {
				if (ym < 0.0) 
					utrise = hour + z1;			
				else 
					utset = hour + z1;
				
			} 
			if (nz == 2) {
				if (ye < 0.0) {
					utrise = hour + z2;
					utset = hour + z1;
				}
				else {
					utrise = hour + z1;
					utset = hour + z2;
				}
			} 
			ym = yp;
			hour += 2.0;
		} 
		double zt=(utrise+utset)/2;
		if(zt<utrise)
			zt=(zt+12)%24;
		return zt;
	}
	
	/**
	 * 解析24小时曲线图中的基本天气数据（温度、湿度、降水、气压）
	 * @param contents 初次抓得的网页内容（用于解析参数）
	 * @param prov 省（品种名）
	 * @param city 市（中文名）
	 * @param am_pm 上午下午标识（0为上午，1为下午）
	 * @author bric_yangyulin
	 * @date 20160831
	 * */
	public String parseHour24(String contents, String prov, String city, int am_pm){
		String data24hourStr = "";//24小时数据
		/*先获取该城市对应代码*/
		String compStr = "initReal\\('(\\d+)'\\)";
		int[] index = {1};
		List<String> paramList = RegexUtil.getMatchStr(contents, compStr, index);
		if(paramList.size() == 1){
			String cityCode = paramList.get(0);
			/*解析24小时实况数据*/
			String hour24Content = httpClient.getHtmlByHttpClient(hour24Url.replace("#cityCode", cityCode), "utf-8", "");//最新24小时实况数据
			try {
				FileStrIO.saveStringToFile(hour24Content, Constants.SAVEDHTML_ROOT+Constants.FILE_SEPARATOR+DateTimeUtil.formatDate(new Date(), "yyyyMMdd")+Constants.FILE_SEPARATOR+"中国天气数据"+Constants.FILE_SEPARATOR, "24hour_" + prov + "_" + city + ".txt", "utf-8");
			} catch (IOException e) {
				logger.error(e);
			}
			JSONArray hour24Data = JSONArray.fromObject(hour24Content);
			int hour24DataLength = hour24Data.size();
//			String line = "";
			String[] category = new String[hour24DataLength];//时间列表
			Double[] temperature = new Double[hour24DataLength];//温度列表
			Double[] humidity = new Double[hour24DataLength];//湿度列表
			Double[] pressure = new Double[hour24DataLength];//气压列表
			//String[] wind = new String[hour24DataLength];
			Double[] rain1h = new Double[hour24DataLength];//降水列表
			//boolean flag = false;
			for(int i = hour24DataLength - 1; i >= 0; i--){
//				if(hour24Data.getJSONObject(i).getString("time").substring(11,13) == "00") {
//					line = hour24DataLength - i - 1;
//					flag = true;
//				}
				String hourOne = hour24Data.getJSONObject(i).getString("time");
				category[i] = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(hourOne, DateTimeUtil.YYYY_MM_DD_HH_MM), "HH");
				if(category[i].startsWith("0")){
					category[i] = category[i].substring(1);
				}
				double temperatureOne = hour24Data.getJSONObject(i).getDouble("temperature");
				temperature[i] = (temperatureOne >= 9999) ? null : temperatureOne;
				double humidityOne = hour24Data.getJSONObject(i).getDouble("humidity");
				humidity[i] = (humidityOne >= 9999) ? null : humidityOne;
				double pressureOne = hour24Data.getJSONObject(i).getDouble("pressure");
				pressure[i] = (pressureOne >= 9999) ? null : pressureOne;
				//wind.push({direction:(data[i].windDirection >= 9999) ? null : data[i].windDirection,speed:(data[i].windSpeed >= 9999) ? null : data[i].windSpeed});
				double rain1hOne = hour24Data.getJSONObject(i).getDouble("rain1h");
				rain1h[i] = (rain1hOne >= 9999) ? null : rain1hOne;
			}
			/*进一步解析并保存数据*/
			Map<String, Integer> hour2posMap = new HashMap<String, Integer>();
			hour2posMap.clear();
			//24小时实况曲线中每个时刻所在位序，用于定位相应时刻数据位序。
			for(int pos=category.length-1;pos>=0;pos--){
				if(category[pos] != null && !hour2posMap.containsKey(category[pos])){//防止上面的情况2
					hour2posMap.put(category[pos], pos);
				}
			}
			if(hour2posMap.get("0") != null){
				hour2posMap.put("24", hour2posMap.get("0"));//24时即0时(将24时数据归为了昨天的数据，所以这样处理)
			}
			/**/
			int numOfTime = hour2posMap.size() - 1;//24小时实况曲线中的时刻数(24时与0时算一个时刻)
			if(numOfTime<1 || temperature.length != numOfTime ||
				rain1h.length != numOfTime ||humidity.length != numOfTime ||pressure.length != numOfTime){
				logger.info(prov+"-"+city+"天气数据不存在");
				RecordCrawlResult.recordFailData(className, prov, city, "基本天气数据不完整");
				return "";
			}
			if(am_pm == 0){//如果是上午（定时为上午5点）则保存前一天下午的数据，
				for(int i=13;i<=24;i++){
					int pos = hour2posMap.get(i+"")!=null ? hour2posMap.get(i+"") : -1;
					if(pos < temperature.length && pos != -1){
						data24hourStr += i+","+temperature[pos]+","+rain1h[pos]+","+humidity[pos]+","+pressure[pos]+"\n";
					} else {
						logger.error(String.format("%s%s的气象数据发生数组越界或无对应时刻数据, i = %d, pos = %d", prov, city, i, pos));
					}
				}
			}else{//如果是下午（定时为下午5点）则保存当天上午的数据
				for(int i=1;i<=12;i++){
					int pos = hour2posMap.get(i+"")!=null ? hour2posMap.get(i+"") : -1;
					if(pos < temperature.length && pos != -1){
						data24hourStr += i+","+temperature[pos]+","+rain1h[pos]+","+humidity[pos]+","+pressure[pos]+"\n";
					} else {
						logger.error(String.format("%s%s的气象数据发生数组越界或无对应时刻数据, i = %d, pos = %d", prov, city, i, pos));
					}	
				}
			}
		} else {
			RecordCrawlResult.recordFailData(className, prov, city, "获取城市编码失败。");
		}
		return data24hourStr;
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_WEATHER_WINDS)
	public void startWinds(){
		String switchFlag = new CrawlerManager().selectCrawler("中央气象台数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到中央气象台数据的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				this.fetchWindsData(new Date());
			}else{
				logger.info("抓取中央气象台数据的定时器已关闭");
			}
		}
	}
	
	public void fetchWindsData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		logger.info("========start fetch weather winds data=========");
		String pageUrl = null;
		String[] filters = {"div", "class", "row winds"};
		String compStr = "\\d+\\.\\d+";
		Set<String> provSet = MapInit.weather_prov2city_map.keySet();
		logger.info(provSet.size()+"个省份");
		for(String provTmp:provSet){
			String[] provAndCode = provTmp.split("-");
			String prov = provAndCode[0];
			String provCode = provAndCode[1];
			Map<String, String> cityMap = MapInit.weather_prov2city_map.get(provTmp);
			for(String cityTmp:cityMap.keySet()){
				try{
					try {
						Thread.sleep((int)(Math.random() * 1500));
					} catch (InterruptedException e1) {
						logger.error(e1.getMessage());
					}
					pageUrl = url.replaceAll("%provCode%", provCode).replaceAll("%city%", cityMap.get(cityTmp));
					logger.info("start to fetch "+prov+cityTmp+"@"+pageUrl);
					String contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", prov+cityTmp, filters, null, 0); 
					httpClient.getHtmlByHttpClient(pageUrl,"utf-8","");
					if(contents!=null && !contents.equals("")){
						List<String> regexList = null;
						regexList = RegexUtil.getMatchStr(contents, compStr);
						if(regexList != null && !regexList.isEmpty()){
							int size = regexList.size();
							float[] speeds = new float[size+1];//最后一位放所有风速的和
							for(int i = 0; i < size; i++){
								speeds[i] = Float.parseFloat(regexList.get(i));
							}
							Arrays.sort(speeds, 0, size);
							speeds[size] = 0.0f;
							for(int i = 0; i < size; i++){
								System.out.println(speeds[i]);
								speeds[size] += speeds[i];
							}
							String min = String.valueOf(speeds[0]);//最小风速
							String max = String.valueOf(speeds[size-1]);//最大风速
							String ave = String.valueOf(speeds[size]/size);//平均风速
							String dirString = Constants.WEATHER_ROOT + Constants.FILE_SEPARATOR + prov + Constants.FILE_SEPARATOR + cityTmp + Constants.FILE_SEPARATOR;
							String windsDataToSave = String.format("#最小风力：%s\n#最大风力：%s\n#平均风力：%s", min, max, ave);
							try {
								FileStrIO.appendStringToFile(windsDataToSave, dirString, timeInt + ".txt", Constants.ENCODE_GB2312);
							} catch (IOException e) {
								logger.error("保存风力数据时发生异常。" + e.getMessage());
								RecordCrawlResult.recordFailData(className, prov, cityTmp, "\"保存中国风力数据至txt文件中时出现异常。" + e.getMessage() + "\"");
							}
						}
					}
				} catch(Exception e) {
					logger.error(prov + cityTmp + "风速抓取出现异常。", e);
					RecordCrawlResult.recordFailData(className, prov, cityTmp, "中国风力数据抓取出现异常");
				}
			}
		}
	}
	
	public static void main(String[] args){
		ChinaWeatherData weather = new ChinaWeatherData();
//		weather.sunriseset(115.88, 37.8);
//		String[] repairDates = new String[]{"20160803", "20160804", "20160805", "20160806", "20160807", "20160808"};
//		for(String repairDate: repairDates){
//			weather.logger.info("——————————————————开始补中国天气数据" + repairDate + "————————————————————————");
//			weather.save2db(new File(Constants.WEATHER_ROOT), repairDate);//利用指定日期的数据文件补指定日期的数据
//		}
//		weather.save2db(new File(Constants.WEATHER_ROOT), "20160810");//利用指定日期的数据文件补指定日期的数据
//		weather.startWinds();
		weather.start();
	}
}
