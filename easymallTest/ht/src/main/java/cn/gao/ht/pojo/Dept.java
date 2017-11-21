package cn.gao.ht.pojo;

/**
 * 部门表
 * Created by tarena on 2016/10/13.
 */
public class Dept extends BaseEntity {
    private Dept parentDept; //父级部门
    private String deptId;//本门id
    private String deptName;//部门名称
    private Integer state;//部门状态

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Dept getParentDept() {
        return parentDept;
    }

    public void setParentDept(Dept parentDept) {
        this.parentDept = parentDept;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
