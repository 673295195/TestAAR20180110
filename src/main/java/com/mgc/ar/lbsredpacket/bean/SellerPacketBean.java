package com.mgc.ar.lbsredpacket.bean;

import java.io.Serializable;

/**
 * Created by SkyCheng on 2017/10/11.
 */

public class SellerPacketBean implements Serializable {
    private int id; //红包id
    private String ca_nid;//商铺名称  //新修改的商铺nid
    private String ca_vid;//vip名称	//新修改的会员vid

    // private String ca_nname;//商家id
    private String ca_amount;//金额
    private String ca_time;//时间
    private String ca_year;//年份
    private String ca_months;//月份
    //private String ca_dname;//会员id
    private String ca_switchstate;//红包领取状态
    private String n_shops; //商家名字
    //private String ca_vname;//会员id
    private String ca_receivingcity;//红包领取城市
    private String ca_chance;//（0普通1运气最佳）
    private String n_picture;//头像

    public String getN_picture() {
        return n_picture;
    }

    public void setN_picture(String n_picture) {
        this.n_picture = n_picture;
    }

    public String getCa_nid() {
        return ca_nid;
    }

    public void setCa_nid(String ca_nid) {
        this.ca_nid = ca_nid;
    }

    public String getCa_vid() {
        return ca_vid;
    }

    public void setCa_vid(String ca_vid) {
        this.ca_vid = ca_vid;
    }

    public String getCa_receivingcity() {
        return ca_receivingcity;
    }

    public void setCa_receivingcity(String ca_receivingcity) {
        this.ca_receivingcity = ca_receivingcity;
    }

    public String getCa_chance() {
        return ca_chance;
    }

    public void setCa_chance(String ca_chance) {
        this.ca_chance = ca_chance;
    }

    public String getN_shops() {
        return n_shops;
    }

    public void setN_shops(String n_shops) {
        this.n_shops = n_shops;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCa_amount() {
        return ca_amount;
    }

    public void setCa_amount(String ca_amount) {
        this.ca_amount = ca_amount;
    }

    public String getCa_time() {
        return ca_time;
    }

    public void setCa_time(String ca_time) {
        this.ca_time = ca_time;
    }

    public String getCa_switchstate() {
        return ca_switchstate;
    }

    public void setCa_switchstate(String ca_switchstate) {
        this.ca_switchstate = ca_switchstate;
    }

    public String getCa_year() {
        return ca_year;
    }

    public void setCa_year(String ca_year) {
        this.ca_year = ca_year;
    }

    public String getCa_months() {
        return ca_months;
    }

    public void setCa_months(String ca_months) {
        this.ca_months = ca_months;
    }


}
