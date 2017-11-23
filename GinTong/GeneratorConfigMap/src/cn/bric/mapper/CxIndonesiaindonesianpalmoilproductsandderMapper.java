package cn.bric.mapper;

import cn.bric.pojo.CxIndonesiaindonesianpalmoilproductsandder;
import cn.bric.pojo.CxIndonesiaindonesianpalmoilproductsandderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CxIndonesiaindonesianpalmoilproductsandderMapper {
    int countByExample(CxIndonesiaindonesianpalmoilproductsandderExample example);

    int deleteByExample(CxIndonesiaindonesianpalmoilproductsandderExample example);

    int insert(CxIndonesiaindonesianpalmoilproductsandder record);

    int insertSelective(CxIndonesiaindonesianpalmoilproductsandder record);

    List<CxIndonesiaindonesianpalmoilproductsandder> selectByExample(CxIndonesiaindonesianpalmoilproductsandderExample example);

    int updateByExampleSelective(@Param("record") CxIndonesiaindonesianpalmoilproductsandder record, @Param("example") CxIndonesiaindonesianpalmoilproductsandderExample example);

    int updateByExample(@Param("record") CxIndonesiaindonesianpalmoilproductsandder record, @Param("example") CxIndonesiaindonesianpalmoilproductsandderExample example);
}