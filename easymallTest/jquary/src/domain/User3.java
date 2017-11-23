package domain;

/**
 * Created by tarena on 2016/9/27.
 */
public class User3 {
    private String id;
    private String name;
    private int age;

    @Override
    public String toString() {
        return "User3{" +
                "age=" + age +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
