/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myWebSpider;

import infoData.ItemInfo;

import java.io.IOException;
import java.util.List;


/**
 *
 * @author Sammy
 */
public class InfoGetRunnable implements Runnable {

    List<ItemInfo> itemlist=null;
    ItemInfoListInterface infoget;
    public boolean over=false;

    public InfoGetRunnable(ItemInfoListInterface infoget){
        this.infoget=infoget;
   }
    
    
//    @Override
    public void run() {
        try {
            itemlist=infoget.getItemInfoList();
            over=true;
        }  catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
}
