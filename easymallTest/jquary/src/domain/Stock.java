package domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tarena on 2016/9/27.
 */
public class Stock {
    private double yesterday;
    private double today;
    private String name;
    private String id;
    private double now;

    Set set=new HashSet();

    public double getNow() {
        return now;
    }

    public void setNow(double now) {
        this.now = now;
    }

    public Stock(String id, String name, double today, double yesterday) {
        this.id = id;
        this.name = name;
        this.today = today;
        this.yesterday = yesterday;
        this.now=today;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getToday() {
        return today;
    }

    public void setToday(double today) {
        this.today = today;
    }

    public double getYesterday() {
        return yesterday;
    }

    public void setYesterday(double yesterday) {
        this.yesterday = yesterday;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
