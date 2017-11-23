
/**
 * Created by 高健 on 2017/4/13.
 */
public class Test {
    public static void main(String[] args) {
        Name name=new Name();
        Test test=new Test();
        test.testTransfor("gaojian",name);
        System.out.println(name.getMyname());

    }
    public int testTransfor(String aname,Name name){
        name.setMyname(aname);
        return 0;
    }
}