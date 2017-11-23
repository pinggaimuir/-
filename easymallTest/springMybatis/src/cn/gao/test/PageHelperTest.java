package cn.gao.test;


import cn.gao.mapper.ItemsMapper;
import cn.gao.po.Items;
import cn.gao.po.ItemsExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;

/**
 * 分页测试
 * Created by tarena on 2016/10/10.
 */
public class PageHelperTest extends TestCase {
    private ApplicationContext context=null;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
    }

    public void testPageHelper() throws IOException {
//        InputStream inputStream = Resources.getResourceAsStream("mybatis/sqlMapConfig.xml");
//        //创建会话工厂，传入mybitis配置信息
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        //通过工厂得到sqlsession
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        ItemsMapper itemsMapper=sqlSession.getMapper(ItemsMapper.class);
        //执行查询并且分页
        ItemsMapper itemsMapper=context.getBean(ItemsMapper.class);

        ItemsExample itemsExample=new ItemsExample();
        PageHelper.startPage(1,5);
        List<Items> list=itemsMapper.selectByExample(itemsExample);
        for(Items item:list){
            System.out.println(item.getId());
        }
        //获取分页信息
        PageInfo<Items> pageInfo=new PageInfo<Items>(list);
        long total=pageInfo.getTotal();
        System.out.println("共有商品："+total);
    }
}
