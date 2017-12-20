package com.mgc.ar_department.lbs2Dredpacket.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SkyCheng on 2017/9/28.
 */

public class BuyerBean implements Serializable {
    private int id;//会员id
    private String v_name;//会员名称
    private String v_sex;//性别
    private String vl_id;//会员等级
    private String v_phone;//联系电话
    private String v_birthday;//生日
    private String v_address;//地址
    private String v_company;//公司
    private String v_score;//积分
    private String v_status;//状态
    private String v_documents;//证件号
    private String v_balance;//余额
    private String v_Email;//电子邮箱
    private String v_registrationtime;//注册时间
    private String v_lasttime;//上次上线
    private String v_head;//会员头像
    private String v_proposer;//会员推荐人
    private String v_proposercode;//会员推荐码
    private String v_city;//所在城市
    private String v_authenticationstatus;//实名认证状态
    private String v_qrcode;//会员二维码
    private String v_sum;//访问地图次数
    private String v_bonus;//参与活动次数
    private String cashTotal; //红包数
    private String chipTotal; //优惠券
    private String couponsTotal;//碎片
    private List<BuyerPacketBean> mBuyerPacketBeanList;

    public List<BuyerPacketBean> getBuyerPacketBeanList() {
        return mBuyerPacketBeanList;
    }

    public void setBuyerPacketBeanList(List<BuyerPacketBean> buyerPacketBeanList) {
        mBuyerPacketBeanList = buyerPacketBeanList;
    }

    public static class BuyerPacketBean {
        private double longitude;  //经度1
        private double latitude;  //纬度2
        private String money;//红包总大小
        private String num;  //红包数量
        private String language;  //祝福语

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

    public String getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(String cashTotal) {
        this.cashTotal = cashTotal;
    }

    public String getChipTotal() {
        return chipTotal;
    }

    public void setChipTotal(String chipTotal) {
        this.chipTotal = chipTotal;
    }

    public String getCouponsTotal() {
        return couponsTotal;
    }

    public void setCouponsTotal(String couponsTotal) {
        this.couponsTotal = couponsTotal;
    }

    public String getV_proposer() {
        return v_proposer;
    }

    public void setV_proposer(String v_proposer) {
        this.v_proposer = v_proposer;
    }

    public String getV_proposercode() {
        return v_proposercode;
    }

    public void setV_proposercode(String v_proposercode) {
        this.v_proposercode = v_proposercode;
    }

    public String getV_city() {
        return v_city;
    }

    public void setV_city(String v_city) {
        this.v_city = v_city;
    }

    public String getV_authenticationstatus() {
        return v_authenticationstatus;
    }

    public void setV_authenticationstatus(String v_authenticationstatus) {
        this.v_authenticationstatus = v_authenticationstatus;
    }

    public String getV_qrcode() {
        return v_qrcode;
    }

    public void setV_qrcode(String v_qrcode) {
        this.v_qrcode = v_qrcode;
    }

    public String getV_sum() {
        return v_sum;
    }

    public void setV_sum(String v_sum) {
        this.v_sum = v_sum;
    }

    public String getV_bonus() {
        return v_bonus;
    }

    public void setV_bonus(String v_bonus) {
        this.v_bonus = v_bonus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getV_sex() {
        return v_sex;
    }

    public void setV_sex(String v_sex) {
        this.v_sex = v_sex;
    }

    public String getVl_id() {
        return vl_id;
    }

    public void setVl_id(String vl_id) {
        this.vl_id = vl_id;
    }

    public String getV_phone() {
        return v_phone;
    }

    public void setV_phone(String v_phone) {
        this.v_phone = v_phone;
    }

    public String getV_birthday() {
        return v_birthday;
    }

    public void setV_birthday(String v_birthday) {
        this.v_birthday = v_birthday;
    }

    public String getV_address() {
        return v_address;
    }

    public void setV_address(String v_address) {
        this.v_address = v_address;
    }

    public String getV_company() {
        return v_company;
    }

    public void setV_company(String v_company) {
        this.v_company = v_company;
    }

    public String getV_score() {
        return v_score;
    }

    public void setV_score(String v_score) {
        this.v_score = v_score;
    }

    public String getV_status() {
        return v_status;
    }

    public void setV_status(String v_status) {
        this.v_status = v_status;
    }

    public String getV_documents() {
        return v_documents;
    }

    public void setV_documents(String v_documents) {
        this.v_documents = v_documents;
    }

    public String getV_balance() {
        return v_balance;
    }

    public void setV_balance(String v_balance) {
        this.v_balance = v_balance;
    }

    public String getV_Email() {
        return v_Email;
    }

    public void setV_Email(String v_Email) {
        this.v_Email = v_Email;
    }

    public String getV_registrationtime() {
        return v_registrationtime;
    }

    public void setV_registrationtime(String v_registrationtime) {
        this.v_registrationtime = v_registrationtime;
    }

    public String getV_lasttime() {
        return v_lasttime;
    }

    public void setV_lasttime(String v_lasttime) {
        this.v_lasttime = v_lasttime;
    }

    public String getV_head() {
        return v_head;
    }

    public void setV_head(String v_head) {
        this.v_head = v_head;
    }
}


