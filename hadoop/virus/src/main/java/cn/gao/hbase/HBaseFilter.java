package cn.gao.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gao on 2017/1/9.
 */
public class HBaseFilter {
    public static void main(String[] args) throws IOException {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoop:2181");
        Connection conn= ConnectionFactory.createConnection(conf);
        HTable table=new HTable(conf,"flux");
        Scan scan=new Scan();
        Filter rowFilter=new RowFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes("row1")));
        List<Filter> list=new ArrayList<Filter>();
        list.add(rowFilter);
        FilterList filters=new FilterList(FilterList.Operator.MUST_PASS_ALL,list);
        scan.setFilter(filters);
        ResultScanner scanner=table.getScanner(scan);
        for(Result r:scanner){

        }
    }
}
