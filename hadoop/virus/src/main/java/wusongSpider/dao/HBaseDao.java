package wusongSpider.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import wusongSpider.domain.WuSongInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 高健 on 2017/3/10.
 */
public class HBaseDao {
    private Connection conn=null;
    private HBaseAdmin admin=null;
    private HTable table=null;
    public HBaseDao(){
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.8.105:2181");
        Connection conn  = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            table= (HTable) conn.getTable(TableName.valueOf("lowinfo"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取链接异常！！！");
        }
    }

    /**
     * 创建HBase表
     * @throws Exception
     */
    public void createTable()  {
        try {
            //表名对象
            TableName name = TableName.valueOf("lowinfo");
            HTableDescriptor hTable = new HTableDescriptor(name);
            //列族描述
            HColumnDescriptor wusong = new HColumnDescriptor("wusong".getBytes());
            hTable.addFamily(wusong);
            admin.createTable(hTable);
            admin.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 插入HBase数据
     */
    public  void insertData(WuSongInfo wusong){
        try {
            Put put=new Put(Bytes.toBytes(wusong.getTitle()+"_"+wusong.getKeyWord()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("title"),Bytes.toBytes(wusong.getTitle()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("court"),Bytes.toBytes(wusong.getCourt()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("trial_round"),Bytes.toBytes(wusong.getTrialRound()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("case_number"),Bytes.toBytes(wusong.getCaseNumber()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("key_word"),Bytes.toBytes(wusong.getKeyWord()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("issue_number"),Bytes.toBytes(wusong.getIssueNumber()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("judgement_abstract"),Bytes.toBytes(wusong.getJudgementAbstract()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("regulation_item"),Bytes.toBytes(wusong.getRegulationItem()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("litigant"),Bytes.toBytes(wusong.getLitigant()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("after_the_trial"),Bytes.toBytes(wusong.getAfterTheTrial()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("court_identified"),Bytes.toBytes(wusong.getCourtIdentified()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("argument"),Bytes.toBytes(wusong.getArgument()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("has_identified"),Bytes.toBytes(wusong.getHasIdentified()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("court_think"),Bytes.toBytes(wusong.getCourt()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("second_result"),Bytes.toBytes(wusong.getSecondResult()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("the_judge"),Bytes.toBytes(wusong.getTheJudge()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("referee_date"),Bytes.toBytes(wusong.getRefereeDate()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("clerk"),Bytes.toBytes(wusong.getClerk()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("evaluation"),Bytes.toBytes(wusong.getEvaluation()));
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("插入数据异常！！！");
        }
    }

    /**
     * 查询数据
     */
    public List<WuSongInfo> queryData(String reg) {
        try {
            List<WuSongInfo> retuList = new ArrayList<>();
            Scan scan = new Scan();
            //根据正则表达式匹配行健查询
            scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(reg)));
            ResultScanner scanner = table.getScanner(scan);
            Result result = null;
            while ((result = scanner.next()) != null) {
                WuSongInfo wusong = new WuSongInfo();
                wusong.setTitle(new String(result.getValue("wusong".getBytes(),"title".getBytes())));
                wusong.setCourt(new String(result.getValue("wusong".getBytes(),"court".getBytes())));
                wusong.setCaseNumber(new String(result.getValue("wusong".getBytes(),"caseNumber".getBytes())));
                wusong.setTrialRound(new String(result.getValue("wusong".getBytes(),"trial_round".getBytes())));
                wusong.setKeyWord(new String(result.getValue("wusong".getBytes(),"key_word".getBytes())));
                wusong.setIssueNumber(new String(result.getValue("wusong".getBytes(),"issue_number".getBytes())));
                wusong.setJudgementAbstract(new String(result.getValue("wusong".getBytes(),"judgement_abstract".getBytes())));
                wusong.setRegulationItem(new String(result.getValue("wusong".getBytes(),"regulation_item".getBytes())));
                wusong.setLitigant(new String(result.getValue("wusong".getBytes(),"litigant".getBytes())));
                wusong.setAfterTheTrial(new String(result.getValue("wusong".getBytes(),"after_the_trial".getBytes())));
                wusong.setCourtIdentified(new String(result.getValue("wusong".getBytes(),"court_identified".getBytes())));
                wusong.setArgument(new String(result.getValue("wusong".getBytes(),"argument".getBytes())));
                wusong.setHasIdentified(new String(result.getValue("wusong".getBytes(),"has_identified".getBytes())));
                wusong.setCourtThink(new String(result.getValue("wusong".getBytes(),"court_think".getBytes())));
                wusong.setSecondResult(new String(result.getValue("wusong".getBytes(),"second_result".getBytes())));
                wusong.setTheJudge(new String(result.getValue("wusong".getBytes(),"the_judge".getBytes())));
                wusong.setRefereeDate(new String(result.getValue("wusong".getBytes(),"referee_date".getBytes())));
                wusong.setClerk(new String(result.getValue("wusong".getBytes(),"clerk".getBytes())));
                wusong.setEvaluation(new String(result.getValue("wusong".getBytes(),"evaluation".getBytes())));
                retuList.add(wusong);
            }
            return retuList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭连接
     */
    public void close(){
        try {
            table.close();
            admin.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            table=null;
            admin=null;
            conn=null;
        }

    }
}
