package cn.tedu.domain;

/**
 * Created by tarena on 2016/9/7.
 */
public class Product {
    private String id;//商品id
    private String name;// 商品名
    private double price;//价格
    private String category;//分类
    private String pnum;//库存
    private String imgurl;//商品图片的部分
    private String description;//商品的描述信息
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj==null){
            return false;
        }
        if(obj  instanceof Product){
            Product other=(Product) obj;
            if(other.getId()==null||!other.getId().equals(id)){
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
