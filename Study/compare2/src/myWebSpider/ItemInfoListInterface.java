/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myWebSpider;

import infoData.ItemInfo;

import java.io.IOException;
import java.util.List;


/**
 *该接口用于规范获取商品项信息列表的类
 * @author Sammy
 */
public interface ItemInfoListInterface {
    /**
     * 通过页面URL获取商品列表
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<ItemInfo> getItemInfoList()
            throws  IOException, InterruptedException;
}
