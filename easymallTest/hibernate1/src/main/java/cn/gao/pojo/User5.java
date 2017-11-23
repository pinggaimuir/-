package cn.gao.pojo;

/**
 * 实体类
 * Created by gao on 2017/3/5.
 */
public class User5 {
    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "User5{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
