package com.mgc.ar.lbsredpacket.bean;

import java.io.Serializable;

/**
 * Created by SkyCheng on 2017/9/28.
 */

public class SellerBean implements Serializable {


    private int id;
    private String n_shops;//商铺名称
    private String n_envelope;//红包数
    private double n_location;//经纬度
    private double n_lat;
    /*@DateTimeFormat( pattern = "yyyy-MM-dd" )*/
    private String n_starttime;//活动开始时间
    /*@DateTimeFormat( pattern = "yyyy-MM-dd" )*/
    private String n_endtime;//活动结束日期
    private String n_picture;//商家头像
    private String n_redstate;//红包状态
    private String n_amount;//金额
    private String n_max;//红包最大值
    private String n_small;//最小值

    private String n_enable;//撤回。。打开状态(0未撤回1撤回)
    private String n_open;//打开红包数量
    private String n_notopen;//未打开红包数量
    private String n_balance;//红包余额
    private String n_expiredstate;//红包过期状态(0未过期1代表过期)
    private String n_shopcontextid;//背景图_id
    private String n_shoplogoid;//关联商家logo_id
    private String n_greetingsid;//祝福语——id
    private String n_shoptime;//商家派发时间


    public String getN_enable() {
        return n_enable;
    }

    public void setN_enable(String n_enable) {
        this.n_enable = n_enable;
    }

    public String getN_open() {
        return n_open;
    }

    public void setN_open(String n_open) {
        this.n_open = n_open;
    }

    public String getN_notopen() {
        return n_notopen;
    }

    public void setN_notopen(String n_notopen) {
        this.n_notopen = n_notopen;
    }

    public String getN_balance() {
        return n_balance;
    }

    public void setN_balance(String n_balance) {
        this.n_balance = n_balance;
    }

    public String getN_expiredstate() {
        return n_expiredstate;
    }

    public void setN_expiredstate(String n_expiredstate) {
        this.n_expiredstate = n_expiredstate;
    }

    public String getN_shopcontextid() {
        return n_shopcontextid;
    }

    public void setN_shopcontextid(String n_shopcontextid) {
        this.n_shopcontextid = n_shopcontextid;
    }

    public String getN_shoplogoid() {
        return n_shoplogoid;
    }

    public void setN_shoplogoid(String n_shoplogoid) {
        this.n_shoplogoid = n_shoplogoid;
    }

    public String getN_greetingsid() {
        return n_greetingsid;
    }

    public void setN_greetingsid(String n_greetingsid) {
        this.n_greetingsid = n_greetingsid;
    }

    public String getN_shoptime() {
        return n_shoptime;
    }

    public void setN_shoptime(String n_shoptime) {
        this.n_shoptime = n_shoptime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getN_shops() {
        return n_shops;
    }

    public void setN_shops(String n_shops) {
        this.n_shops = n_shops;
    }

    public String getN_envelope() {
        return n_envelope;
    }

    public void setN_envelope(String n_envelope) {
        this.n_envelope = n_envelope;
    }

    public double getN_location() {
        return n_location;
    }

    public void setN_location(double n_location) {
        this.n_location = n_location;
    }

    public double getN_lat() {
        return n_lat;
    }

    public void setN_lat(double n_lat) {
        this.n_lat = n_lat;
    }

    public String getN_starttime() {
        return n_starttime;
    }

    public void setN_starttime(String n_starttime) {
        this.n_starttime = n_starttime;
    }

    public String getN_endtime() {
        return n_endtime;
    }

    public void setN_endtime(String n_endtime) {
        this.n_endtime = n_endtime;
    }

    public String getN_picture() {
        return n_picture;
    }

    public void setN_picture(String n_picture) {
        this.n_picture = n_picture;
    }

    public String getN_redstate() {
        return n_redstate;
    }

    public void setN_redstate(String n_redstate) {
        this.n_redstate = n_redstate;
    }

    public String getN_amount() {
        return n_amount;
    }

    public void setN_amount(String n_amount) {
        this.n_amount = n_amount;
    }

    public String getN_max() {
        return n_max;
    }

    public void setN_max(String n_max) {
        this.n_max = n_max;
    }

    public String getN_small() {
        return n_small;
    }

    public void setN_small(String n_small) {
        this.n_small = n_small;
    }
}
