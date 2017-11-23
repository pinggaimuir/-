package wusongSpider.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import wusongSpider.domain.ConfigProperties;

import java.io.IOException;

/**
 * 将hbase中的数据导入solr中建立索引
 * Created by 高健 on 2017/3/11.
 */
public class HBaseToSolr {
    public static void main(String[] args1) throws Exception  {


        ConfigProperties tutorialProperties = new ConfigProperties();

        String tbName = tutorialProperties.getHBASE_TABLE_NAME();

        String tbFamily = tutorialProperties.getHBASE_TABLE_FAMILY();

        System.out.println("tbName ===="+tbName);

        System.out.println("tbFamily ===="+tbFamily);

        HttpSolrServer solrServer = new HttpSolrServer("http://hadoop:8983/solr/lawinfo");

        Configuration conf = HBaseConfiguration.create();

        System.out.println("hbase.zookeeper.quorum==="+tutorialProperties.getHBASE_ZOOKEEPER_QUORUM());

        conf.set("hbase.zookeeper.quorum",tutorialProperties.getHBASE_ZOOKEEPER_QUORUM());

        conf.set("hbase.zookeeper.property.clientPort",

                tutorialProperties.getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT());

        System.out.println("hbase.zookeeper.property.clientPort==="+tutorialProperties.getHBASE_ZOOKEEPER_PROPERTY_CLIENT_PORT());

        conf.set("hbase.master", tutorialProperties.getHBASE_MASTER());

        conf.set("hbase.rootdir", tutorialProperties.getHBASE_ROOTDIR());

        conf.set("solr.server", tutorialProperties.getSOLR_SERVER());

        HTable table = new HTable(conf, tbName); // 这里指定HBase表名称

        Scan scan = new Scan();

        scan.addFamily(Bytes.toBytes(tbFamily)); // 这里指定HBase表的列族

        scan.setCaching(500);

        scan.setCacheBlocks(false);

        ResultScanner ss = table.getScanner(scan);

        System.out.println("start ...");

        int i = 0;

        try {

            for (Result r : ss) {

                SolrInputDocument solrDoc = new SolrInputDocument();

                for (KeyValue kv : r.raw()) {

                    String fieldName = new String(kv.getQualifier());

                    String fieldValue = new String(kv.getValue());


                        solrDoc.addField(fieldName, fieldValue);


//                    if(fieldName.equalsIgnoreCase("DOCCONTENT")) {
//
//                        solrDoc.addField("content",fieldValue);
//
//                    }

                }

                solrServer.add(solrDoc);

                solrServer.commit(true, true,true);

                i = i + 1;

                System.out.println("已经成功处理 "+ i + " 条数据");

            }

            ss.close();

            table.close();

            System.out.println("done !");

        } catch (IOException e) {

        } finally {

            ss.close();

            table.close();

            System.out.println("erro !");

        }

    }
}
