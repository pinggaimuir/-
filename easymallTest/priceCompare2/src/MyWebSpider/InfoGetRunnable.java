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
public class InfoGetRunnable implements Runnable {

    ItemInfoData[] itemlist=null;
   // PageInfoData pagedata;
    //String pageurl;
    InfoGetInterface infoget;
    public boolean over=false;
    public InfoGetRunnable(InfoGetInterface infoget1){
    
        //pagedata=pagedata1;
       // pageurl=pageurl1;
        infoget=infoget1;
   }
    
    
    @Override
    public void run() {
       // infoget.SETpagedata(pagedata);
        try {
            itemlist=infoget.GetInfo();
            over=true;
        } catch (ParserException ex) {
            //Logger.getLogger(InfoGetRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
           // Logger.getLogger(InfoGetRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            //Logger.getLogger(InfoGetRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
