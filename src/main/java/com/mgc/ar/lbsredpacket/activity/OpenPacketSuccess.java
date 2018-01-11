package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerBean;
import com.mgc.ar.lbsredpacket.bean.SellerPacketBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by SkyCheng on 2017/9/25.
 */

public class OpenPacketSuccess extends AppCompatActivity {

    private ImageButton mHeadImage;
    private TextView mSellerName;
    private TextView mMoney;
    private TextView mRecord;
    private ArrayList<SellerPacketBean> sellerPacketBean;
    private Context mContext;
    private SellerBean mSellerBean;
    private BuyerBean mBuyerBean;
    private SellerPacketBean mPacketBean;
    private DecimalFormat mDf = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_packet_success);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        sellerPacketBean = new ArrayList<>();
        initView();
        //获取传递数据
        initIntent();
        //显示数据
        initData();

    }
    private void initData() {
        Double aDouble = Double.valueOf(mPacketBean.getCa_amount());
        String format = mDf.format(aDouble);

        mMoney.setText(format + "元");
        mSellerName.setText(mSellerBean.getN_shops());
        Glide.with(this).load(mSellerBean.getN_picture()).into(mHeadImage);
    }

    private void initIntent() {
        if (getIntent() != null) {
            mSellerBean = (SellerBean) getIntent().getSerializableExtra("sellerBean");
            mPacketBean = (SellerPacketBean) getIntent().getSerializableExtra("mPacketBean");
            mBuyerBean = (BuyerBean) getIntent().getSerializableExtra("mBuyerBean");
        }
    }

    private void initView() {
        mHeadImage = (ImageButton) findViewById(R.id.head_img_packet);
        mSellerName = (TextView) findViewById(R.id.name_packet);
        mMoney = (TextView) findViewById(R.id.money_packet);
        mRecord = (TextView) findViewById(R.id.record_packet);

        mRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
                if (activeNetworkInfo != null) {
                    //获取消费者红包记录,传入会员ID,与哪种红包,消费者名字
                    Intent intent = new Intent(OpenPacketSuccess.this, RedPacketRecord.class);
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
    //关闭
    public void packet(View view) {
        finish();
    }
}
