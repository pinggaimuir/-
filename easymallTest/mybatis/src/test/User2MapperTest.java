package test;

import junit.framework.TestCase;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by tarena on 2016/10/8.
 */
public class User2MapperTest extends TestCase {
    SqlSessionFactory sqlSessionFactory =null;
    protected void setUp() throws Exception {
        InputStream input= Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory=new SqlSessionFactoryBuilder().build(input);
    }

}
