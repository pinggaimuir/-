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
public class MyFilterEND_1times implements NodeFilter{
    public String filtertext;
    public boolean choose;
    boolean found=false;
    public MyFilterEND_1times(String text,boolean containOrNo)
    {
        filtertext=text;
        choose=containOrNo;
    }
    @Override
    public boolean accept(Node node) {
        if(!found)
        {
            if(node.getText().endsWith(filtertext))
            {
                found=true;
                return choose;
            }
            else
                return !choose;
        }
        else
            return !choose;
    }
}
