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
public class MyFilter2StrEND_1times implements NodeFilter{
    
    private String filtertext1;
    private String filtertext2;
    private boolean choose;
    private boolean found=false;

    public MyFilter2StrEND_1times(String text1,String text2,boolean containOrNo){
        
         filtertext1=text1;
         filtertext2=text2;
        choose=containOrNo;
    }
@Override
    public boolean accept(Node node) {
    if(!found){
        String tagContent =node.getText();
        if(tagContent.endsWith(filtertext1)||
               tagContent.endsWith(filtertext2))
            return choose;
        else
            return !choose;
     }
    else
        return !choose;
       
      }
}
