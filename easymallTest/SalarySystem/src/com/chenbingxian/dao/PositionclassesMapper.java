package com.chenbingxian.dao;

import com.chenbingxian.po.Positionclasses;
import com.chenbingxian.vo.Number;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionclassesMapper {
    int deleteByPrimaryKey(@Param("positionClassesId") Integer positionclassesid);

    int insert(Positionclasses record);

    int insertSelective(Positionclasses record);

    Positionclasses selectByPrimaryKey(Integer positionclassesid);

    int updateByPrimaryKeySelective(Positionclasses record);
    int updateByPrimaryKey(Positionclasses record);
     /** 新增的**/
     List<Positionclasses>  selectAllClasses();
    /** 查询班次信息**/
     List<Positionclasses> selectClass();
    /**查询该班次是否正在使用中**/
    Number selectClassUseNo(@Param("positionClassesId") int positionClassesId);
    }