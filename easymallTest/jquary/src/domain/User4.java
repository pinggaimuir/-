package domain;

/**
 * Created by tarena on 2016/9/28.
 */
public class User4 {
    private int id;
    private String name;
    private int age;
    private String sex;

    public User4(String sex, int age, int id, String name) {
        this.sex = sex;
        this.age = age;
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
