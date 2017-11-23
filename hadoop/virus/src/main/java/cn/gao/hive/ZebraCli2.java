package cn.gao.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 个网站的表现统计
 * Created by gao on 2016/12/19.
 */
public class ZebraCli2 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn= DriverManager.getConnection("jdbc:hive2://192.168.8.105:10000/zebra","root","root");
        Statement stat=conn.createStatement();
        String sql1="create table D_H_HTTP_HOST("+
                "hourid string," +
                "host string," +
                "appServerIP bigint," +
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
        String sql2="insert overwrite table D_H_HTTP_HOST " +
                "select " +
                "reporttime,host,appServerIP," +
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
                "reporttime,host,appServerIP";

        String sql3="create table D_H_HTTP_CELLID("+
                "hourid string," +
                "cellid string," +
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
        String sql4="insert overwrite table D_H_HTTP_CELLID " +
                "select " +
                "reporttime,cellid," +
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
                "reporttime,cellid";


        String sql5="create table D_H_HTTP_CELLID_HOST("+
                "hourid string," +
                "cellid string," +
                "host string,"+
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
        String sql6="insert overwrite table D_H_HTTP_CELLID_HOST " +
                "select " +
                "reporttime,cellid,host" +
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
                "reporttime,cellid,host";
        stat.execute(sql4);
    }
}
