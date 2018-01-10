package com.mgc.ar.lbsredpacket.bean;

import java.io.Serializable;

/**
 * Created by SkyCheng on 2017/10/12.
 */

public class SellerCouponBean implements Serializable {
    private int id; //红包id
    //private String p_nname;//商铺id
    private String p_nid;//商铺名称  //新修改的商铺nid
    private String p_vid;//vip名称	//新修改的会员vid
    private String n_shops; //商家名字
    //private String p_vname;//vip id
    private String p_amount;//优惠券
    private String p_time;//日期
    private String p_switchstate;//状态
    private String p_months;//月
    private String p_year;//年
    private String vi_receivingcity;//红包领取城市
    private String p_enddate;//结束日期
    private String n_basemap  ;//背景图
    private String n_regular    ;//使用规则

    public String getN_regular() {
        return n_regular;
    }

    public void setN_regular(String n_regular) {
        this.n_regular = n_regular;
    }

    public String getN_basemap() {
        return n_basemap;
    }

    public void setN_basemap(String n_basemap) {
        this.n_basemap = n_basemap;
    }

    public String getP_nid() {
        return p_nid;
    }

    public void setP_nid(String p_nid) {
        this.p_nid = p_nid;
    }

    public String getP_vid() {
        return p_vid;
    }

    public void setP_vid(String p_vid) {
        this.p_vid = p_vid;
    }

    public String getP_enddate() {
        return p_enddate;
    }

    public void setP_enddate(String p_enddate) {
        this.p_enddate = p_enddate;
    }

    public String getVi_receivingcity() {
        return vi_receivingcity;
    }

    public void setVi_receivingcity(String vi_receivingcity) {
        this.vi_receivingcity = vi_receivingcity;
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



    public String getP_amount() {
        return p_amount;
    }

    public void setP_amount(String p_amount) {
        this.p_amount = p_amount;
    }

    public String getP_time() {
        return p_time;
    }

    public void setP_time(String p_time) {
        this.p_time = p_time;
    }

    public String getP_switchstate() {
        return p_switchstate;
    }

    public void setP_switchstate(String p_switchstate) {
        this.p_switchstate = p_switchstate;
    }

    public String getP_months() {
        return p_months;
    }

    public void setP_months(String p_months) {
        this.p_months = p_months;
    }

    public String getP_year() {
        return p_year;
    }

    public void setP_year(String p_year) {
        this.p_year = p_year;
    }
}
