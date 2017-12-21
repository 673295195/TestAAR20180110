package com.mgc.ar_department.lbs2Dredpacket.view;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar_department.lbs2Dredpacket.bean.CouponDetailBean;
import com.mgc.ar_department.lbs2Dredpacket.util.Constants;
import com.mgc.ar_department.lbs2Dredpacket.util.LogUtils;
import com.mgc.ar_department.lbs2Dredpacket.util.PermissionAndNetUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.amap.test2Dlibrary.R.id.close_detail;

public class CouponDetail extends AppCompatActivity implements View.OnClickListener {


    private TextView mDeadTime;
    private TextView mCondition;
    private TextView mName;
    private Button mUse;
    private Button mClose;
    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private String mStringDetail;
    private ArrayList<CouponDetailBean> mBmCouponDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        ButterKnife.bind(this);
        mContext = this;
        //loadNet();
        initView();
    }

    private void initView() {
        mClose = (Button) findViewById(close_detail);
        mUse = (Button) findViewById(R.id.use_detail);
        mName = (TextView) findViewById(R.id.name_detail);
        mCondition = (TextView) findViewById(R.id.name1_detail);
        mDeadTime = (TextView) findViewById(R.id.deadTime_detail);
        //TextView name= (TextView) findViewById(R.id.name1_detail);

        mClose.setOnClickListener(this);
        mUse.setOnClickListener(this);
    }

    private void loadNet() {
        //获取会员信息地址
        String url = Constants.address + "lbsbonustext/member.action";  //传入优惠券信息
        mOkHttpClient = new OkHttpClient();
        //url可能不同
        Request mRequest = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyRedPacketRecord.this, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtils.error(Constants.server_remind + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mStringDetail = response.body().string();

                LogUtils.error("会员数据" + mStringDetail);
                Gson gson = new Gson();

                mBmCouponDetailBean = new ArrayList<CouponDetailBean>();
                Type type = new TypeToken<List<CouponDetailBean>>() {
                }.getType();
                mBmCouponDetailBean = gson.fromJson(mStringDetail, type);
            }
        });

        //判断网络是否有链接
  /*      ConnectivityManager connect = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connect.getActiveNetworkInfo();*/
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.loading);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBmCouponDetailBean == null) {
                        //Toast.makeText(mContext, "服务器繁忙,请稍后再试!", Toast.LENGTH_SHORT).show();
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);

                    }
                }
            }, 2000);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_detail) {
            finish();
        }else if (v.getId()== R.id.use_detail){  //发送商家id, 券id.服务器接收到返回使用成功
            Toast.makeText(mContext,"使用成功",Toast.LENGTH_SHORT).show();
        }
    }
}
