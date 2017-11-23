package cn.bric.pojo;


import javax.persistence.*;
import java.util.Date;

/**
 * 对应数据库中的表CX_IndonesiaIndonesianpalmoilproductsandder
 * Created by 高健 on 2017/3/22.
 */
@Table(name = "CX_IndonesiaIndonesianpalmoilproductsandder")
public class CX_IndonesiaIndonesianpalmoilproductsandder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "EditTime")
    private Date EditTime;
    @Column(name = "VarId")
    private Integer VarId;
    @Column(name = "TimeInt")
    private Integer TimeInt;

    @Column(name = "Nearby")
    private Double Nearby;

    @Column(name = "1月")
    private Double mon1;

    @Column(name = "2月")
    private Double mon2;

    @Column(name = "3月")
    private Double mon3;

    @Column(name = "4月")
    private Double mon4;

    @Column(name = "5月")
    private Double mon5;

    @Column(name = "6月")
    private Double mon6;

    @Column(name = "7月")
    private Double mon7;

    @Column(name = "8月")
    private Double mon8;

    @Column(name = "9月")
    private Double mon9;

    @Column(name = "10月")
    private Double mon10;

    @Column(name = "11月")
    private Double mon11;

    @Column(name = "12月")
    private Double mon12;


}
