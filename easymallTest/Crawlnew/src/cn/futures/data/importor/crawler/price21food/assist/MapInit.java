package cn.futures.data.importor.crawler.price21food.assist;


import cn.futures.data.jdbc.JdbcRunner;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MapInit {
	public static Map<String,String> analyseDataMap;
	public static Map<String,String> productToKindMap;
	static {
		synchronized(MapInit.class){
			loadAnalyseDataMap();
		}		
	}
	
	/*
	 * 吴宪国 20140808
	 * 读取果蔬批发市场的品种信息，由于数据库客户端中并未以品种形式对待，而是仅仅以一个属性表对待，因此不能通过品种id关联，只能通过品种名去关联
	 * 返回这个品种对应的数据库表名
	 */
	public static void loadAnalyseDataMap(){
		analyseDataMap = new HashMap<String,String>();
		productToKindMap = new HashMap<String,String>();
		String sql = "SELECT varId,cnName,dbName FROM CFG_TABLE_META where varId in(306,307,313) ";
		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			ResultSet rs = db.query(sql);
			while (rs.next()){
				String cnname = rs.getString("cnName");
				String dbname = rs.getString("dbName");		
				int varid = rs.getInt("varId");
				//System.out.println(cnname+","+dbname);
				analyseDataMap.put(cnname, dbname);
				productToKindMap.put(cnname, String.valueOf(varid));				
			}			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error happens when load Variety");
		} finally {
			db.release();
		}
		
	}
	
	//蔬菜类
	public static Map<Integer, String> VegetablesMap = new HashMap<Integer, String>(){
		{
			put(8146, "蘑菇");
			put(7668, "黑木耳");
			put(7138, "白萝卜");
			put(7991, "韭菜");
			put(7184, "杏鲍菇");
			put(7799, "黄瓜");
			put(8429, "西红柿");
			put(8081, "萝卜");
			put(8120, "毛豆");
			put(8016, "辣椒");
			put(8019, "辣椒干");
			put(8151, "木耳菜");
			put(7764, "花椒");
			put(8449, "香菜");
			put(7895, "生姜");
			put(7298, "大葱");
			put(8006, "苦瓜");
			put(8434, "西兰花");
			put(8451, "香菇");
			put(7105, "小白菜");
			put(8198, "平菇");
			put(7947, "金针菇");
			put(7382, "大蒜");
			put(8397, "土豆");
			put(7432, "冬瓜");
			put(7304, "洋葱");
			put(7741, "胡萝卜");
			put(8157, "南瓜");
			put(7103, "大白菜");
			put(7890, "绿尖椒");
			put(8347, "莴笋");
			put(8053, "莲藕");
			put(8291, "生菜");
			put(7742, "西葫芦");
			put(7104, "洋白菜");
			put(8520, "油菜");
			put(8334, "蒜薹");
			put(7208, "菠菜");
			put(7447, "豆角");
			put(7222, "菜花");
			put(7231, "菜苔");
			put(7299, "葱头");
			put(7875, "茄子");
			put(8224, "青椒");
			put(8217,"芹菜");			

		}
	};
	//水果类
	public static Map<Integer, String> FruitMap = new HashMap<Integer, String>(){
		{
			put(8453, "香蕉");
			put(8206, "葡萄");
			put(8199, "苹果");
			put(8426, "西瓜");
			put(8519, "柚子");
			put(10170, "柑橘");
			put(7545, "甘蔗");
			put(7252, "草莓");
			put(8362, "桃子");
			put(7712, "红提子");
			put(8039, "香梨");
			put(7277, "脐橙");
			put(7944, "金丝枣");
			put(8167, "柠檬");
			put(8097, "绿马奶葡萄干");
			put(8112, "芒果");
			put(7700, "红马奶葡萄干");
			put(7734, "猕猴桃");
			put(8033, "鸭梨");
			put(8136, "蜜桔");
			put(7996, "巨峰葡萄");
			put(7211, "菠萝");
			put(7599, "哈密瓜");
			put(8154, "木瓜");
			put(8244, "青枣");
		}
	};
	//家禽类
	public static Map<Integer, String> FowlMap = new HashMap<Integer, String>(){
		{
			put(8490, "鸭蛋");
			put(9877, "活鸡");
			put(7850, "鸡蛋");
			put(9790, "肉鸡");
			put(15136, "鸭苗");
			put(7146, "白条鸡");
			put(19706, "鸭");
			put(9884, "乌鸡");
		}
	};
	//家畜类
	public static Map<Integer, String> CattleMap = new HashMap<Integer, String>(){
		{
			put(9664, "生猪");
			put(10193, "肉驴");
			put(10994, "西门塔尔牛");
			put(13890, "夏洛莱牛");
			put(10993, "鲁西黄牛");
			put(19130, "杜泊绵羊");
			put(8501, "羊肉");
			put(8173, "牛肉");
			put(9894, "仔猪");
			put(10044, "羊");
			put(9883, "肉牛");
			put(9878, "肉羊");
			put(10991, "小尾寒羊");
			put(9882, "育肥羊");
			put(10965, "波尔山羊");
			put(11072, "母猪");
			put(9891, "毛猪");
		}
	};
	//粮油类
	public static Map<Integer, String> GrainAndOilMap = new HashMap<Integer, String>(){
		{
			put(7778, "花生油");
			put(8460, "香油");
			put(8375, "特一粉");
			put(8444, "籼米");
			put(7773, "花生");
			put(8584, "棕榈油");
			put(8180, "糯米");
			put(8012, "葵花籽");
			put(8534, "玉米");
			put(8468, "小麦");
			put(8096, "绿豆");
			put(7361, "大米");
			put(7645, "核桃");
			put(7325, "大豆");
			put(7171, "板栗");
			put(7459, "豆粕");
			put(8139, "面粉");
			put(7795, "黄豆");
			put(8102, "马铃薯");
			put(7357, "东北大米");
			put(7774, "花生米");
		}
	};
	//绿化类
	public static Map<Integer, String> GreenMap = new HashMap<Integer, String>(){
		{
			put(12318, "107杨");
			put(12188, "白皮松");
			put(11197, "香樟");
			put(7163, "百合");
			put(10523, "麦冬");
			put(11193, "桂花");
			put(11310, "速生柳");
			put(11444, "银杏树");
			put(11299, "红豆杉");
		}
	};
	//水产类
	public static Map<Integer, String> AquacMap = new HashMap<Integer, String>(){
		{
			put(7393, "小带鱼");
			put(7588, "活鳜鱼");
			put(7142, "白鳝鱼");
			put(7836, "基围虾");
			put(7379, "大平鱼");
			put(8472, "小平鱼");
			put(8351, "梭子蟹");
			put(7733, "虹鳟鱼");
			put(8280, "扇贝");
			put(8176, "牛蛙");
			put(7188, "鲍鱼");
			put(7601, "海参");
			put(7814, "黄鳝");
			put(7475, "鳄鱼龟");
			put(8160, "泥鳅");
			put(7471, "对虾");
			put(8239, "青虾");
			put(8487, "鲟鱼");
			put(7577, "桂花鱼");
			
		}
	};
	//药材类
	public static Map<Integer, String> MedicineMap = new HashMap<Integer, String>(){
		{
			put(9775, "金银花");
			put(10627, "天麻");
			put(10594, "冬虫夏草");
			put(10643, "肉苁蓉");
			put(9260, "草果");
			put(10651, "黄连");
			put(10331, "川芎");
			put(8275, "山药");
			put(10605, "梅花鹿茸");
			put(10754, "何首乌");
			put(10617, "三七");
			put(10562, "丹参");
			put(10521, "白术");
			put(10270, "莲子");
			put(11275, "皂角");
			put(9266, "陈皮");
			put(10503, "黄芩");
		}
	};
	//特殊养殖类
	public static Map<Integer, String> SpecilBreedMap = new HashMap<Integer, String>(){
		{
			put(10403, "獭兔");
			put(10416, "蝎子");
			put(17013, "梅花鹿");
			put(10407, "黄粉虫");
			put(13921, "肉狗");
		}
	};
	//经济作物类
	public static Map<Integer, String> EcoCropsMap = new HashMap<Integer, String>(){
		{
			put(9774,"棉花");
		}
	};
	
	//汇总
	public static Map<String, Map<Integer, String>> nameMap = new HashMap<String, Map<Integer, String>>(){
		{
			put("蔬菜", VegetablesMap);//6.3 done
			put("水产", AquacMap);//6.3 done
			put("粮油", GrainAndOilMap);//6.3 done
			put("绿化", GreenMap);//6.3 done
			put("家禽", FowlMap);//6.3 done
			put("家畜", CattleMap);//6.3 done
			put("特种养殖", SpecilBreedMap);//6.3 done
			put("经济作物", EcoCropsMap);//6.3 done
			put("药材", MedicineMap);//6.3 done
			put("水果", FruitMap);//6.3 done
		}
	};
	public static Map<String, Integer> nameKindIdMap = new HashMap<String, Integer>(){
		{
			put("蔬菜", 1001);
			put("水产", 1002);
			put("粮油", 1003);
			put("绿化", 1004);
			put("家禽", 1005);
			put("家畜", 1006);
			put("特种养殖", 1007);
			put("经济作物", 1008);
			put("药材", 1009);
			put("水果", 1010);
		}
	};
	//大连持仓
	public static Map<String, String> DLCCmapMap = new HashMap<String,String>(){
		{
			put("豆一", "a");//done
			put("豆二", "b");//done
			put("胶合板", "bb");//done
			put("玉米", "c");//done
			//put("玉米淀粉","cs");
			put("纤维板", "fb");//done
			put("铁矿石", "i");//done
			put("焦炭", "j");//done
			put("焦煤", "jm");//done
			put("聚乙烯", "l");//done
			put("鸡蛋","jd");
			put("豆粕", "m");//4080 done
			put("棕榈油", "p");//2430 done
			put("聚丙烯", "pp");//130 done
			put("聚氯乙烯", "v");//1855 done
			put("豆油", "y");//2800 done
			put("大豆", "s");//done
		}
	};
	public static Map<String, String> DLCCTableMap = new HashMap<String,String>(){
		{
			put("豆一", "CX_DLCC_Douyi");
			put("豆二", "CX_DLCC_Douer");
			put("胶合板", "CX_DLCC_Jiaoheban");
			put("玉米", "CX_DLCC_Yvmi");
			//put("玉米淀粉","CX_DLCC_Ymdf");
			put("纤维板", "CX_DLCC_Xianweiban");
			put("铁矿石", "CX_DLCC_Tiekuangshi");
			put("焦炭", "CX_DLCC_Jiaotan");
			put("焦煤", "CX_DLCC_Jiaomei");
			put("聚乙烯", "CX_DLCC_Jvyixi");
			put("鸡蛋","CX_DLCC_Jidan");
			put("豆粕", "CX_DLCC_Doupo");
			put("棕榈油", "CX_DLCC_Zonglvyou");
			put("聚丙烯", "CX_DLCC_Jvbingxi");
			put("聚氯乙烯", "CX_DLCC_Jvlvyixi");
			put("豆油", "CX_DLCC_Douyou");
			put("大豆", "CX_DLCC_Dadou");
		}
	};
	public static Map<String, Integer> DLCCVarIdMap = new HashMap<String,Integer>(){
		{
			put("豆一", 1011);
			put("豆二", 1012);
			put("胶合板", 1013);
			put("玉米", 1014);
			put("纤维板", 1015);
			put("铁矿石", 1016);
			put("焦炭", 1017);
			put("鸡蛋", 1018);
			put("焦煤", 1019);
			put("聚乙烯", 1020);			
			put("豆粕", 1021);
			put("棕榈油", 1022);
			put("聚丙烯", 1023);
			put("聚氯乙烯", 1024);
			put("豆油", 1025);
			put("大豆", 1026);
		}
	};
	
	public static Map<String, String> CZCEmapMap = new HashMap<String, String>(){
		{
			put("强麦","WH");
			put("棉花","CF");
			put("玻璃","FG");
			put("粳稻","JR");
			put("菜粕","RM");
			put("菜油","OI");
			put("晚籼","LR");
			put("菜籽","RS");
			put("动力煤","TC");
			put("普麦","PM");
			put("白糖","SR");
			put("甲醇","ME");
			put("早籼","RI");
		}
	};
	
	public static Map<String, Integer> CZCEvarIDMap = new HashMap<String, Integer>(){
		{
			put("强麦",31001);
			put("棉花",31002);
			put("玻璃",31003);
			put("粳稻",31004);
			put("菜粕",31005);
			put("菜油",31006);
			put("晚籼",31007);
			put("菜籽",31009);
			put("动力煤",31010);
			put("普麦",31011);
			put("白糖",31012);
			put("甲醇",31013);
			put("早籼",31014);
		}
	};
	
	public static Map<String, String> CZCEtableMap = new HashMap<String, String>(){
		{
			put("强麦","CX_CZCE_Qiangmai");
			put("棉花","CX_CZCE_Mianhua");
			put("玻璃","CX_CZCE_Boli");
			put("粳稻","CX_CZCE_Jingdao");
			put("菜粕","CX_CZCE_Caipo");
			put("菜油","CX_CZCE_Caiyou");
			put("晚籼","CX_CZCE_Wanxian");
			put("菜籽","CX_CZCE_Caizi");
			put("动力煤","CX_CZCE_Donglimei");
			put("普麦","CX_CZCE_Pumai");
			put("白糖","CX_CZCE_Baitang");
			put("甲醇","CX_CZCE_Jiachun");
			put("早籼","CX_CZCE_Zaoxian");
		}
	};

	public static Map<String, String> CZCEmapReverse = new HashMap<String, String>(){
		{
			put("WH","强麦");
			put("CF","棉花");
			put("FG","玻璃");
			put("JR","粳稻");
			put("RM","菜粕");
			put("OI","菜油");
			put("LR","晚籼");
			put("RS","菜籽");
			put("TC","动力煤");
			put("PM","普麦");
			put("SR","白糖");
			put("ME","甲醇");
			put("MA","甲醇");
			put("RI","早籼");
		}
	};
		
	public static Map<String, Integer> nameKindIdMap21food = new HashMap<String, Integer>(){
		{
			put("蔬菜类", 1027);
//			put("食用菌", 1028);
				put("水果类", 1029);
//			put("肉类", 1030);
//			put("禽蛋", 1031);
			put("水产品", 1032);
//			put("稻谷", 1033);
//			put("玉米", 1034);
//			put("小麦", 1035);
			put("豆类", 1036);
//			put("油料", 1037);
//			put("休闲食品", 1038);
//			put("保健食品", 1039);
//			put("淀粉类", 1040);
//			put("乳制品", 1041);
//			put("茶叶类", 1042);
			put("调味品", 1043);
//			put("食品添加剂", 1044);
//			put("糖类", 1045);
//			put("食品其它", 1046);
		}
	};
	
	public static Map<String, String> marketPrice21foodTableMap = new HashMap<String, String>(){
		{
			put("蔬菜类", "CX_21FoodPrice_Vagetables");
//			put("食用菌", "CX_21FoodPrice_Mushroom");
			put("水果类", "CX_21FoodPrice_Fruits");
//			put("肉类", "CX_21FoodPrice_Meat");
//			put("禽蛋", "CX_21FoodPrice_Eggs");
			put("水产品", "CX_21FoodPrice_Aquatics");
//			put("稻谷", "CX_21FoodPrice_Rice");
//			put("玉米", "CX_21FoodPrice_Corn");
//			put("小麦", "CX_21FoodPrice_Wheat");
			put("豆类", "CX_21FoodPrice_Beans");
//			put("油料", "CX_21FoodPrice_Oil");
//			put("休闲食品", "CX_21FoodPrice_Snacks");
//			put("保健食品", "CX_21FoodPrice_HealthFood");
//			put("淀粉类", "CX_21FoodPrice_Starch");
//			put("乳制品", "CX_21FoodPrice_Dairy");
//			put("茶叶类", "CX_21FoodPrice_Tea");
			put("调味品", "CX_21FoodPrice_Seasoning");
//			put("食品添加剂", "CX_21FoodPrice_Addtives");
//			put("糖类", "CX_21FoodPrice_Suger");
//			put("食品其它", "CX_21FoodPrice_OtherFood");
		}
	};
	public static Map<String, Integer> otherFood_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> suger_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> addtives_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> seasoning_21food_map = new HashMap<String, Integer>(){
		{
			put("尖椒", 23000);
		}
	};
	public static Map<String, Integer> tea_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> dairy_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> starch_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> healthFood_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> snacks_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> oil_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> beans_21food_map = new HashMap<String, Integer>(){
		{
			put("豇豆", 22000);
			put("四季豆", 22001);
		}
	};
	public static Map<String, Integer> wheat_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> corn_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> rice_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> Aquatics_21food_map = new HashMap<String, Integer>(){
		{
			put("龙虾", 24000);
			put("鲶鱼", 24001);
			put("黄鳝", 24002);
			put("小黄鱼", 24003);
			put("草鱼", 24004);
			put("鲤鱼", 24005);
			put("鲫鱼", 24006);
			put("鱿鱼", 24007);
			put("武昌鱼", 24008);
			put("牛蛙", 24009);
			put("鲈鱼", 24010);
			put("带鱼", 24011);
			put("白鲢", 24012);
			put("桂鱼", 24013);
		}
	};
	public static Map<String, Integer> eggs_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> meat_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> fruits_21food_map = new HashMap<String, Integer>(){
		{
			put("油桃", 21000);
			put("苹果", 21001);
			put("榴莲", 21002);
			put("猕猴桃", 21003);
			put("山楂", 21004);
			put("香蕉", 21005);
			put("芒果", 21006);
			put("荔枝", 21007);
			put("菠萝", 21008);
			put("西瓜", 21009);
			put("木瓜", 21010);
			put("火龙果", 21011);
		}
	};
	public static Map<String, Integer> mushroom_21food_map = new HashMap<String, Integer>();
	public static Map<String, Integer> vagetables_21food_map = new HashMap<String, Integer>(){
		{
			put("萝卜", 20000);
			put("土豆", 20001);
			put("番茄", 20002);
			put("芹菜", 20003);
			put("圆椒", 20004);
			put("茄子", 20005);
			put("黄瓜", 20006);
			put("冬瓜", 20007);
			put("苦瓜", 20008);
			put("圣女果", 20009);
		}
	};	
	public static Map<String, Map<String, Integer>> nameReflectMap21food = new HashMap<String, Map<String, Integer>>(){
		{
			put("蔬菜类", vagetables_21food_map);
//			put("食用菌", mushroom_21food_map);
			put("水果类", fruits_21food_map);
//			put("肉类", meat_21food_map);
//			put("禽蛋", eggs_21food_map);
			put("水产品", Aquatics_21food_map);
//			put("稻谷", rice_21food_map);
//			put("玉米", corn_21food_map);
//			put("小麦", wheat_21food_map);
			put("豆类", beans_21food_map);
//			put("油料", oil_21food_map);
//			put("休闲食品", snacks_21food_map);
//			put("保健食品", healthFood_21food_map);
//			put("淀粉类", starch_21food_map);
//			put("乳制品", dairy_21food_map);
//			put("茶叶类", tea_21food_map);
			put("调味品", seasoning_21food_map);
//			put("食品添加剂", addtives_21food_map);
//			put("糖类", suger_21food_map);
//			put("食品其它", otherFood_21food_map);
		}
	};
	

	public static Map<String, Integer> mofcomMarketPriceMap = new HashMap<String, Integer>(){
		{
			put("畜产品", 13079);
			put("水产品", 13080);
			put("粮油", 13073);
			put("果品", 13076);
			put("蔬菜", 13075);
		}
	};
	
	public static Map<String, String> marketPriceMofcomTableMap = new HashMap<String, String>(){
		{
			put("畜产品", "CX_MofcomPrice_meat");
			put("水产品", "CX_MofcomPrice_Aquatics");
			put("粮油", "CX_MofcomPrice_riceAoil");
			put("果品", "CX_MofcomPrice_fruits");
			put("蔬菜", "CX_MofcomPrice_Vagetables");
		}
	};
		
	public static Map<String, Integer> vagetables_mofcom_map = new HashMap<String, Integer>(){
		{
			put("土豆", 20413);
			put("黄瓜", 20410);
			put("大白菜", 20409);
			put("芹菜", 20411);
			put("青椒", 13811);
			put("韭菜", 13515);
			put("茄子", 13509);
			put("大葱", 15637);
			put("大蒜", 13520);
			put("生姜", 15641);
			put("白萝卜", 20408);
			put("胡萝卜", 13512);
			put("香菜", 13504);
			put("冬瓜", 15643);
			put("豆角", 13510);
			put("菠菜", 13508);
			put("油菜", 20406);
			put("洋白菜", 13511);
			put("西葫芦", 13366);
			put("生菜", 13495);
			put("苦瓜", 13499);
			put("南瓜", 13519);
			put("莴笋", 15642);
			put("香菇", 13365);
			put("小白菜", 13370);
			put("平菇", 13217);
			put("丝瓜", 13498);
			put("西兰花", 13497);
			put("山药", 13214);
			put("茼蒿", 13255);
			put("豇豆", 13368);
		}
	};
	public static Map<String, Integer> fruits_mofcom_map = new HashMap<String, Integer>(){
		{
			put("香蕉", 13103);
			put("富士苹果", 13097);
			put("西瓜", 13106);
			put("哈密瓜", 13107);
			put("菠萝", 13105);
			put("巨峰葡萄", 13139);
			put("蜜桔", 13098);
			put("鸭梨", 13102);
			put("猕猴桃", 13201);
			put("红提子", 13244);
			put("芒果", 13228);
			put("草莓", 13167);
			put("甘蔗", 13224);
			put("雪梨", 13123);
			put("红枣", 13185);
			put("香瓜", 13164);
			put("山竹", 13260);
			put("核桃", 13176);
			put("油桃", 13215);
			put("柠檬", 13246);
			put("石榴", 13230);
			put("荔枝", 13194);
			put("樱桃", 13169);
			put("椰子", 13188);
		}
	};
	public static Map<String, Integer> meat_mofcom_map = new HashMap<String, Integer>(){
		{
			put("鸡蛋", 13245);
			put("牛肉", 13235);
			put("猪肉(白条猪)", 13233);
			put("羊肉", 13237);
			put("白条鸡", 13240);
			put("鸭蛋", 13248);
			put("活鸡", 13243);
			put("乌鸡", 13257);
			put("三黄鸡", 13328);
			put("活鸭", 13261);
			put("猪大肠", 13317);
			put("仔猪", 13253);
			put("毛猪", 13250);
			put("黄牛", 13304);
			put("驴肉", 13239);
			put("肉羊", 13351);
			put("肉牛", 13349);
			put("鸡苗(只)", 13303);
			put("红皮鸡蛋", 13263);
			put("白皮鸡蛋", 13264);
		}
	};
	public static Map<String, Integer> Aquatics_mofcom_map = new HashMap<String, Integer>(){
		{
			put("活草鱼", 8754711);
			put("白鲢活鱼", 8754710);
			put("活鲫鱼", 8754722);
			put("活鲤鱼", 8754705);
			put("黄鳝", 13137);
			put("武昌鱼", 13144);
			put("带鱼", 13118);
			put("黑鱼", 13134);
			put("鲈鱼", 13151);
			put("泥鳅", 13135);
			put("小黄鱼", 15011220);
			put("章鱼", 15052322);
			put("牛蛙", 13168);
			put("基围虾", 13189);
			put("草鱼", 13093);
			put("鲫鱼", 13117);
			put("鲢鱼", 13089);
			put("鲤鱼", 13091);
			put("对虾", 13121);
			put("蛤蜊", 13200);
		
		}
	};
	public static Map<String, Integer> riceAoil_mofcom_map = new HashMap<String, Integer>(){
		{
			put("面粉", 13095);
			put("绿豆", 13096);
			put("色拉油", 13119);
			put("大豆", 13087);
			put("标准粉", 13104);
			put("高粱", 15052308);
			put("玉米", 13086);
			put("芸豆", 13264);
			put("花生油", 13094);
			put("大米", 13084);
			put("香油", 13113);
			put("特一粉", 13108);
			put("花生", 13122);
			put("棕榈油", 13115);
			put("豆油", 13090);
			put("菜油", 13088);
			put("粳米", 13085);
			put("糯米", 8754700);
			put("籼米", 13083);
			put("早籼稻", 9060841);
			put("晚籼稻", 9060839);
		}
	};
	public static Map<String, Map<String, Integer>> mofcomKindReflectMap = new HashMap<String, Map<String, Integer>>(){
		{			
			put("畜产品", meat_mofcom_map);
			put("水产品", Aquatics_mofcom_map);
			put("粮油", riceAoil_mofcom_map);
			put("果品", fruits_mofcom_map);
			put("蔬菜", vagetables_mofcom_map);
		}
	};

	public static Map<String, Integer> beijing_mofcom_map = new HashMap<String, Integer>(){
		{
			put("北京丰台区新发地农产品批发市场", 20531);
			put("北京昌平水屯农副产品批发市场", 2576578);
			put("北京朝阳区大洋路农副产品批发市场", 20540);
			put("北京城北回龙观商品交易市场", 21451);
			put("北京市通州八里桥农产品中心批发市场", 20536);
			put("北京市华垦岳各庄批发市场", 20532);
			put("北京市锦绣大地农副产品批发市场", 20543);
			put("北京市锦绣大地玉泉路粮油批发市场", 20546);
			put("北京市日上综合商品批发市场", 20548);
			put("北京顺义区顺鑫石门农产品批发市场", 20551);
			put("北京市华垦岳各庄批发市场", 20532);
		}
	};
	public static Map<String, Integer> anhui_mofcom_map = new HashMap<String, Integer>(){
		{

			
		}
	};
	public static Map<String, Integer> fujian_mofcom_map = new HashMap<String, Integer>(){
		{

		}
	};
	public static Map<String, Map<String, Integer>> mofcomMarketReflectMap = new HashMap<String, Map<String, Integer>>(){
		{
			put("北京", beijing_mofcom_map);
//			put("安徽", anhui_mofcom_map);
//			put("福建", fujian_mofcom_map);
		}
	};
}
