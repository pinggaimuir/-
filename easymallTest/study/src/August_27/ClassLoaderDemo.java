package August_27;

import java.io.File;

/**
 * Created by tarena on 2016/8/29.
 */
public class ClassLoaderDemo {
    public static void main(String[] args) {
        new ClassLoaderDemo().getResoure();
    }
    public void getResoure(){
        String path=ClassLoaderDemo.class.getClassLoader().getResource("3.text").getPath();
        File file=new File(path);
        System.out.println(file.getAbsolutePath());
    }
}
