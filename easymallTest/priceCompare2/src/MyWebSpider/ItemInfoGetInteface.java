/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider;

import InfoData.ItemInfoData;
import java.io.IOException;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 *
 * @author mazhenhao
 */
public interface ItemInfoGetInteface {
    public ItemInfoData[] GetArrayFromList(NodeList pagItemList)
            throws ParserException, IOException;  
}
