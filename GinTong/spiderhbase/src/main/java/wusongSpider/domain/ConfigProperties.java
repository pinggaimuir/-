package wusongSpider.domain;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 链接hbase、solr配置文件属相类
 * Created by 高健 on 2017/3/11.
 */
public class ConfigProperties {

    private String HBASE_ZOOKEEPER_QUORUM;

    private String HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT;

    private String HBASE_MASTER;

    private String HBASE_ROOTDIR;

    private String DFS_NAME_DIR;

    private String DFS_DATA_DIR;

    private String FS_DEFAULT_NAME;

    private String SOLR_SERVER;

    private String HBASE_TABLE_NAME;

    private String HBASE_TABLE_FAMILY;

    public ConfigProperties(){
        Properties pro = new Properties();
        FileInputStream in = null;
        try {
            String config=ConfigProperties.class.getClassLoader().getResource("config.properties").getPath();
            in = new FileInputStream(config);
            pro.load(in);
            this.HBASE_ZOOKEEPER_QUORUM=pro.getProperty("HBASE_ZOOKEEPER_QUORUM");
            this.HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT=pro.getProperty("HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT");
            this.HBASE_MASTER=pro.getProperty("HBASE_MASTER");
            this.HBASE_ROOTDIR=pro.getProperty("HBASE_ROOTDIR");
            this.DFS_NAME_DIR=pro.getProperty("DFS_NAME_DIR");
            this.DFS_DATA_DIR=pro.getProperty("DFS_DATA_DIR");
            this.FS_DEFAULT_NAME=pro.getProperty("FS_DEFAULT_NAME");
            this.SOLR_SERVER=pro.getProperty("SOLR_SERVER");
            this.HBASE_TABLE_NAME=pro.getProperty("HBASE_TABLE_NAME");
            this.HBASE_TABLE_FAMILY=pro.getProperty("HBASE_TABLE_FAMILY");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getHBASE_ZOOKEEPER_QUORUM() {
        return HBASE_ZOOKEEPER_QUORUM;
    }

    public String getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT() {
        return HBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT;
    }

    public String getHBASE_MASTER() {
        return HBASE_MASTER;
    }

    public String getHBASE_ROOTDIR() {
        return HBASE_ROOTDIR;
    }

    public String getDFS_NAME_DIR() {
        return DFS_NAME_DIR;
    }

    public String getDFS_DATA_DIR() {
        return DFS_DATA_DIR;
    }

    public String getFS_DEFAULT_NAME() {
        return FS_DEFAULT_NAME;
    }

    public String getSOLR_SERVER() {
        return SOLR_SERVER;
    }

    public String getHBASE_TABLE_NAME() {
        return HBASE_TABLE_NAME;
    }

    public String getHBASE_TABLE_FAMILY() {
        return HBASE_TABLE_FAMILY;
    }
}
