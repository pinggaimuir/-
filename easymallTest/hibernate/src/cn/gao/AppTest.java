package cn.gao;

import org.junit.Test;

/**
 * Created by gao on 2017/3/5.
 */
public class AppTest {
    @Test
    public void testSave() throws Exception{
        User5 user=new User5();
        user.setName("zhangsan");
    }
    @Test
    public void testGet() throws Exception{
        User5 user=getById(1);
        System.out.println(user);
    }
}
