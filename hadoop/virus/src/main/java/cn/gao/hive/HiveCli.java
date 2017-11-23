package cn.gao.hive;

import java.sql.*;

/**
 * Created by gao on 2016/12/19.
 */
public class HiveCli {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn= DriverManager.getConnection("jdbc:hive2://192.168.8.105:10000/zebra","root","root");
        Statement stat=conn.createStatement();
        ResultSet set=null;
        //sql1 和sql2清洗数据将原来的77个字节变为17个字段
        String sql1="create table dataclear (" +
                "reporttime string," +
                "appType bigint," +
                "appSubtype bigint," +
                "userIp string," +
                "userPort bigint," +
                "appServerIP string," +
                "appServerPort bigint," +
                "host string," +
                "cellid string," +
                "appTypeCode bigint ," +
                "interruptType String," +
                "transStatus bigint," +
                "trafficUL bigint," +
                "trafficDL bigint," +
                "retranUL bigint," +
                "retranDL bigint," +
                "procdureStartTime bigint," +
                "procdureEndTime bigint" +
                ") row format delimited fields terminated by '|'";
        String sql2="insert overwrite table dataclear select reportTime,a23,a24,a27,a29,a31,a33,a59,a17," +
                "a19,a68,a55,a34,a35,a40,a41,a20,a21 from zebra";
        //sql3 sql4处理业务逻辑，得到dataproc表 17个字段
        String sql3="create table dataproc(reporttime string," +
                "appType bigint," +
                "appSubtype bigint," +
                "userIp string," +
                "userPort bigint," +
                "appServerIP string," +
                "appServerPort bigint," +
                "host string," +
                "cellid string," +
                "attempts bigint," +
                "accepts bigint," +
                "trafficUL bigint," +
                "trafficDL bigint," +
                "retranUL bigint," +
                "retranDL bigint," +
                "failCount bigint," +
                "transDelay bigint"+
                ") row format delimited fields terminated by '|'";
        String sql4="insert overwrite table dataproc select reporttime," +
                "appType," +
                "appSubtype," +
                "userIp," +
                "userPort," +
                "appServerIP," +
                "appServerPort," +
                "host,"+
                "if(cellid=='','000000000',cellid),"+
                "if(appTypeCode == 103,1,0)," +
                "if(appTypeCode == 103 and find_in_set(','+transStatus+',',',10,11,12,13,14,15," +
                "32,33,34,35,36,37,38,48,49,50,51,52,53,54,55,199,200,201,202,203,204,205,206,302,304,306,') and interruptType = null ,1,0 )," +
                "if(appTypeCode == 103,trafficUL,0)," +
                "if(appTypeCode == 103,trafficDL,0)," +
                "if(appTypeCode == 103,retranUL,0)," +
                "if(appTypeCode == 103,retranDL,0)," +
                "if(appTypeCode == 103 and transStatus == 1 and interruptType == null,1,0)," +
                "if(appTypeCode == 103 ,procdureEndTime - procdureStartTime,0) " +
                "FROM " +
                "dataclear";
        //sql5 和sql6 表计算应用受欢迎程度
        String sql5="create table D_H_HTTP_APPTYPE("+
                "hourid string," +
                "appType bigint," +
                "appSubtype bigint," +
                "attempts bigint," +
                "accepts bigint," +
                "succRatio double," +
                "trafficUL bigint," +
                "trafficDL bigint," +
                "totalTraffic bigint," +
                "retranUL bigint," +
                "retranDL bigint," +
                "retranTraffic bigint," +
                "failCount bigint," +
                "transDelay bigint"+
                ") row format delimited fields terminated by '|'";
        String sql6="insert overwrite table D_H_HTTP_APPTYPE " +
                "select " +
                "reporttime,apptype,appsubtype," +
                "sum(attempts) as attempts," +
                "sum(accepts) as accepts," +
                "round(sum(accepts)/sum(attempts),2) as succRatio," +
                "sum(trafficUL) as trafficUL," +
                "sum(trafficDL) as trafficDL," +
                "sum(trafficUL) + sum(trafficDL) as totalTraffic," +
                "sum(retranUL) as retranUL," +
                "sum(retranDL) as retranDL," +
                "sum(retranUL) + sum(retranDL) as retranTraffic," +
                "sum(failCount) as failCount," +
                "sum(transDelay) as transDelay " +
                "from " +
                "dataproc " +
                "group by " +
                "reporttime,apptype,appsubtype";

        //查询测试
        String sql7="select hourid,apptype,sum(totalTraffic) as tt from D_H_HTTP_APPTYPE " +
                "group by hourid,apptype sort by tt desc limit 5";
        set=stat.executeQuery(sql7);
        while(set.next()){
            System.out.println(set.getString(1)+"\t"+set.getString(2)+"\t"+set.getString(3));
        }

        stat.close();
        conn.close();
    }
}
