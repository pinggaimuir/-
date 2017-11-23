/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyFilter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;

/**
 *
 * @author MZH
 */
public class MyFilterEND  implements NodeFilter{
     public String filtertext;
    public boolean choose;
    public MyFilterEND(String text,boolean containOrNo)
    {
        filtertext=text;
        choose=containOrNo;
    }
    @Override
    public boolean accept(Node node) {
        if(node.getText().endsWith(filtertext))
            return choose;
        else
            return !choose;
    }
}
