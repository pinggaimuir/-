import domain.User3;
import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2016/9/27.
 */
public class TestJSON extends TestCase {
    //转化成json对象
    public void testJSONArray(){
        Map<String,String> map=new HashMap();
        map.put("wode1","nide1");
        map.put("wode2","nide2");
        map.put("wode3","nide3");
        JsonConfig config=new JsonConfig();
        //不想要转化的
        config.setExcludes(new String[]{"wode3"});
        map.put("wode4","nide4");
        JSONArray jsonArray= JSONArray.fromObject(map,config);
        System.out.println(jsonArray);
    }
    //json转化成对象
    public void testJsonToBean(){
        String json="{'id':'gao','age':18,'name':'jian'}";
        JSONObject jsonUser=JSONObject.fromObject(json);
        User3 user3= (User3) JSONObject.toBean(jsonUser,User3.class);
        System.out.println(user3.toString());

    }
}
