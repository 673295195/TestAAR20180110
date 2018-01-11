package com.mgc.ar.lbsredpacket.bean;

import java.io.Serializable;

/**
 * Created by SkyCheng on 2017/10/12.
 */

public class SellerFragmentBean implements Serializable {


    private int id;//红包商家id
    private String n_shops; //商家名字
    private String vi_nid;//商铺自己  //新修改的商铺nid
    private String vi_vid;//vip名称	//新修改的会员vid
    private String vi_time;//日期
    private String vi_year;//年
    private String vi_months;//月
    private String vi_chipcol;//碎片
    private String vi_switchstate;//状态
    private String p_receivingcity;//红包领取城市
    private String n_picture;//头像
    private String vi_onstate;//状态0未合成,1合成,2已合成
    private String vi_oneimg;//1号图片
    private String vi_twoimg;//2号图片
    private String vi_threeimg;//3号图片
    private String vi_fourimg;//4号图片
    private String vi_fiveimg;//5号图片
    private String vi_onesum;//1号总数
    private String vi_twosum;//2号总数
    private String vi_threesum;//3号总数
    private String vi_foursum;//4号总数
    private String vi_fivesum;//5号总数
    private String syntheticstate; //合成状态
    private String vi_collectiontime; //使用时间

    public String getVi_collectiontime() {
        return vi_collectiontime;
    }

    public void setVi_collectiontime(String vi_collectiontime) {
        this.vi_collectiontime = vi_collectiontime;
    }

    public String getSyntheticstate() {
        return syntheticstate;
    }

    public void setSyntheticstate(String syntheticstate) {
        this.syntheticstate = syntheticstate;
    }

    public String getVi_onstate() {
        return vi_onstate;
    }

    public void setVi_onstate(String vi_onstate) {
        this.vi_onstate = vi_onstate;
    }

    public String getVi_oneimg() {
        return vi_oneimg;
    }

    public void setVi_oneimg(String vi_oneimg) {
        this.vi_oneimg = vi_oneimg;
    }

    public String getVi_twoimg() {
        return vi_twoimg;
    }

    public void setVi_twoimg(String vi_twoimg) {
        this.vi_twoimg = vi_twoimg;
    }

    public String getVi_threeimg() {
        return vi_threeimg;
    }

    public void setVi_threeimg(String vi_threeimg) {
        this.vi_threeimg = vi_threeimg;
    }

    public String getVi_fourimg() {
        return vi_fourimg;
    }

    public void setVi_fourimg(String vi_fourimg) {
        this.vi_fourimg = vi_fourimg;
    }

    public String getVi_fiveimg() {
        return vi_fiveimg;
    }

    public void setVi_fiveimg(String vi_fiveimg) {
        this.vi_fiveimg = vi_fiveimg;
    }

    public String getVi_onesum() {
        return vi_onesum;
    }

    public void setVi_onesum(String vi_onesum) {
        this.vi_onesum = vi_onesum;
    }

    public String getVi_twosum() {
        return vi_twosum;
    }

    public void setVi_twosum(String vi_twosum) {
        this.vi_twosum = vi_twosum;
    }

    public String getVi_threesum() {
        return vi_threesum;
    }

    public void setVi_threesum(String vi_threesum) {
        this.vi_threesum = vi_threesum;
    }

    public String getVi_foursum() {
        return vi_foursum;
    }

    public void setVi_foursum(String vi_foursum) {
        this.vi_foursum = vi_foursum;
    }

    public String getVi_fivesum() {
        return vi_fivesum;
    }

    public void setVi_fivesum(String vi_fivesum) {
        this.vi_fivesum = vi_fivesum;
    }

    public String getN_picture() {
        return n_picture;
    }

    public void setN_picture(String n_picture) {
        this.n_picture = n_picture;
    }

    public String getVi_nid() {
        return vi_nid;
    }

    public void setVi_nid(String vi_nid) {
        this.vi_nid = vi_nid;
    }

    public String getVi_vid() {
        return vi_vid;
    }

    public void setVi_vid(String vi_vid) {
        this.vi_vid = vi_vid;
    }

    public String getP_receivingcity() {
        return p_receivingcity;
    }

    public void setP_receivingcity(String p_receivingcity) {
        this.p_receivingcity = p_receivingcity;
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


    public String getVi_time() {
        return vi_time;
    }

    public void setVi_time(String vi_time) {
        this.vi_time = vi_time;
    }

    public String getVi_year() {
        return vi_year;
    }

    public void setVi_year(String vi_year) {
        this.vi_year = vi_year;
    }

    public String getVi_months() {
        return vi_months;
    }

    public void setVi_months(String vi_months) {
        this.vi_months = vi_months;
    }

    public String getVi_chipcol() {
        return vi_chipcol;
    }

    public void setVi_chipcol(String vi_chipcol) {
        this.vi_chipcol = vi_chipcol;
    }

    public String getVi_switchstate() {
        return vi_switchstate;
    }

    public void setVi_switchstate(String vi_switchstate) {
        this.vi_switchstate = vi_switchstate;
    }
}