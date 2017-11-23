package com.gao.beantest1;

/**
 * Created by tarena on 2016/9/18.
 */
public class Bean4 {
    private Bean1 bean1;
    private Bean2 bean2;
    private Bean3 bean3;

    public void setBean2(Bean2 bean2) {
        this.bean2 = bean2;
    }

    public void setBean1(Bean1 bean1) {
        this.bean1 = bean1;
    }

    public void setBean3(Bean3 bean3) {
        this.bean3 = bean3;
    }

    public Bean1 getBean1() {
        return bean1;
    }

    public Bean3 getBean3() {
        return bean3;
    }

    public Bean2 getBean2() {
        return bean2;
    }
}
