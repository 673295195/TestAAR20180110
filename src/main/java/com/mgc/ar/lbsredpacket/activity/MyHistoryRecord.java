package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.CouponBgView;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.defaultValue;

public class MyHistoryRecord extends AppCompatActivity implements View.OnClickListener {

    private TextView packet;
    private TextView coupon;
    private TextView fragment;
    private ArrayList<BuyerBean> mBuyerBean;
    private Context mContext;
    private CouponBgView detail1;
    private CouponBgView detail3;
    private CouponBgView detail2;
    private int mUserId;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_packet_record);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        bundle = new Bundle();
        intent();
        loadNet();
        initView();

    }

    private void intent() {
        if (getIntent() != null) {
            mUserId = getIntent().getIntExtra("userId", defaultValue);
        }
    }

    private void loadNet() {
        //获取会员信息地址
        String url = Constants.address + "lbsbonustext/member.action";//?userid=" + mUserId;
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid", mUserId);
        OkHttp.post(url, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.error("会员数据" + response);
                Gson gson = new Gson();
                mBuyerBean = new ArrayList<BuyerBean>();
                Type type = new TypeToken<List<BuyerBean>>() {
                }.getType();
                mBuyerBean = gson.fromJson(response, type);
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyHistoryRecord.this, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error(Constants.server_remind + error);
            }
        });
        //判断网络是否有链接
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.all_loading);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBuyerBean == null) {
                        //Toast.makeText(mContext, "服务器繁忙,请稍后再试!", Toast.LENGTH_SHORT).show();
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);
                       /* packet.setText("我的现金红包" + mBuyerBean.get(0).getCashTotal() + "个");
                        coupon.setText("我的优惠券" + mBuyerBean.get(0).getCouponsTotal() + "个");
                        fragment.setText("我的碎片" + mBuyerBean.get(0).getChipTotal() + "个");*/
                    }
                }
            }, Constants.TIME);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        packet = (TextView) findViewById(R.id.my_packet);
        coupon = (TextView) findViewById(R.id.my_coupon);
        fragment = (TextView) findViewById(R.id.my_fragment);
        detail1 = (CouponBgView) findViewById(R.id.packet_detail);
        detail2 = (CouponBgView) findViewById(R.id.coupon_detail);
        detail3 = (CouponBgView) findViewById(R.id.fragment_detail);
/*
        packet.setText("我的现金红包");
        coupon.setText("我的优惠券");
        fragment.setText("我的活动碎片");*/

        detail1.setOnClickListener(this);
        detail2.setOnClickListener(this);
        detail3.setOnClickListener(this);

    }

    public void myCoupon(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (mBuyerBean != null) {
            if (v.getId() == R.id.packet_detail) {
                Intent intent = new Intent(MyHistoryRecord.this, RedPacketRecord.class);
                bundle.putSerializable("mBuyerBean", mBuyerBean.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v.getId() == R.id.coupon_detail) {
                Intent intent = new Intent(MyHistoryRecord.this, CouponRecord.class);
                bundle.putSerializable("mBuyerBean", mBuyerBean.get(0));
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v.getId() == R.id.fragment_detail) {
                Intent intent = new Intent(MyHistoryRecord.this, FragmentRecord.class);
                bundle.putSerializable("mBuyerBean", mBuyerBean.get(0));
                LogUtils.error("id==" + mBuyerBean.get(0).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

    }

}
