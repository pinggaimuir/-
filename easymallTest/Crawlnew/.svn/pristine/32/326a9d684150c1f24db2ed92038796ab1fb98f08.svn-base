package cn.futures.test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;

import cn.futures.data.jdbc.JdbcRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
 

public class MongoTest {

	public static void main(String[] args) throws Exception  {
		// TODO Auto-generated method stub
		
		testOne();

	}
	 /**  
     * 保存实体对象  
     * @throws Exception  
     */  
     public static void saveEntity() throws Exception{  
          //第一：实例化mongo对象，连接mongodb服务器  包含所有的数据库  
           
          //默认构造方法，默认是连接本机，端口号，默认是27017  
          //相当于Mongo mongo =new Mongo("localhost",27017)  
          Mongo mongo =new Mongo();  
           
          //第二：连接具体的数据库  
          //其中参数是具体数据库的名称，若服务器中不存在，会自动创建  
          DB db=mongo.getDB("myMongo");  
           
          //第三：操作具体的表  
         //在mongodb中没有表的概念，而是指集合  
          //其中参数是数据库中表，若不存在，会自动创建  
          DBCollection collection=db.getCollection("user");  
           
          //添加操作  
          //在mongodb中没有行的概念，而是指文档  
          BasicDBObject document=new BasicDBObject();  
           
          document.put("id", 1);  
          document.put("name", "小明");  
//          //然后保存到集合中  
//     //     collection.insert(document);  
           
           
          //当然我也可以保存这样的json串  
/*          {  
               "id":1,  
               "name","小明",  
               "address":  
               {  
               "city":"beijing",  
               "code":"065000"  
               }  
          }*/  
          //实现上述json串思路如下：  
          //第一种：类似xml时，不断添加  
          BasicDBObject addressDocument=new BasicDBObject();  
          addressDocument.put("city", "beijing");  
          addressDocument.put("code", "065000");  
          document.put("address", addressDocument);  
          //然后保存数据库中  
          collection.insert(document);  
           
          //第二种：直接把json存到数据库中  
/*          String jsonTest="{'id':1,'name':'小明',"+  
                   "'address':{'city':'beijing','code':'065000'}"+  
                    "}";  
         DBObject dbobjct=(DBObject)JSON.parse(jsonTest);  
         collection.insert(dbobjct);*/      
     }  
      
     /**  
     * 遍历所有的  
     * @throws Exception  
     */  
     public static void selectAll() throws Exception{  
          //第一：实例化mongo对象，连接mongodb服务器  包含所有的数据库  
           
          //默认构造方法，默认是连接本机，端口号，默认是27017  
          //相当于Mongo mongo =new Mongo("localhost",27017)  
          Mongo mongo =new Mongo();  
           
          //第二：连接具体的数据库  
          //其中参数是具体数据库的名称，若服务器中不存在，会自动创建  
          DB db=mongo.getDB("myMongo");  
           
          //第三：操作具体的表  
         //在mongodb中没有表的概念，而是指集合  
          //其中参数是数据库中表，若不存在，会自动创建  
          DBCollection collection=db.getCollection("user");  
           
          //查询操作  
          //查询所有  
          //其中类似access数据库中游标概念  
          DBCursor cursor=collection.find();  
          System.out.println("mongodb中的user表结果如下：");  
          while(cursor.hasNext()){  
               System.out.println(cursor.next());  
          }  
     }  
      
     /**  
     * 根据条件查询  
     * @throws Exception  
     */  
     public static void selectPart() throws Exception{  
          //第一：实例化mongo对象，连接mongodb服务器  包含所有的数据库  
           
          //默认构造方法，默认是连接本机，端口号，默认是27017  
          //相当于Mongo mongo =new Mongo("localhost",27017)  
          Mongo mongo =new Mongo();  
           
          //第二：连接具体的数据库  
          //其中参数是具体数据库的名称，若服务器中不存在，会自动创建  
          DB db=mongo.getDB("myMongo");  
           
          //第三：操作具体的表  
         //在mongodb中没有表的概念，而是指集合  
          //其中参数是数据库中表，若不存在，会自动创建  
          DBCollection collection=db.getCollection("user");  
           
      
          //可以直接put  
          BasicDBObject queryObject=new BasicDBObject();  
          queryObject.put("id", 1);  
          DBCursor querycursor=collection.find(queryObject);  
          System.out.println("条件查询如下：");  
          while(querycursor.hasNext()){  
               System.out.println(querycursor.next());  
          }  
     }  
      
     /**  
     * 更新操作  
     * 更新一条记录  
     * @throws Exception  
     */  
     public static void update()throws Exception{  
          //第一：实例化mongo对象，连接mongodb服务器  包含所有的数据库  
           
          //默认构造方法，默认是连接本机，端口号，默认是27017  
          //相当于Mongo mongo =new Mongo("localhost",27017)  
          Mongo mongo =new Mongo();  
           
          //第二：连接具体的数据库  
          //其中参数是具体数据库的名称，若服务器中不存在，会自动创建  
          DB db=mongo.getDB("myMongo");  
           
          //第三：操作具体的表  
         //在mongodb中没有表的概念，而是指集合  
          //其中参数是数据库中表，若不存在，会自动创建  
          DBCollection collection=db.getCollection("user");  
           
          //更新后的对象  
//          第一种更新方式  
          BasicDBObject newBasicDBObject =new BasicDBObject();  
          newBasicDBObject.put("id", 2);  
          newBasicDBObject.put("name", "小红");  
          collection.update(new BasicDBObject().append("id", 1),newBasicDBObject);  
           
//          第二种更新方式  
//          更新某一个字段  
//          BasicDBObject newBasicDBObject =new BasicDBObject().append("$set",new BasicDBObject().append("name", "小红") );  
//          collection.update(new BasicDBObject().append("id", 1).append("name", "小明"),newBasicDBObject);  
  
           
          DBCursor querycursor1=collection.find();  
          System.out.println("更新后结果如下：");  
          while(querycursor1.hasNext()){  
               System.out.println(querycursor1.next());  
          }  
     }  
      
     /**  
     * 删除文档，其中包括删除全部或删除部分  
     * @throws Exception  
     */  
     public static void delete() throws Exception{  
           
          //第一：实例化mongo对象，连接mongodb服务器  包含所有的数据库  
           
          //默认构造方法，默认是连接本机，端口号，默认是27017  
          //相当于Mongo mongo =new Mongo("localhost",27017)  
          Mongo mongo =new Mongo();  
           
          //第二：连接具体的数据库  
          //其中参数是具体数据库的名称，若服务器中不存在，会自动创建  
          DB db=mongo.getDB("myMongo");  
           
          //第三：操作具体的表  
         //在mongodb中没有表的概念，而是指集合  
          //其中参数是数据库中表，若不存在，会自动创建  
          DBCollection collection=db.getCollection("user");  
          BasicDBObject queryObject1=new BasicDBObject();  
          queryObject1.put("id", 2);  
          queryObject1.put("name","小红");  
           
          //删除某一条记录  
         collection.remove(queryObject1);  
          //删除全部  
          //collection.drop();  
           
          DBCursor cursor1=collection.find();  
          System.out.println("删除后的结果如下：");  
          while(cursor1.hasNext()){  
               System.out.println(cursor1.next());  
          }  
           
      
     }  
     
     public static void testOne() throws Exception{
    	 //需要两个jdbc
    	 try{
	    	 JdbcRunner jdbc = new JdbcRunner();
	    	 Mongo mongo = new Mongo("192.168.2.2",27017);
	 		//第二：连接具体的数据库  
	         //其中参数是具体数据库的名称，若服务器中不存在，会自动创建  
	         DB db=mongo.getDB("test");            
	    	 DBCollection collection = db.getCollection("test");
	    	 BasicDBObject document = new BasicDBObject();
	    	 document.put("name", "CX_Worldriceexportpriceaa");
	    	 document.put("updateTime",  new Date());	     	     
	    	 
	    	 
	    	 String select = "select  top 1 * from CX_Worldriceexportpriceaa where varid=231 ";
	
	    	 ResultSet rs = jdbc.query(select);
	    	 ResultSetMetaData m = rs.getMetaData();
	    	 
	    	 
	    	 ArrayList headers = new ArrayList();
	    	 for(int i=0;i<m.getColumnCount();i++){
	    		 headers.add(m.getColumnName(i+1));
	    	 }
	    	 document.put("headers", headers);
	    	 
	    	 ArrayList data = new ArrayList();	    	
	    	 
	    	 while (rs.next()){
	    		 BasicDBObject tmp = new BasicDBObject();
	    		 for(int i =0;i<m.getColumnCount();i++){
	    			 tmp.put(m.getColumnName(i+1), rs.getString(i+1));
	    		 }
	    		 data.add(tmp);
	    	 }
	    	 rs.close();
	    	 document.put("datas", data);
	    	 System.out.println(collection.insert(document));
	    	 
	    	 

	    	 BasicDBObject document2 = new BasicDBObject();
	    	 document2.put("updateTime",  new Date());	     	  
	    	 select = "select  top 1 * from CX_NytanorMAEHONGSON where varid = 4088";
	    	 rs = jdbc.query(select);
	    	 m = rs.getMetaData();
	    	 headers.clear();
	    	 for(int i=0;i<m.getColumnCount();i++){
	    		 headers.add(m.getColumnName(i+1));
	    	 }
	    	 document2.put("headers", headers);
	    	 document2.put("name", "CX_NytanorMAEHONGSON");
	    	 data.clear();
	    	 while (rs.next()){
	    		 BasicDBObject tmp = new BasicDBObject();
	    		 for(int i =0;i<m.getColumnCount();i++){
	    			 tmp.put(m.getColumnName(i+1), rs.getString(i+1));
	    		 }
	    		 data.add(tmp);
	    	 }
	    	 rs.close();
	    	 document2.put("datas", data);
	    	 System.out.println(collection.insert(document2));
	    	 
	    	 
	    	 
	    	 

	    	 BasicDBObject document3 = new BasicDBObject();
	    	 document3.put("name", "cx_marketdata_1_152");
	    	 document3.put("updateTime",  new Date());	     	  
	    	 select = "select  top 1 * from cx_marketdata_1  where varid=152";
	    	 rs = jdbc.query(select);
	    	 m = rs.getMetaData();
	    	 headers.clear();
	    	 for(int i=0;i<m.getColumnCount();i++){
	    		 headers.add(m.getColumnName(i+1));
	    	 }
	    	 document3.put("headers", headers);
	    	 data.clear();
	    	 while (rs.next()){
	    		 BasicDBObject tmp = new BasicDBObject();
	    		 for(int i =0;i<m.getColumnCount();i++){
	    			 tmp.put(m.getColumnName(i+1), rs.getString(i+1));
	    		 }
	    		 data.add(tmp);
	    	 }
	    	 rs.close();
	    	 document3.put("datas", data);
	    	 //collection.
	    	 System.out.println(collection.insert(document3));
	    	 mongo.close();
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }
     }

}
