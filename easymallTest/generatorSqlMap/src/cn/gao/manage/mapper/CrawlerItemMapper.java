package cn.gao.manage.mapper;

import cn.gao.manage.pojo.CrawlerItem;
import cn.gao.manage.pojo.CrawlerItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CrawlerItemMapper {
    int countByExample(CrawlerItemExample example);

    int deleteByExample(CrawlerItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CrawlerItem record);

    int insertSelective(CrawlerItem record);

    List<CrawlerItem> selectByExample(CrawlerItemExample example);

    CrawlerItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CrawlerItem record, @Param("example") CrawlerItemExample example);

    int updateByExample(@Param("record") CrawlerItem record, @Param("example") CrawlerItemExample example);

    int updateByPrimaryKeySelective(CrawlerItem record);

    int updateByPrimaryKey(CrawlerItem record);
}