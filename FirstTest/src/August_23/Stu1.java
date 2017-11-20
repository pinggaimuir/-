package August_23;

/**
 * Created by tarena on 2016/8/23.
 */
public class Stu1 {
    private long id;
    private String name;
    private String dormitory;
    private long card;

    public long getCard() {
        return card;
    }

    public void setCard(long card) {
        this.card = card;
    }

    public String getDormitory() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return "Stu1{" +
                "card=" + card +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", dormitory='" + dormitory + '\'' +
                '}';
    }

    public Stu1() {
    }

    public Stu1(long id, String name, String dormitory,long card ) {
        this.card = card;
        this.dormitory = dormitory;
        this.id = id;
        this.name = name;
    }
}
