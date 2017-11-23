/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider;

import InfoData.ItemInfoData;
import java.io.IOException;
import org.htmlparser.util.ParserException;

/**
 *
 * @author MZH
 */
public interface InfoGetInterface {
    public ItemInfoData[] GetInfo() 
            throws ParserException, IOException, InterruptedException;
    //public void SETpagedata(PageInfoData pagedata1);
}
