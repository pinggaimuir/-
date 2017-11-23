package cn.gao.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gao on 2016/12/20.
 */
public class HbaseApi1 {
    public static void main(String[] args) {

    }
    @Test
    public void createTable() throws IOException {
        Configuration conf= HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        HBaseAdmin admin=new HBaseAdmin(conf);
        //表名对象
        TableName name=TableName.valueOf("javaTab");
        HTableDescriptor hTable=new HTableDescriptor(name);
        //列族描述,
        HColumnDescriptor cf2=new HColumnDescriptor("cf2".getBytes());
        hTable.addFamily(cf2);
        HColumnDescriptor cf3=new HColumnDescriptor("cf3".getBytes());
        hTable.addFamily(cf3);
        admin.createTable(hTable);
        admin.close();
    }
    //插入数据
    @Test
    public void insertData() throws IOException {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        HTable table=new HTable(conf,"javaTab".getBytes());

        //指定行键
        Put put=new Put(Bytes.toBytes("rk2"));
        put.add("cf2".getBytes(),"c3".getBytes(),"v3".getBytes());
        put.add("cf2".getBytes(),"c4".getBytes(),"v4".getBytes());
        table.put(put);
        table.close();
    }
    @Test
    public void get() throws IOException {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        HTable table=new HTable(conf,"javaTab".getBytes());

        Get get=new Get("rk1".getBytes());
        get.addColumn("cf2".getBytes(),"c1".getBytes());
        Result result=table.get(get);
        byte[] bytes=result.getValue("cf2".getBytes(),"c1".getBytes());
        System.out.println(new String(bytes));
        table.close();
    }
    //删除行
    @Test
    public void delete() throws IOException {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        HTable table=new HTable(conf,"javaTab".getBytes());
        //指定行键
        Delete delete=new Delete("rk3".getBytes());
        table.delete(delete);
        table.close();
    }
    //删除表
    public void deleTable() throws IOException {
        Configuration conf= HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        HBaseAdmin admin=new HBaseAdmin(conf);
        admin.disableTable("javaTab".getBytes());
        admin.deleteTable("javaTab".getBytes());
        admin.close();
    }
    //条件查询,批量查询
    @Test
    public void select() throws IOException {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        HTable table=new HTable(conf,"javaTab".getBytes());

        //类似于查询条件
        Scan scan=new Scan();
//        Filter filter=new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator("rk1".getBytes()));
        //用正则表达式匹配行健
        Filter filter=new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("^.*1$"));
        //随机获取数据的过滤器
        Filter filter1=new RandomRowFilter(0.5f);
        //指定开始和终止行健，包含开头页包含结尾
        Filter filter2=new InclusiveStopFilter("rk1".getBytes());
        //
//        scan.setFilter(filter1);
        List<Filter> filters=new ArrayList<Filter>();
        filters.add(filter);
        filters.add(filter2);
        filters.add(filter);
        FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ONE,filters);

        ResultScanner rs=table.getScanner(scan);
        Result r=null;
        while((r=rs.next())!=null){

            byte[] bytes=r.getValue("cf2".getBytes(),"c1".getBytes());
            System.out.println(new String(bytes));
        }
    }
}
