package cn.gao.hive;


import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Created by gao on 2016/12/16.
 */
public class HiveJdbcCli {
    private static String driverName="org.apache.hive.jdbc.HiveDriver";
    //HiveServer远程连接的端口，默认为10000
    private static String url="jdbc:hive2://192.168.8.107:10000/default";
    private static String username="root";
    private static String password="root";
    private static String sql;
    private static ResultSet resultSet;
    private static final Logger log= Logger.getLogger(HiveJdbcCli.class);

    public static void main(String[] args)  {
        Connection conn=null;
        Statement stat=null;
        try {
            conn=getConn();
            stat=conn.createStatement();

            //1、存在就先删除
            String tableName=dropTable(stat);
            //2 、不存在就创建
            createTable(stat,tableName);
            //3、查看创建的表
            showTables(stat,tableName);
            //4、describe table表
            descTables(stat,tableName);
            //5、load data into table
            loadData(stat,tableName);
            //6、select * query
            selectData(stat,tableName);
            //7、regular hive query
            countData(stat,tableName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(driverName+"is not found!");
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Connection error",e);
            System.exit(1);
        }finally {

                try {
                    if(resultSet!=null){
                        resultSet.close();
                        resultSet=null;
                    }
                    if(stat!=null){
                        stat.close();
                        stat=null;
                    }
                    if(conn!=null){
                        conn.close();
                        conn=null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    private static void countData(Statement stat, String tableName) throws SQLException {
        sql = "select count(1) from "+tableName;
        System.out.println("running sql:"+sql);
        resultSet=stat.executeQuery(sql);
        while (resultSet.next()){
            System.out.println("count---->"+resultSet.getString(1));
        }
    }

    private static void selectData(Statement stat, String tableName) throws SQLException {
        sql="select * from "+ tableName;
        System.out.println("running sql:"+sql);
        resultSet=stat.executeQuery(sql);
        while(resultSet.next()){
            System.out.println(resultSet.getString(1)+"\t"+resultSet.getString(2));
        }
    }

    private static void loadData(Statement stat, String tableName) throws SQLException {
        String filePath="/opt/mydata/english.txt";
        sql="load data local inpath '"+filePath+"' into table "+tableName;
        System.out.println("running sql:"+sql);
        stat.execute(sql);
    }

    private static void descTables(Statement stat, String tableName) throws SQLException {
        sql = "describe " + tableName;
        System.out.println("Running:" + sql);
        System.out.println("running sql:"+sql);
        resultSet = stat.executeQuery(sql);
        System.out.println("执行 describe table 运行结果:");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2));
        }
    }

    private static void showTables(Statement stat, String tableName) throws SQLException {
        sql = "show tables '"+tableName+"'";
        System.out.println("running sql:"+sql);
        resultSet=stat.executeQuery(sql);
        while (resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
    }

    //根据表名创建表
    private static void createTable(Statement stat, String tableName) throws SQLException {
        sql="create table "+tableName+" (key int,value string) row format delimited fields terminated by '\t'";
        System.out.println("running sql:"+sql);
        stat.execute(sql);
    }

    //
    private static String dropTable(Statement stat) throws SQLException {
        //创建表名
        String tableName="testHIve";
        sql="drop table "+tableName;
        System.out.println("running sql:"+sql);
        stat.execute(sql);
        return tableName;
    }
    //获取链接
    private static Connection getConn() throws ClassNotFoundException,
            SQLException {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}
