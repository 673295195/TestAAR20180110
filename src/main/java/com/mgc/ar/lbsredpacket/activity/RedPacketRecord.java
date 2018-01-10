package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerPacketBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RedPacketRecord extends AppCompatActivity {

    private RecyclerView mRecycle;
    private Context mContext;
    private RecycleAdapter recycleAdapter;
    private ArrayList<SellerPacketBean> packetBean;
    private double j = 0;
    private BuyerBean mBuyerBean;
    private int mId;
    private DecimalFormat mDf = new DecimalFormat("0.00");
    private String mFormat;
    private HashMap<String, Object> mParams;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_record);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        initView();
        setInfo();
        loadNet();
    }

    private void setIntent() {
        for (int i = 0; i < packetBean.size(); i++) {
            j += Double.valueOf(packetBean.get(i).getCa_amount());
        }
        mFormat = mDf.format(j);
    }

    private void loadNet() {//加载红包记录数据
        mId = mBuyerBean.getId();
        String url = Constants.address + "lbsbonustext/keep.action";
        //?vname=" + mId + "&state=0";
        mParams = new HashMap<>();
        mParams.put("vname", mId);
        mParams.put("state", 0);
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {
                mGson = new Gson();
                packetBean = new ArrayList<SellerPacketBean>();
                Type type = new TypeToken<List<SellerPacketBean>>() {
                }.getType();
                packetBean = mGson.fromJson(response, type);
            }

            @Override
            public void onFailure(String error) {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.load_packet);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (packetBean == null) {
                        //Toast.makeText(mContext, "服务器繁忙,请稍后再试!", Toast.LENGTH_SHORT).show();
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);
                        LogUtils.error("红包记录" + packetBean.size());
                        setIntent();
                        setData();

                    }
                }
            }, 1000);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
     /*   buyerName = (TextView) findViewById(R.id.name_packet_record);
        totalMoney = (TextView) findViewById(R.id.total_packet);
        headImage = (ImageButton) findViewById(R.id.head_img_packet_recored);*/
        mRecycle = (RecyclerView) findViewById(R.id.recycle_packet);
    }

    private void setInfo() {

        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            //mUserId = getIntent().getIntExtra("userId", defaultValue);
            //packetBean = (ArrayList<SellerPacketBean>) intent.getSerializableExtra("listPacketBean");
            mBuyerBean = (BuyerBean) intent.getSerializableExtra("mBuyerBean");
        }
    }

    private void setData() {

        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        recycleAdapter = new RecycleAdapter(mRecycle);
        mRecycle.setAdapter(recycleAdapter);
    }

    public void packetClose(View view) {
        finish();
    }

    private class RecycleAdapter extends RecyclerView.Adapter {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_NORMAL = 1;

        private RecycleHolder mRecycleHolder;
        private final View mHeaderView;

        public RecycleAdapter(RecyclerView mRecycle) {
            mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.header_item, mRecycle, false);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_HEADER == viewType) {
                return new RecycleHolder(mHeaderView);
            } else {
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_packet_record, parent, false);
                mRecycleHolder = new RecycleHolder(inflate);
                return mRecycleHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mRecycleHolder = (RecycleHolder) holder;
            if (position == 0) {
                if (packetBean != null) {
                    Glide.with(mContext).load(mBuyerBean.getV_head()).into(mRecycleHolder.headImage);
                    mRecycleHolder.headName.setText(mBuyerBean.getV_name());
                    mRecycleHolder.headNumber.setText("您总共领取" + mFormat + "元");
                } else {
                    //totalMoney.setText("您的钱包空空如也!");
                    Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                }
            } else {
                mRecycleHolder.setBindData(packetBean.get(position - 1), mDf);
                Glide.with(mContext).load(packetBean.get(position - 1).getN_picture()).into(mRecycleHolder.image);
            }
        }

        @Override
        public int getItemCount() {
            if (packetBean != null) {
                return packetBean.size() + 1;
            } else return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }
    }

    private static class RecycleHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mMoney;
        private final TextView mTime;
        private final ImageView image;
        private TextView headName;
        private TextView headNumber;
        private ImageButton headImage;

        public RecycleHolder(View view) {
            super(view);
            //position==0
            headImage = (ImageButton) view.findViewById(R.id.coupon_detailimg);
            headName = (TextView) view.findViewById(R.id.coupon_detailname);
            headNumber = (TextView) view.findViewById(R.id.coupon_detailnumber);
            //position==1
            mName = (TextView) view.findViewById(R.id.name_send_packet);
            mMoney = (TextView) view.findViewById(R.id.received_packet);
            mTime = (TextView) view.findViewById(R.id.time_packet);
            image = (ImageView) view.findViewById(R.id.image_sellerHead_packet);
        }

        public void setBindData(SellerPacketBean packetBean, DecimalFormat df) {
            Double aDouble = Double.valueOf(packetBean.getCa_amount());
            String format = df.format(aDouble);
            mName.setText(packetBean.getN_shops());
            mTime.setText(packetBean.getCa_time());
            mMoney.setText(format + "元");
        }
    }

}
