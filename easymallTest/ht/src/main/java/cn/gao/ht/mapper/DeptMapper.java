package cn.gao.ht.mapper;

import cn.gao.ht.pojo.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tarena on 2016/10/14.
 */
public interface DeptMapper {
    List<Dept> findDeptList()throws Exception;
    /*更改部门状态*/
    void updateDeptState(@Param("deptId") Integer deptId, @Param("state") Integer state);
    /*根据deptid删除部门*/
    void deleteDept(Integer id);
    /*新增保存部门信息*/
    void saveDept(Dept dept);
    /*根据deptId查询单个部门信息*/
    Dept selectDeptOne(String deptId);
    /*获区所有部门的列表*/
    List<Dept> getDeptList();
    /*更新部门信息*/
    void updateDept(Dept dept);
}
