package cn.gao.ht.service;

import cn.gao.ht.mapper.DeptMapper;
import cn.gao.ht.pojo.Dept;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/13.
 */
@Service
public class DeptServiveImpl implements DeptService {
    @Resource
    private DeptMapper deptMapper;

    /**
     * 部门查询
     * @return
     * @throws Exception
     */
    public List<Dept> findDeptList() throws Exception {
        return deptMapper.findDeptList();
    }

    /**
     * 修改部门状态
     * @param deptId
     * @param i
     */
    public void updateState(Integer[] deptId, int i) {
       for(Integer id:deptId){
           deptMapper.updateDeptState(id,i);
       }
    }

    /**
     * 跟车部门id删除部门
     */
    public void deleteDept(Integer[] deptIds) {
        for(Integer id:deptIds){
            deptMapper.deleteDept(id);
        }
    }

    /**
     * 新建部门信息提交保存
     */
    public void saveDept(Dept dept) {
        deptMapper.saveDept(dept);
    }

    /**
     * 查看单个部门信息
     */
    public Dept selectDeptOne(String deptId) {
        return deptMapper.selectDeptOne(deptId);
    }

   //查询所有部门下拉列表
    public List<Dept> getDeptList() {
        return deptMapper.getDeptList();
    }

    //更新部门
    public void updateDept(Dept dept) {
        deptMapper.updateDept(dept);
    }
}
