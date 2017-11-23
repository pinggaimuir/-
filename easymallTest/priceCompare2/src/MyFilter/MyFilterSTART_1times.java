/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyFilter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;

/**
 *
 * @author mazhenhao
 */
public class MyFilterSTART_1times implements NodeFilter{
    public String filtertext;
    public boolean choose;
    boolean found=false;
    public MyFilterSTART_1times(String text,boolean containOrNo)
    {
        filtertext=text;
        choose=containOrNo;
    }
    @Override
    public boolean accept(Node node) {
        if(!found)
        {
            if(node.getText().startsWith(filtertext))
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
