package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerBean;
import com.mgc.ar.lbsredpacket.bean.SellerCouponBean;
import com.mgc.ar.lbsredpacket.bean.SellerFragmentBean;
import com.mgc.ar.lbsredpacket.bean.SellerPacketBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;

public class OpenFail extends AppCompatActivity {

    private TextView search;

    private OkHttpClient mOkHttpClient;
    private String state;
    private ArrayList<SellerPacketBean> listPacketBean;
    private ArrayList<SellerCouponBean> listCouponBean;
    private ArrayList<SellerFragmentBean> listFragmentBean;
    private Context mContext;
    private BuyerBean mBuyerBean;
    private SellerBean mSellerBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_fail);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        initIntent();
        setData();
        intNet();
        intentToRecord();
    }

    private void intentToRecord() {

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
                //判断是那种红包
                if (activeNetworkInfo != null) {
                    if (listPacketBean != null && state.equals("0")) {
                        Intent intent = new Intent(OpenFail.this, RedPacketRecord.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listPacketBean", listPacketBean);
                        bundle.putSerializable("mBuyerBean", mBuyerBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (listCouponBean != null && state.equals("1")) {
                        Intent intent = new Intent(OpenFail.this, CouponRecord.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listCouponBean", listCouponBean);
                        bundle.putSerializable("mBuyerBean", mBuyerBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (listFragmentBean != null && state.equals("2")) {
                        Intent intent = new Intent(OpenFail.this, FragmentRecord.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listFragmentBean", listFragmentBean);
                        bundle.putSerializable("mBuyerBean", mBuyerBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        intNet();
                        Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void intNet() {
        int id = mBuyerBean.getId();
        state = mSellerBean.getN_redstate();
        String url = Constants.address + "lbsbonustext/keep.action"; //?vname=" + mId + "&state=" + state;
        HashMap<String, Object> params = new HashMap<>();
        params.put("vname", id);
        params.put("state", state);
        OkHttp.post(url, params, new OkCallback() {
            @Override
            public void onResponse(String response) {
                //String string = response.body().string();
                LogUtils.error("红包记录= " + response);
                Gson gson = new Gson();
                if (state.equals("0")) {
                    listPacketBean = new ArrayList<SellerPacketBean>();
                    Type type = new TypeToken<List<SellerPacketBean>>() {
                    }.getType();
                    listPacketBean = gson.fromJson(response, type);
                } else if (state.equals("1")) {
                    listCouponBean = new ArrayList<SellerCouponBean>();
                    Type type = new TypeToken<List<SellerCouponBean>>() {
                    }.getType();
                    listCouponBean = gson.fromJson(response, type);
                } else {
                    listFragmentBean = new ArrayList<SellerFragmentBean>();
                    Type type = new TypeToken<List<SellerFragmentBean>>() {
                    }.getType();
                    listFragmentBean = gson.fromJson(response, type);
                }
            }
            @Override
            public void onFailure(String error) {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OpenFail.this, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
    }
    private void setData() {
        TextView nameFail = (TextView) findViewById(R.id.name_fail);
        ImageButton headImage = (ImageButton) findViewById(R.id.head_img_fail);
        search = (TextView) findViewById(R.id.fail_search);
        nameFail.setText(mSellerBean.getN_shops());
        Glide.with(mContext).load(mSellerBean.getN_picture()).into(headImage);

    }
    private void initIntent() {
        if (getIntent() != null) {
            mSellerBean = (SellerBean) getIntent().getSerializableExtra("sellerBean");
            mBuyerBean = (BuyerBean) getIntent().getSerializableExtra("mBuyerBean");
        }
    }

    public void failClose(View view) {
        finish();
    }
}

