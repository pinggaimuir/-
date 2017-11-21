package cn.gao.ht.service;

import cn.gao.ht.pojo.Dept;

import java.util.List;

/**
 * Created by tarena on 2016/10/14.
 */
public interface DeptService {
    List<Dept> findDeptList() throws Exception;
    void updateState(Integer[] deptId, int i);
    void deleteDept(Integer[] deptIds);
    void saveDept(Dept dept);
    Dept selectDeptOne(String deptId);
    //查找部门列表
    List<Dept> getDeptList();
    //删除部门
    void updateDept(Dept dept);
}
