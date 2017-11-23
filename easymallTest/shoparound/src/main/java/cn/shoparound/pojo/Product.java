package cn.shoparound.pojo;

/**
 * 商品信息
 * Created by 高健 on 2017/5/17.
 */
public class Product {
    //商品名
    private String name;

    //商品的url地址
    private String url;

    //商品的价格
    private double price;

    //商品的简介
    private String introduction;

    //商品图片地址
    private String imageurl;

    //商户地址
    private String address;

    //商品月销量
    private String salecount;

    //商品总评论数
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalecount() {
        return salecount;
    }

    public void setSalecount(String salecount) {
        this.salecount = salecount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", price=" + price +
                ", introduction='" + introduction + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", address='" + address + '\'' +
                ", salecount='" + salecount + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
