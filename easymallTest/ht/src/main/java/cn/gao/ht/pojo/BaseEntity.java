package cn.gao.ht.pojo;

import java.io.Serializable;
import java.util.Date;

//抽象类
public abstract class BaseEntity implements Serializable{
	private String createBy;//创建人
	private String createDept;//创建人所在部门
	private Date createTime;//创建时间
	private String updateBy;//修改人
	private Date updateTime;//修改事件
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
