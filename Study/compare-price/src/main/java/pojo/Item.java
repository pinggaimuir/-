package pojo;

/**
 * 商品信息
 * Created by gao on 2016/11/20.
 */
public class Item implements Comparable<Item>{
    private Long id;//商品id
    private String title;//商品标题
    private String price;//商品价格
    private String url;//商品url地址链接
    private String image;//商品图片链接
    private String intro;//商品介绍

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    //根据两个商品的价格进行比较
    public int compareTo(Item o) {
        String price1=this.price.trim();
        String price2=null;
        if(o!=null){
            o.price.trim();
        }else{
            return 0;
        }
        int len1=0;
        int len2=0;
        if((price1!=null&&price2!=null)|(price1!=""&&price2!="")){
            len1=price1.length();
            len2=price2.length();
        }
        if(len1>len2){
            return 1;
        }else if(len1<len2){
            return 0;
        }else{
            int larger=1;
            for (int i = 0; i < len1; i++) {
                if(price1.charAt(len1-1-i)<price2.charAt(len1-1-i)){
                    larger=0;
                    break;
                }
            }
            return larger;
        }
    }
}
