package cn.futures.data.DAO;

import cn.futures.data.jdbc.DbConfig;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by 高健 on 2017/6/29.
 * 向dbm3接口传输数据的dao，所有定时爬虫都通过该接口传输数据
 */
public class Dbm3InterfaceDao {
    private final Logger logger = Logger.getLogger(Dbm3InterfaceDao.class);
    //dbm3通过dbName存储数据的接口
    private static  String byDbNameAddr;
    //dbm3通过TableDir存储数据的接口
    private static  String byTableDirAddr;

    static {
        try {
            Properties prop = new Properties();
            InputStream is = DbConfig.class.getClassLoader()
                    .getResourceAsStream("dbm3_interface.properties");
            prop.load(is);
            byDbNameAddr=prop.getProperty("byDbNameAddr");
            byTableDirAddr=prop.getProperty("byTableDirAddr");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Gson gson = new Gson();

    /**
     * 通过数据库表名存储数据
     * @param data
     */
    public void saveByDbName(Map data){
        data.put("operator","yyl");
        data.put("bric","jingmi");
        data.put("isCovered",data.getOrDefault("isCovered","false"));
        data.put("condition",data.getOrDefault("condition","TimeInt,VarId"));
        String jsonData=gson.toJson(data);
        logger.info("要传输的数据为："+jsonData);
        try {
//            String index = WebServiceTool.postRequest(byDbNameAddr, "data="+jsonData);
            String index = Jsoup.connect(byDbNameAddr+"?data="+jsonData).get().text();
            logger.info("数据传输结果: "+index);
        }catch (Exception e){
            logger.error("数据传输报错："+e.getMessage());
        }
    }

    /**
     * 通过客户端四级目录存储数据
     * @param data
     */
    public void saveByTableDir(Map data){
        data.put("operator","yyl");
        data.put("bric","jingmi");
        data.put("isCovered",data.getOrDefault("isCovered","false"));
        data.put("condition",data.getOrDefault("condition","TimeInt,VarId"));
        String jsonData=gson.toJson(data);
        logger.info("要传输的数据为："+jsonData);
        try {
//            String index = WebServiceTool.postRequest(byTableDirAddr, jsonData);
            String index = Jsoup.connect(byTableDirAddr+"?data="+jsonData).get().text();
            logger.info("数据传输结果: "+index);
        }catch (Exception e){
            logger.error("数据传输报错："+e.getMessage());
        }
    }
}
