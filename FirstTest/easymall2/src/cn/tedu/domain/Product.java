package cn.tedu.domain;

/**
 * Created by tarena on 2016/9/6.
 */
public class Product {
    private String id;//商品id
    private String name;// 商品名
    private double price;//价格
    private String category;//分类
    private int pnum;//库存
    private String imgurl;//商品图片的部分
    private String description;//商品的描述信息

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 31+(id==null?0:id.hashCode());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
