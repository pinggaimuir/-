package cn.futures.data.DAO;

import cn.futures.data.importor.crawler.heatIndex.BaiduIndex;
import cn.futures.data.util.WebServiceTool;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by 高健 on 2017/6/29.
 */
public class Dbm3InterfaceDao {
    private final Logger logger = Logger.getLogger(BaiduIndex.class);
    //dbm3通过dbName存储数据的接口
    private final String byDbNameAddr="http://dbm3.ncpqh.com/warehouse/byDbName";
    //dbm3通过TableDir存储数据的接口
    private final String byTableDirAddr="http://dbm3.ncpqh.com/warehouse/byTableDir";

    private final  Gson gson = new Gson();

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
            String index = WebServiceTool.postRequest(byDbNameAddr, jsonData);
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
            String index = WebServiceTool.postRequest(byTableDirAddr, jsonData);
            logger.info("数据传输结果: "+index);
        }catch (Exception e){
            logger.error("数据传输报错："+e.getMessage());
        }
    }
}
