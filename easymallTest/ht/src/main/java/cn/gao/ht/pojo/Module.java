package cn.gao.ht.pojo;

/**
 * Created by tarena on 2016/10/17.
 */
public class Module extends BaseEntity {

    private Module parent;//父级模块

    private Integer ctype;//类型 1 主菜单 2 操作页面 3 按钮

    private Integer state;// 状态

    private Integer orderNo; //排序号

    private String remark;//备注

    private String name;//模块名称

    private String moduleId;//模块id

    private String pId;//ztree 的父级节点

    private boolean checked;//角色是否拥有该权限  有为true 没有为false

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId(){
        return moduleId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCtype() {
        return ctype;
    }

    public void setCtype(Integer ctype) {
        this.ctype = ctype;
    }

    public Module getParent() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
