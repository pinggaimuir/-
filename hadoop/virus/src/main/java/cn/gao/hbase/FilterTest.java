package cn.gao.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gao on 2016/12/20.
 */
public class FilterTest {
    Configuration conf=null;
    @Before
    public void before(){
        conf= HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
    }
    @Test
    public void create() throws IOException {
        Configuration conf= HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        Connection conn=null;
        try{
            conn= ConnectionFactory.createConnection(conf);
            HBaseAdmin admin= (HBaseAdmin) conn.getAdmin();
            HTableDescriptor desc=new HTableDescriptor(TableName.valueOf("testtable"));
            desc.addFamily(new HColumnDescriptor("colfam1"));
            desc.addFamily(new HColumnDescriptor("colfam2"));
            if(admin.tableExists("testtable")){
                System.out.println("table is exists!");
                System.exit(0);
            }else{
                admin.createTable(desc);
                System.out.println("创建表成功！");
            }
        }finally {
            conn.close();
        }


    }
    @Test
    public void insert(){
        Connection conn=null;
        HTable table=null;
        ResultScanner scan=null;
        try {
            conn=ConnectionFactory.createConnection(conf);
            table= (HTable) conn.getTable(TableName.valueOf("testtable"));
            Put put_row1=new Put(Bytes.toBytes("row1"));
            put_row1.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"),Bytes.toBytes("ROW1_QUAL1_VAL"));
            put_row1.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual2"),Bytes.toBytes("ROW1_QUAL2_VAL"));

            Put put_row2=new Put(Bytes.toBytes("row2"));
            put_row2.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"),Bytes.toBytes("ROW2_QUAL1_VAL"));
            put_row2.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual2"),Bytes.toBytes("ROW2_QUAL2_VAL"));

            table.put(put_row1);
            table.put(put_row2);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFileter(){
        Connection conn=null;
        HTable table=null;
        try {
            conn=ConnectionFactory.createConnection(conf);
            table= (HTable) conn.getTable(TableName.valueOf("testtable"));
            Scan scan=new Scan();
            //根据正则表达式对行键进行匹配
            //Filter filter1=new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("^r.*?1$"));
            SingleColumnValueFilter scvf = new SingleColumnValueFilter(
                    Bytes.toBytes("colfam1"),
                    Bytes.toBytes("qual2"),
                    CompareFilter.CompareOp.NOT_EQUAL,
                    new SubstringComparator("BOGUS"));
            scvf.setFilterIfMissing(false);
            scvf.setLatestVersionOnly(true); // OK
            // OK 如果突然发现一行中的列数超过设定的最大值时，整个扫描操作会停止,不会报错
            Filter ccf = new ColumnCountGetFilter(2);
            // OK 筛选某个（值的条件满足的）特定的单元格
            Filter vf = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("QUAL1"));
            // OK 筛选出前缀匹配的列
            Filter cpf = new ColumnPrefixFilter(Bytes.toBytes("qual1"));
            // OK 筛选出第一个每个第一个单元格
            Filter fkof = new FirstKeyOnlyFilter();
            // OK 包含了扫描的上限在结果之内
            Filter isf = new InclusiveStopFilter(Bytes.toBytes("row1"));
            // OK 随机选出一部分的行
            Filter rrf = new RandomRowFilter((float) 0.8);
            // OK 返回所有的行，但值全是空
            Filter kof = new KeyOnlyFilter();
            // OK  筛选匹配行键的前缀成功的行
            Filter pf = new PrefixFilter(Bytes.toBytes("row"));
            // OK 筛选出匹配的所有的行
            Filter rf = new RowFilter(CompareFilter.CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes("row1")));
            // OK 类似于Python itertools中的takewhile
            Filter wmf = new WhileMatchFilter(rf);
            // OK 发现某一行中的一列需要过滤时，整个行就会被过滤掉
            Filter skf = new SkipFilter(vf);

            List<Filter> filters = new ArrayList<Filter>();
            filters.add(vf);
            filters.add(cpf);
            FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters); // OK 综合使用多个过滤器， AND 和 OR 两种关系
            Filter filter2=new ColumnCountGetFilter(1);
            scan.setFilter(fl);
            ResultScanner scanner=table.getScanner(scan);
            for(Result r:scanner){
                for(Cell cell:r.rawCells()){
                    System.out.println("KV"+cell+"-----value:"+Bytes.toString(CellUtil.cloneValue(cell))
                            +"---"+Bytes.toString(CellUtil.cloneFamily(cell))+"---"+Bytes.toString(CellUtil.cloneQualifier(cell)));
                }
                for(KeyValue kv:r.raw()){
                    System.out.println();
                    System.out.println(kv+"==="+kv.getValue());
                    System.out.println();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
