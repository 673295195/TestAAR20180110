package com.mgc.ar_department.lbs_redpacket.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar_department.lbs_redpacket.bean.BuyerBean;
import com.mgc.ar_department.lbs_redpacket.bean.SellerBean;
import com.mgc.ar_department.lbs_redpacket.bean.SellerCouponBean;
import com.mgc.ar_department.lbs_redpacket.bean.SellerFragmentBean;
import com.mgc.ar_department.lbs_redpacket.bean.SellerPacketBean;
import com.mgc.ar_department.lbs_redpacket.view.LocationAndPacket;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SkyCheng on 2017/9/30.
 */

public class OkHttpUtil {

    private OkHttpClient mOkHttpClient;
    private static final String TAG = "OkHttpUtil";
    private LocationAndPacket mLocationAndPacket;
    private PostToServer mPostToServer = new PostToServer();
    private int mId;
    private String mStringvip;
    private String mV_name;
    private String mV_amount;
    private ArrayList<BuyerBean> buyreList;

    public OkHttpUtil(LocationAndPacket locationAndPacket) {
        mLocationAndPacket = locationAndPacket;
    }

    public void getSellerBean() {
        //真机
        String url = Constants.address+"lbsbonustext/shopm.action";
        mOkHttpClient = new OkHttpClient();
        //url可能不同
        Request mRequest = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLocationAndPacket.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mLocationAndPacket, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error("1次请求服务器异常: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();

                LogUtils.error("所有商家数据" + string);
                Gson gson = new Gson();

                ArrayList<SellerBean> list = new ArrayList<SellerBean>();
                Type type = new TypeToken<List<SellerBean>>() {
                }.getType();
                list = gson.fromJson(string, type);
                LogUtils.error("商家个数 " + list.size());
                mLocationAndPacket.setData(list);
            }
        });
    }

    public void getPacketMoney(int pid, final String n_redstate) {

        //发送商家和消费者ID,获取红包
        LogUtils.error("传入的状态" + n_redstate);
        String url = Constants.address+"lbsbonustext/redhbaoqq.action?pid=" + pid + "&vid=" + mId;
        mOkHttpClient = new OkHttpClient();
        //url可能不同
        Request mRequest = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLocationAndPacket.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mLocationAndPacket, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error("2次请求异常: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtils.error("返回商家红包 " + string);
                Gson gson = new Gson();
                if (n_redstate.equals("0")) {  //现金红包
                    ArrayList<SellerPacketBean> list = new ArrayList<SellerPacketBean>();
                    Type type = new TypeToken<List<SellerPacketBean>>() {
                    }.getType();
                    list = gson.fromJson(string, type);
                    mLocationAndPacket.getPacketData(list.get(0));
                } else if (n_redstate.equals("1")) { //优惠券
                    ArrayList<SellerCouponBean> list = new ArrayList<SellerCouponBean>();
                    Type type = new TypeToken<List<SellerCouponBean>>() {
                    }.getType();
                    list = gson.fromJson(string, type);
                    mLocationAndPacket.getCouponData(list.get(0));
                } else {                             //活动碎片
                    ArrayList<SellerFragmentBean> list = new ArrayList<SellerFragmentBean>();
                    Type type = new TypeToken<List<SellerFragmentBean>>() {
                    }.getType();
                    list = gson.fromJson(string, type);

                    mLocationAndPacket.getFragmentData(list.get(0));
                }
            }
        });
    }

    public void getBuyerBean() {
        //获取会员信息地址
        String url = Constants.address+"lbsbonustext/member.action";
        mOkHttpClient = new OkHttpClient();
        //url可能不同
        Request mRequest = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                mLocationAndPacket.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mLocationAndPacket, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error("1次请求服务器异常: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mStringvip = response.body().string();

                LogUtils.error("获得会员" + mStringvip);
                Gson gson = new Gson();

                buyreList = new ArrayList<BuyerBean>();
                Type type = new TypeToken<List<BuyerBean>>() {
                }.getType();
                buyreList = gson.fromJson(mStringvip, type);
                mId = buyreList.get(0).getId();
                mLocationAndPacket.setBuyerData(buyreList);
                //返回会员信息
                //mPostToServer.loginByPost(mStringvip);

            }
        });
    }
}