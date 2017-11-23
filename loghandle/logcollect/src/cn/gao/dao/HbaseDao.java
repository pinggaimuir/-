package cn.gao.dao;

import cn.gao.domain.FluxInfo;
import cn.gao.util.FluxUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gao on 2017/1/31.
 */
public class HbaseDao {
    private HbaseDao(){}

    /**
     * ，根据正则表达式过了不起查询数据
     * @param reg
     * @return
     */
    public static List<FluxInfo> queryData(String reg){
        try{
            Configuration conf= HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum","192.168.8.151:2181");
            Connection  conn= ConnectionFactory.createConnection(conf);
            HBaseAdmin admin= (HBaseAdmin) conn.getAdmin();

            HTable table= (HTable) conn.getTable(TableName.valueOf("flux"));

            List<FluxInfo> retuList=new ArrayList<>();
            Scan scan=new Scan();
            scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator(reg)));
            ResultScanner scanner=table.getScanner(scan);
            Result result=null;
            while((result=scanner.next())!=null){
                String time =new String(result.getValue("cf1".getBytes(),"time".getBytes()));
                String uv_id  =new String(result.getValue("cf1".getBytes(),"uv_id".getBytes()));
                String ss_id  =new String(result.getValue("cf1".getBytes(),"ss_id".getBytes()));
                String ss_time  =new String(result.getValue("cf1".getBytes(),"ss_time".getBytes()));
                String urlname  =new String(result.getValue("cf1".getBytes(),"urlname".getBytes()));
                String cip   =new String(result.getValue("cf1".getBytes(),"cip".getBytes()));
                FluxInfo fi=new FluxInfo(time, uv_id, ss_id, ss_time, urlname, cip);
                retuList.add(fi);
            }
            return retuList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void insertData(FluxInfo fi){
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","192.168.8.151:2181");
        try {
            Connection conn=ConnectionFactory.createConnection(conf);
            HTable table= (HTable) conn.getTable(TableName.valueOf("flux"));

            Put put=new Put(Bytes.toBytes(fi.getTime()+"_"+fi.getUv_id()+fi.getSs_id()+"_"+fi.getCip()+"_"+ FluxUtils.randNum(8)));
            put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("time"),Bytes.toBytes(fi.getTime()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("uv_id"),Bytes.toBytes(fi.getUv_id()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("ss_id"),Bytes.toBytes(fi.getSs_id()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("ss_time"),Bytes.toBytes(fi.getSs_time()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("urlname"),Bytes.toBytes(fi.getUrlname()));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("cip"),Bytes.toBytes(fi.getCip()));
            table.put(put);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
