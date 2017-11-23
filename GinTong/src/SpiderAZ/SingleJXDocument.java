package SpiderAZ;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.jsoup.nodes.Document;

import java.util.concurrent.locks.ReentrantLock;

/**
 *获取
 * Created by gao on 2017/2/21.
 */
public class SingleJXDocument {
    private SingleJXDocument(){}
    private static JXDocument document=null;
    private static ReentrantLock lock=new ReentrantLock();
    public static JXDocument getJXDocument(Document doc){
        try{
            lock.lock();
            if(document==null){
                document=new JXDocument(doc);
            }
            return document;
        }finally {
            lock.unlock();
        }
    }
}
