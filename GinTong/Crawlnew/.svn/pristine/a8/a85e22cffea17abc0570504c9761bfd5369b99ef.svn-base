package cn.futures.data.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class DataLog {
	public static DataLog instance = null;
    private FileWriter fw;
    private int lastdate;
    
	private DataLog()
	{
		try{
			newFile();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	public synchronized  static DataLog getInstance()
    {
		if (instance == null)
        {
            instance = new DataLog();
        }
		if( true ){//已经室昨天的记录文件了，今天要生成一个新的
		
		}
        return instance;
    }
	
    /// <summary>
    /// 写入日志
    /// </summary>
    public synchronized void println(String line) throws Exception
    {    	
    	//写入记录    	
    	reopenFW();
    	fw.write(line);
    	fw.write("\n");
    	fw.flush();
    }
    
    
    private void reopenFW(){
    	if(fw==null || lastdate != (new Date()).getDate()){
    		newFile();
    	}
    }
    
    public synchronized void print(String line) throws Exception{
    	reopenFW();
    	fw.write(line);;
    	fw.flush();
    }
    
    private synchronized void newFile()  {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = new Date();
    	lastdate = date.getDate();
        String dateStr = format.format(date);        
        String filename = "data."+dateStr+".dat";
		Properties prop = this.loadProperty();
        String path = prop.getProperty("dataFilePath");
        File file = new File(path);
        if  (!file.exists())      
        {                   
            file.mkdirs();    
        } 
//        else{
//        	file.renameTo(new File(file.getAbsoluteFile()+"_bak"));
//        	file.mkdirs();
//        }
        try{
        	if(fw != null ){
        		fw.close();
        	}
        	fw = new FileWriter(path+filename,true);
        	//println("创建数据文件成功");
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    public void close() throws Exception{
    	fw.close();
    }
    

	/**
	 * 读取chinaBric.properties配置文件类
	 * @return
	 */
	private Properties loadProperty() {
        Properties prop=new Properties();
        try {
            InputStream is=this.getClass().getResourceAsStream("/info.properties");
            prop.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
	
}
