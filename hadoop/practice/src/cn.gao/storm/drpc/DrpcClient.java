package cn.gao.storm.drpc;

import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;
import org.apache.thrift7.TException;

/**
 * Created by gao on 2016/12/23.
 */
public class DrpcClient {
    public static void main(String[] args) throws TException, DRPCExecutionException {
        DRPCClient client=new DRPCClient("192.168.8.107",3772);
        String result=client.execute("exec","aaa");
        System.out.println(result);
    }
}
/*
* 1.
* */
