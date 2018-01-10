package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.SellerCouponBean;
import com.mgc.ar.lbsredpacket.bean.StateBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.defaultValue;
import static com.amap.test2Dlibrary.R.id.close_detail;

public class CouponDetail extends AppCompatActivity implements View.OnClickListener {


    private TextView mDeadTime;
    private TextView mCondition;
    private TextView mName;
    private Button mUse;
    private Button mClose;
    private Context mContext;
    private ArrayList<SellerCouponBean> mCouponBeen;
    private int mPosition;
    private int mUserId;
    private int mSellerId;
    private Button mUsed;
    private ArrayList<StateBean> stateBean;
    private TextView mRegular;
    private ImageView mImageView;
    private TextView mDiscount;
    private HashMap<String, Object> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        intent();
        loadNet();  //加载优惠券详情
        initView();
    }

    private void intent() {
        if (getIntent() != null) {
            mSellerId = getIntent().getIntExtra("sellerId", defaultValue);
            mPosition = getIntent().getIntExtra("position", defaultValue);
            mUserId = getIntent().getIntExtra("userId", defaultValue);
            LogUtils.error("传入的优惠券id=" + mSellerId);
        }
    }

    private void initView() {
        mClose = (Button) findViewById(close_detail);
        mUse = (Button) findViewById(R.id.use_detail);
        mUsed = (Button) findViewById(R.id.used_detail);
        mName = (TextView) findViewById(R.id.coupon_detailname);
        mCondition = (TextView) findViewById(R.id.name1_detail);
        mDeadTime = (TextView) findViewById(R.id.deadTime_detail);
        mDiscount = (TextView) findViewById(R.id.coupon_detailnumber);
        mRegular = (TextView) findViewById(R.id.regular);
        mImageView = (ImageView) findViewById(R.id.coupon_detailimg);
        mClose.setOnClickListener(this);
        mUse.setOnClickListener(this);
    }

    private void loadNet() {
        //获取会员信息地址
        String url = Constants.address + "lbsbonustext/lbsdetailed.action";
        //?bonusid=" + mSellerId;  //传入优惠券id,   店家id和userId
        mParams = new HashMap<>();
        //mParams.put("userid", mUserId);
        mParams.put("bonusid", mSellerId);
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {

                LogUtils.error("会员数据" + response);
                Gson gson = new Gson();
                mCouponBeen = new ArrayList<SellerCouponBean>();
                Type type = new TypeToken<List<SellerCouponBean>>() {
                }.getType();
                mCouponBeen = gson.fromJson(response, type);
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error(Constants.server_remind + error);
            }
        });
        //判断网络是否有链接
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.all_detail_loading);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mCouponBeen == null) {
                        Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);
                        setData();
                    }
                }
            }, Constants.TIME);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        //设置数据
        Glide.with(mContext).load(mCouponBeen.get(0).getN_basemap()).into(mImageView);
        mName.setText(mCouponBeen.get(0).getN_shops());
        mRegular.setText(mCouponBeen.get(0).getN_regular());
        mDeadTime.setText(mCouponBeen.get(0).getP_enddate());
        mDiscount.setText(mCouponBeen.get(0).getP_amount());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_detail) {
            finish();
        } else if (v.getId() == R.id.use_detail) {  //使用优惠券,发送商家id, 券id.服务器接收到返回使用成功
            mUse.setVisibility(View.GONE);
            mUsed.setVisibility(View.VISIBLE);
            NetworkInfo networkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
            if (networkInfo != null) {
                String url = Constants.address + "lbsbonustext/Usecoupons.action";
                //?bonusid=" + mSellerId;  //传入商家id,优惠券和userId
                OkHttp.post(url, mParams, new OkCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.error("所有=" + response);
                        Gson gson = new Gson();
                        stateBean = new ArrayList<StateBean>();
                        Type type = new TypeToken<List<StateBean>>() {
                        }.getType();
                        stateBean = gson.fromJson(response, type);
                        String state = stateBean.get(0).getState();
                        LogUtils.error("state" + state);
                        if (state.equals("1")) {  //1,使用成功
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, Constants.use_success, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //finish();

                        } else {  //失败
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, Constants.use_fail, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(mContext, Constants.use_fail, Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                });

            } else {
                Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
