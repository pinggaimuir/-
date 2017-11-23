package cn.gao.po;

/**
 * Created by tarena on 2016/10/6.
 */
public class ItemsQueryVo {
    /*商品信息*/
    private Items items;
    /*商品扩展类*/
    private ItemsCustom itemsCustom;

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public ItemsCustom getItemsCustom() {
        return itemsCustom;
    }

    public void setItemsCustom(ItemsCustom itemsCustom) {
        this.itemsCustom = itemsCustom;
    }
}
