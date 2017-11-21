package cn.gao.ht.pojo;

import javax.validation.constraints.NotNull;

/**
 * Created by tarena on 2016/10/17.
 */
public class Role extends BaseEntity{
    @NotNull(message = "{id.isNotNUll}")
    private String roleId; //角色id

    private String name; //角色名称

    private String remark;//角色说明

    private Integer orderNo;//排序号
    private boolean checked;//用户是否拥有该角色

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId(){
        return roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
