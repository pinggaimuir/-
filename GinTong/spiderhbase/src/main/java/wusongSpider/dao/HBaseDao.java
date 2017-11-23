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
 * 链接hbase，添加、查询记录
 * Created by 高健 on 2017/3/10.
 */
public class HBaseDao {
    private Connection conn=null;
    private HBaseAdmin admin=null;
    private HTable table=null;

    /**
     * 链接hbase
     */
    public HBaseDao(){
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.8.105:2181");
        try {
            conn = ConnectionFactory.createConnection(conf);
//            HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
            table= (HTable) conn.getTable(TableName.valueOf("lowinfo"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取链接异常！！！");
        }
    }
    //测试
//    public static void main(String[] args) {
//        HBaseDao dao=new HBaseDao();
//        List<WuSongInfo> list=dao.queryData(".*?江苏南建建设集团.*?");
//        for(WuSongInfo info:list){
//            System.out.println(info.toString());
//        }
//    }

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
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("source_name"),Bytes.toBytes(wusong.getSourceName()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("issue_number"),Bytes.toBytes(wusong.getIssueNumber()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("judgement_abstract"),Bytes.toBytes(wusong.getJudgementAbstract()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("regulation_item"),Bytes.toBytes(wusong.getRegulationItem()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("litigant"),Bytes.toBytes(wusong.getLitigant()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("plaintiff_said"),Bytes.toBytes(wusong.getPlaintiffSaid()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("after_the_trial"),Bytes.toBytes(wusong.getAfterTheTrial()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("court_identified"),Bytes.toBytes(wusong.getCourtIdentified()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("argument"),Bytes.toBytes(wusong.getArgument()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("has_identified"),Bytes.toBytes(wusong.getHasIdentified()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("court_think"),Bytes.toBytes(wusong.getCourtThink()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("second_result"),Bytes.toBytes(wusong.getSecondResult()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("the_judge"),Bytes.toBytes(wusong.getTheJudge()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("referee_date"),Bytes.toBytes(wusong.getRefereeDate()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("clerk"),Bytes.toBytes(wusong.getClerk()));
            put.addColumn(Bytes.toBytes("wusong"),Bytes.toBytes("evaluation"),Bytes.toBytes(wusong.getEvaluation()));
            table.put(put);
            System.out.println("charuhou====================================");
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
                wusong.setTitle(this.getValue(result,"wusong","title"));
                wusong.setCourt(this.getValue(result,"wusong","court"));
                wusong.setCaseNumber(this.getValue(result,"wusong","case_number"));
                wusong.setTrialRound(this.getValue(result,"wusong","trial_round"));
                wusong.setKeyWord(this.getValue(result,"wusong","key_word"));
                wusong.setSourceName(this.getValue(result,"wusong","source_name"));
                wusong.setIssueNumber(this.getValue(result,"wusong","issue_number"));
                wusong.setJudgementAbstract(this.getValue(result,"wusong","judgement_abstract"));
                wusong.setRegulationItem(this.getValue(result,"wusong","regulation_item"));
                wusong.setLitigant(this.getValue(result,"wusong","litigant"));
                wusong.setPlaintiffSaid(this.getValue(result,"wusong","plaintiff_said"));
                wusong.setAfterTheTrial(this.getValue(result,"wusong","after_the_trial"));
                wusong.setCourtIdentified(this.getValue(result,"wusong","court_identified"));
                wusong.setArgument(this.getValue(result,"wusong","argument"));
                wusong.setHasIdentified(this.getValue(result,"wusong","has_identified"));
                wusong.setCourtThink(this.getValue(result,"wusong","court_think"));
                wusong.setSecondResult(this.getValue(result,"wusong","second_result"));
                wusong.setTheJudge(this.getValue(result,"wusong","the_judge"));
                wusong.setRefereeDate(this.getValue(result,"wusong","referee_date"));
                wusong.setClerk(this.getValue(result,"wusong","clerk"));
                wusong.setEvaluation(this.getValue(result,"wusong","evaluation"));
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
//            admin.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            table=null;
//            admin=null;
            conn=null;
        }
    }
    public String getValue(Result result,String colFimlary,String column){
        byte[] value=result.getValue(Bytes.toBytes(colFimlary),Bytes.toBytes(column));
        return value==null?"":new String(value);
    }
}
