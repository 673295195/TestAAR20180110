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
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerBean;
import com.mgc.ar.lbsredpacket.bean.SellerCouponBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;

public class OpenCouponSuccess extends AppCompatActivity {

    private TextView search;
    private SellerBean mSellerBean;
    private BuyerBean mBuyerBean;
    private SellerCouponBean mCouponBean;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_coupon_success);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        initIntent();
        init();
        initData();
    }
    private void init() {
        TextView name = (TextView) findViewById(R.id.name_coupon);
        TextView overTime = (TextView) findViewById(R.id.limittime_coupon);
        TextView number = (TextView) findViewById(R.id.coupon);
        ImageButton headImage = (ImageButton) findViewById(R.id.head_img_coupon);
        TextView data = (TextView) findViewById(R.id.gettime_coupon);

        search = (TextView) findViewById(R.id.search_coupon);

        Glide.with(mContext).load(mSellerBean.getN_picture()).into(headImage);
        name.setText(mSellerBean.getN_shops());
        number.setText(mCouponBean.getP_amount());
        data.setText("领取时间:" + mCouponBean.getP_time());
        overTime.setText("有效日期:" + mCouponBean.getP_enddate());
    }

    private void initIntent() {
        if (getIntent() != null) {
            mSellerBean = (SellerBean) getIntent().getSerializableExtra("sellerBean");
            mCouponBean = (SellerCouponBean) getIntent().getSerializableExtra("mCouponBean");
            mBuyerBean = (BuyerBean) getIntent().getSerializableExtra("mBuyerBean");
        }
    }

    private void initData() {


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
                if (activeNetworkInfo != null) {
                    Intent intent = new Intent(OpenCouponSuccess.this, CouponRecord.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mBuyerBean", mBuyerBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void coupon(View view) {
        finish();
    }
}
