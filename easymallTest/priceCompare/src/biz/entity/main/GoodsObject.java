package biz.entity.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 网页中的商品
 */
@Entity
@Table(name = "t_goods")
public class GoodsObject {
	private int id;
	private String goodsName;// 名称
	private String picUrl;// 图片
	private float price = 0;// 价格
	private int sellAmount = 0;// 销量
	private int type = 0;// 商品类型
	private int comments = 0;// 好评数量
	private float commentPercent = 0;// 好评率
	private String goodsUrl;
	private String goodsFrom;// 来源
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getGoodsFrom() {
		return goodsFrom;
	}

	public void setGoodsFrom(String goodsFrom) {
		this.goodsFrom = goodsFrom;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public float getCommentPercent() {
		return commentPercent;
	}

	public void setCommentPercent(float commentPercent) {
		this.commentPercent = commentPercent;
	}

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(int sellAmount) {
		this.sellAmount = sellAmount;
	}

	@Override
	public String toString() {
		return "GoodsObject [goodsName=" + goodsName + ", picUrl=" + picUrl + ", price=" + price + ", sellAmount=" + sellAmount + ", type=" + type
				+ ", comments=" + comments + ", commentPercent=" + commentPercent + ", goodsUrl=" + goodsUrl + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsObject other = (GoodsObject) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
