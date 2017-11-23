//package test;
//
//import junit.framework.TestCase;
//import org.mybatis.generator.api.MyBatisGenerator;
//import org.mybatis.generator.config.Configuration;
//import org.mybatis.generator.config.xml.ConfigurationParser;
//import org.mybatis.generator.internal.DefaultShellCallback;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by tarena on 2016/10/6.
// */
//public class mybatis_generator extends TestCase {
//    public void generator()throws Exception{
//        List<String> warnings=new ArrayList();
//        boolean overwrite=true;
//        File configFile=new File("generatorConfig.xml");
//        ConfigurationParser cp = new ConfigurationParser(warnings);
//        Configuration config = cp.parseConfiguration(configFile);
//        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
//                callback, warnings);
//        myBatisGenerator.generate(null);
//    }
//    public void testGenerator(){
//        try {
//            mybatis_generator generatorSqlmap = new mybatis_generator();
//            generatorSqlmap.generator();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
