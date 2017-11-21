package rabbit;

import static org.junit.Assert.*;

/**
 * Created by gao on 2016/11/25.
 */
public class NotCrawlUrlQueueTest {

    @org.junit.Test
    public void testUrl() throws Exception {
        NotCrawlUrlQueue queue=new NotCrawlUrlQueue();
//        queue.addUrl("http://www.cnblogs.com");
//        queue.addUrl("www.baidu.com");
//        queue.addUrl("www.sina.com");
        String url=queue.getUrl();
        System.out.println(url);
    }


}