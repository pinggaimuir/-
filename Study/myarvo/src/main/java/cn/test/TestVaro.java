package cn.test;

import cn.gao.User;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by gao on 2016/12/2.
 */
public class TestVaro {
    @Test
    public void testCreateObject() throws IOException {
        User u1=new User();
        u1.setName("gaojian");
        u1.setGender("nan");
        u1.setAge(22);

        User u2=new User("fengfeng","nan",23);

        User u3=User.newBuilder().setName("jetty").setGender("girl").setAge(20).build();
        User u4=User.newBuilder(u2).setAge(50).build();
        DatumWriter<User> dw=new SpecificDatumWriter<User>(User.class);
        DataFileWriter<User> dfw=new DataFileWriter<User>(dw);
        //schema出入的是要序列化的对象的schema
        dfw.create(u1.getSchema(),new File("1.txt"));
        dfw.append(u2);
        dfw.append(u3);
    }
    @Test
    public void read() throws IOException {

        DatumReader<User> reader=new SpecificDatumReader<User>(User.class);
//        String path=this.getClass().getClassLoader().getResource("1.txt").getPath();
        DataFileReader<User> fileReader=new DataFileReader<User>(new File("1.txt"),reader);
        while(fileReader.hasNext()){
            System.out.println(fileReader.next());
        }
        fileReader.close();
    }
}
