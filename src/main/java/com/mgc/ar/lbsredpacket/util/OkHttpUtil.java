package com.mgc.ar.lbsredpacket.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerBean;
import com.mgc.ar.lbsredpacket.bean.SellerCouponBean;
import com.mgc.ar.lbsredpacket.bean.SellerFragmentBean;
import com.mgc.ar.lbsredpacket.bean.SellerPacketBean;
import com.mgc.ar.lbsredpacket.activity.LBSAndPacketActivity;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SkyCheng on 2017/9/30.
 */

public class OkHttpUtil {

    private  final String TAG = "OkHttpUtil";
    private LBSAndPacketActivity mLocationAndPacket;
    private int mId;
    private ArrayList<BuyerBean> buyreList;

    public OkHttpUtil(LBSAndPacketActivity locationAndPacket) {
        mLocationAndPacket = locationAndPacket;
    }

    public void getSellerBean() {
        //真机
        String url = Constants.address + "lbsbonustext/shopm.action";
        HashMap<String,Object> mParams = new HashMap<>();
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.error("所有商家数据" + response);
                Gson gson = new Gson();
                ArrayList<SellerBean> list = new ArrayList<SellerBean>();
                Type type = new TypeToken<List<SellerBean>>() {
                }.getType();
                list = gson.fromJson(response, type);
                LogUtils.error("商家个数 " + list.size());
                mLocationAndPacket.setData(list);
            }

            @Override
            public void onFailure(String error) {
                mLocationAndPacket.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mLocationAndPacket, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error("1次请求服务器异常: " + error);
            }
        });
    }

    public void getPacketMoney(int pid, final String n_redstate, int userId) {

        //发送商家和消费者ID,获取红包
        LogUtils.error("传入的状态" + n_redstate);
        String url = Constants.address + "lbsbonustext/redhbaoqq.action";//?pid=" + pid + "&vid=" + userId;
        HashMap<String,Object> mParams = new HashMap<>();
        mParams.put("pid", pid);
        mParams.put("vid", userId);
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.error("返回商家红包 " + response);
                Gson gson = new Gson();
                if (n_redstate.equals("0")) {  //现金红包
                    ArrayList<SellerPacketBean> list = new ArrayList<SellerPacketBean>();
                    Type type = new TypeToken<List<SellerPacketBean>>() {
                    }.getType();
                    list = gson.fromJson(response, type);
                    mLocationAndPacket.getPacketData(list.get(0));
                } else if (n_redstate.equals("1")) { //优惠券
                    ArrayList<SellerCouponBean> list = new ArrayList<SellerCouponBean>();
                    Type type = new TypeToken<List<SellerCouponBean>>() {
                    }.getType();
                    list = gson.fromJson(response, type);
                    mLocationAndPacket.getCouponData(list.get(0));
                } else {                             //活动碎片
                    ArrayList<SellerFragmentBean> list = new ArrayList<SellerFragmentBean>();
                    Type type = new TypeToken<List<SellerFragmentBean>>() {
                    }.getType();
                    list = gson.fromJson(response, type);

                    mLocationAndPacket.getFragmentData(list.get(0));
                }
            }

            @Override
            public void onFailure(String error) {
                mLocationAndPacket.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mLocationAndPacket, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error("2次请求异常: " + error);
            }
        });

       /* mOkHttpClient = new OkHttpClient();
        //url可能不同
        Request mRequest = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });*/
    }

    public void getBuyerBean(int id) {
        //获取会员信息地址
        String url = Constants.address + "lbsbonustext/member.action";//?userid=" + id;
        HashMap<String,Object> mParams = new HashMap<>();
        mParams.put("userid", id);
        //mParams.put("state", 1);
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {
                //mStringvip = response.body().string();

                LogUtils.error("获得会员" + response);
                Gson gson = new Gson();

                buyreList = new ArrayList<BuyerBean>();
                Type type = new TypeToken<List<BuyerBean>>() {
                }.getType();
                buyreList = gson.fromJson(response, type);
                mId = buyreList.get(0).getId();
                mLocationAndPacket.setBuyerData(buyreList);
            }

            @Override
            public void onFailure(String error) {
                mLocationAndPacket.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(mLocationAndPacket, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error("1次请求服务器异常: " + error);
            }
        });
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}