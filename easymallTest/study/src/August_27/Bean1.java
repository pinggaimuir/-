package August_27;

/**
 * Created by tarena on 2016/8/31.
 */
public class Bean1 {
    private String name;
    private int age;
    private String addr;
    public Bean1(){}

    public Bean1(String name, int age,String addr ) {
        this.addr = addr;
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toUpper(String s){
        return s.toUpperCase();
    }
}
