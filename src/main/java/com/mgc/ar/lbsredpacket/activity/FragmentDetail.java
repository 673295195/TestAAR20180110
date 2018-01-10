package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.SellerFragmentBean;
import com.mgc.ar.lbsredpacket.bean.StateBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.OkHttpUtil;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;
import com.mgc.ar.lbsredpacket.util.SquareImageView;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.defaultValue;


public class FragmentDetail extends AppCompatActivity {

    private Button button;
    private ArrayList<SellerFragmentBean> listFragmentBean;
    private Context mContext;
    private TextView name;
    private ImageView button1;
    private ImageView button2;
    private ImageView button3;
    private ImageView button4;
    private ImageView button5;
    private Button mSuccess;
    private int mUserId;
    private int mPosition;
    private String mSellerId;
    private String mSellerName;
    private String mOneSum;
    private String mTwoSum;
    private String mThreeSum;
    private String mFourSum;
    private String mFivesum;
    private Button mConfirm;
    private Button mConfirmed;
   /* private BadgeViewPro mBv1;
    private BadgeViewPro mBv2;
    private BadgeViewPro mBv3;
    private BadgeViewPro mBv4;
    private BadgeViewPro mBv5;*/
    private TextView useTime;
    private ArrayList<StateBean> stateBean;
    private Gson mGson;
    private Map<String, Object> mParams;
    private int mI;
    private LinearLayout mPart;
    private LinearLayout bottom;
    private TextView mNum1;
    private TextView mNum2;
    private TextView mNum3;
    private TextView mNum4;
    private TextView mNum5;
    private String mOnstate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_detail1);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
       // getScreenSize();
        init();
        initIntent();
        loadNet();
    }

    private void getScreenSize() {
        WindowManager wm = this.getWindowManager();
        float width = wm.getDefaultDisplay().getWidth();
        int wid = OkHttpUtil.px2dip(mContext, width);
        mI = (wid - 12 * 5) / 5;
        LogUtils.error("mi=" + mI + "dp");
    }

    private void init() {
        mPart = (LinearLayout) findViewById(R.id.partrefresh);
        bottom = (LinearLayout) findViewById(R.id.lin_detail);
        mConfirmed = (Button) findViewById(R.id.seller_confirmed);
        mConfirm = (Button) findViewById(R.id.seller_confirm);
        mSuccess = (Button) findViewById(R.id.collect);  //合成按钮
        button = (Button) findViewById(R.id.fragment_detail_close);
        name = (TextView) findViewById(R.id.fragment_detail_name);
        useTime = (TextView) findViewById(R.id.use_time);
        setFragment();
    }

    private void setFragment() {


        //放弃badgeView
        button1 = (SquareImageView) findViewById(R.id.square1);
        button2 = (SquareImageView) findViewById(R.id.square2);
        button3 = (SquareImageView) findViewById(R.id.square3);
        button4 = (SquareImageView) findViewById(R.id.square4);
        button5 = (SquareImageView) findViewById(R.id.square5);
        mNum1 = (TextView) findViewById(R.id.round1);
        mNum2 = (TextView) findViewById(R.id.round2);
        mNum3 = (TextView) findViewById(R.id.round3);
        mNum4 = (TextView) findViewById(R.id.round4);
        mNum5 = (TextView) findViewById(R.id.round5);

    }

    private void initIntent() {
        if (getIntent() != null) {
            mUserId = getIntent().getIntExtra("userId", defaultValue);
            mPosition = getIntent().getIntExtra("position", defaultValue);
            mSellerId = getIntent().getStringExtra("sellerId");
            LogUtils.error("用户id=" + mUserId + ";卖家id=" + mSellerId);
            mSellerName = getIntent().getStringExtra("sellerName");
            LogUtils.error("碎片id==" + mSellerId + "position=" + mPosition);
        }
    }

    private void loadNet() {
        String url = Constants.address + "lbsbonustext/picturepoll.action";
        //?userid=" + mUserId + "&merchantid=" + mSellerId;

        mParams = new HashMap<>();
        mParams.put("userid", mUserId);
        mParams.put("merchantid", mSellerId);
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {
                String string = response;
                LogUtils.error("获得某一个用户一个商家的碎片" + string);
                mGson = new Gson();
                listFragmentBean = new ArrayList<SellerFragmentBean>();
                Type type = new TypeToken<List<SellerFragmentBean>>() {
                }.getType();
                listFragmentBean = mGson.fromJson(string, type);
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.load_fragment_detail);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listFragmentBean == null) {
                        //Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);
                        setData();
                        initData();
                    }
                }
            }, 1000);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        name.setText(mSellerName);
        Glide.with(mContext).load(listFragmentBean.get(0).getVi_oneimg()).into(button1);
        Glide.with(mContext).load(listFragmentBean.get(0).getVi_twoimg()).into(button2);
        Glide.with(mContext).load(listFragmentBean.get(0).getVi_threeimg()).into(button3);
        Glide.with(mContext).load(listFragmentBean.get(0).getVi_fourimg()).into(button4);
        Glide.with(mContext).load(listFragmentBean.get(0).getVi_fiveimg()).into(button5);

    }

    private void initData() {
        mOneSum = listFragmentBean.get(0).getVi_onesum();
        mTwoSum = listFragmentBean.get(0).getVi_twosum();
        mThreeSum = listFragmentBean.get(0).getVi_threesum();
        mFourSum = listFragmentBean.get(0).getVi_foursum();
        mFivesum = listFragmentBean.get(0).getVi_fivesum();
        mOnstate = listFragmentBean.get(0).getSyntheticstate();

        mNum1.setText(mOneSum);
        mNum2.setText(mTwoSum);
        mNum3.setText(mThreeSum);
        mNum4.setText(mFourSum);
        mNum5.setText(mFivesum);
      /*
        mBv1 = new BadgeViewPro(mContext);
        //mBv1.setStrText(mOneSum).setMargin(-7, 8, -5, -5).setTargetView(button1);
        mBv1.setStrText(mOneSum).setMargin(0, 0, 0, 0).setTargetView(button1);
        mBv2 = new BadgeViewPro(mContext);
        //mBv2.setStrText(mTwoSum).setMargin(-7, 8, -5, -5).setTargetView(button2);
        mBv2.setStrText(mTwoSum).setMargin(0, 0, 0, 0).setTargetView(button2);
        mBv3 = new BadgeViewPro(mContext);
        //mBv3.setStrText(mThreeSum).setMargin(-7, 8, -5, -5).setTargetView(button3);
        mBv3.setStrText(mThreeSum).setMargin(0, 0, 0, 0).setTargetView(button3);
        mBv4 = new BadgeViewPro(mContext);
        //mBv4.setStrText(mFourSum).setMargin(-7, 8, -5, -5).setTargetView(button4);
        mBv4.setStrText(mFourSum).setMargin(0, 0, 0, 0).setTargetView(button4);
        mBv5 = new BadgeViewPro(mContext);
        //mBv5.setStrText(mFivesum).setMargin(-7, 8, -5, -5).setTargetView(button5);
        mBv5.setStrText(mFivesum).setMargin(0, 0, 0, 0).setTargetView(button5);*/

        if (!mOneSum.equals("0") && !mTwoSum.equals("0") && !mThreeSum.equals("0")
                && !mFourSum.equals("0") && !mFivesum.equals("0")&&mOnstate.equals("0") ) {//均不为0且状态为0,未合成
            mSuccess.setVisibility(View.VISIBLE);
        } else if (mOnstate.equals("1")) { //合成1次,显示确认按钮
            mConfirm.setVisibility(View.GONE);
            mConfirmed.setVisibility(View.VISIBLE);
        } else if (mOnstate.equals("2")) {//显示领取时间,确认按钮已点
            useTime.setVisibility(View.VISIBLE);
            useTime.setText("使用时间:"+listFragmentBean.get(0).getVi_collectiontime());
        }
    }

    public void fragmentDetailClose(View view) {
        finish();
    }

    public void sellerConfirm(View view) {//2店家确认 获取当前时间并发给服务器

        String url = Constants.address + "lbsbonustext/receive.action";
        //?userid=" + mUserId + "&merchantid=" + mSellerId;  //传入店家id和userId+时间

    /*    Map<String, Object> params = new HashMap<>();
        params.put("userid", mUserId);
        params.put("merchantid", mSellerId);*/
        NetworkInfo networkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (networkInfo != null) {
            OkHttp.post(url, mParams, new OkCallback() {
                @Override
                public void onResponse(String response) {
                    String json = response.toString();
                    LogUtils.error("返红包 " + json);
                    ArrayList<StateBean> stateBean = new ArrayList<StateBean>();
                    Type type = new TypeToken<List<StateBean>>() {
                    }.getType();
                    stateBean = mGson.fromJson(json, type);
                    String state = stateBean.get(0).getState();
                    LogUtils.error("state" + state);
                    if (state.equals("1")) {  //1是使用成功

                        String data = "yyyy年MM月dd日 HH:mm ";
                        //String data = "yyyy年MM月dd日 HH:mm:ss ";
                        SimpleDateFormat formatter = new SimpleDateFormat(data);
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        final String str = formatter.format(curDate);
                        mConfirm.setVisibility(View.VISIBLE); //灰色按钮
                        mConfirmed.setVisibility(View.GONE);  //店家确认按钮
                        useTime.setVisibility(View.VISIBLE);
                        useTime.setText("使用时间:" + str);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, Constants.use_success, Toast.LENGTH_SHORT).show();
                            }
                        });

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

                }
            });
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }

    }

    private void reStartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void buyerConfirm(View view) {//1用户合成  每个自动减1,发送请求告诉服务器,传状态1
        String url = Constants.address + "lbsbonustext/debris.action";
        //?userid=" + mUserId + "&merchantid=" + mSellerId;  //传入店家id和userId

     /*   Map<String, Object> params = new HashMap<>();
        params.put("userid", mUserId);
        params.put("merchantid", mSellerId);*/
        NetworkInfo networkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (networkInfo != null) {
            OkHttp.post(url, mParams, new OkCallback() {
                @Override
                public void onResponse(String response) {
                    String json = response.toString();
                    LogUtils.error("所" + json);
                    //Gson gson = new Gson();
                    stateBean = new ArrayList<StateBean>();
                    Type type = new TypeToken<List<StateBean>>() {
                    }.getType();
                    // StateBean stateBean=new StateBean();
                    stateBean = mGson.fromJson(json, type);
                    String state = stateBean.get(0).getState();
                    LogUtils.error("state" + state);
                    if (state.equals("1")) {  //1是成功,数量各-1
                        final int one = Integer.valueOf(mOneSum) - 1;
                        final int two = Integer.valueOf(mTwoSum) - 1;
                        final int three = Integer.valueOf(mThreeSum) - 1;
                        final int four = Integer.valueOf(mFourSum) - 1;
                        final int five = Integer.valueOf(mFivesum) - 1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSuccess.setVisibility(View.GONE);  //合成按钮
                                mConfirm.setVisibility(View.GONE); //灰色按钮
                                mConfirmed.setVisibility(View.VISIBLE);  //店家确认按钮

                                mNum1.setText(one+"");
                                mNum2.setText(two+"");
                                mNum3.setText(three+"");
                                mNum4.setText(four+"");
                                mNum5.setText(five+"");

                                Toast.makeText(mContext, Constants.collect_success, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {  //失败 不-1
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, Constants.collect_fail, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                @Override
                public void onFailure(String error) {
                   /* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, Constants.collect_fail, Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    LogUtils.error("合成异常: " + error);
                }
            });
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }
}


