package bio.selector;

import java.nio.channels.SelectionKey;

/**
 * Created by gao on 2016/11/28.
 */
public interface Protocol {
    void HandleAccept(SelectionKey key)throws Exception;
    void HandleRead(SelectionKey key)throws Exception;
    void HandleWrite(SelectionKey key)throws Exception;
}
