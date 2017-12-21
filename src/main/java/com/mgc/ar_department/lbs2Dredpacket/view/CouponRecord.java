package com.mgc.ar_department.lbs2Dredpacket.view;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar_department.lbs2Dredpacket.bean.BuyerBean;
import com.mgc.ar_department.lbs2Dredpacket.bean.SellerCouponBean;
import com.mgc.ar_department.lbs2Dredpacket.util.Constants;
import com.mgc.ar_department.lbs2Dredpacket.util.LogUtils;
import com.mgc.ar_department.lbs2Dredpacket.util.PermissionAndNetUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CouponRecord extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecycle;
    //private ImageButton head;
    // private TextView name;
    // private TextView number;
    private RelativeLayout lin;
    private ArrayList<SellerCouponBean> listCouponBean;
    private BuyerBean mBuyerBean;
    private int mId;
    private OkHttpClient mOkHttpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_record);
        mContext = this;

        /*head = (ImageButton) findViewById(R.id.coupon_img);
        name = (TextView) findViewById(R.id.coupon_name);
        number = (TextView) findViewById(R.id.coupon_number);*/
        mRecycle = (RecyclerView) findViewById(R.id.recycle);
        initIntent();
        loadNet();

    }

    private void loadNet() {
        mId = mBuyerBean.getId();
        String url = Constants.address + "lbsbonustext/keep.action?vname=" + mId + "&state=" + "1";
        mOkHttpClient = new OkHttpClient();
        Request mRequest = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(CouponRecord.this, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //返回红包历史记录
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtils.error("红包记录= " + string);
                Gson gson = new Gson();
                listCouponBean = new ArrayList<SellerCouponBean>();
                Type type = new TypeToken<List<SellerCouponBean>>() {
                }.getType();
                listCouponBean = gson.fromJson(string, type);
            }
        });
        /*final ConnectivityManager connect = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connect.getActiveNetworkInfo();*/
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.load_coupon);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listCouponBean == null) {
                        //Toast.makeText(mContext, "服务器繁忙,请稍后再试!", Toast.LENGTH_SHORT).show();
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);
                        //setInfo();
                        setData();
                    }
                }
            }, 1000);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    private void initIntent() {
        if (getIntent() != null) {

            Intent intent = getIntent();
            //Bundle extras = intent.getExtras();
            mBuyerBean = (BuyerBean) intent.getSerializableExtra("mBuyerBean");
            //listCouponBean = (ArrayList<SellerCouponBean>) intent.getSerializableExtra("listCouponBean");
        }
    }

    private void setInfo() {
       /* if (listCouponBean != null) {
            Glide.with(mContext).load(mBuyerBean.getV_head()).into(head);
            name.setText(mBuyerBean.getV_name());
            number.setText("共领取" + listCouponBean.size() + "张优惠券");
        } else {
            //number.setText("您还没有领到一张优惠券");
            Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
        }*/
    }

    private void setData() {

        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        RecycleAdapter recycleAdapter = new RecycleAdapter(mContext, mRecycle);
        mRecycle.setAdapter(recycleAdapter);
        recycleAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.error("位置" + position);
                
            }
        });
    }

    public void couponClose(View view) {
        finish();
    }

    public class RecycleAdapter extends RecyclerView.Adapter {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_NORMAL = 1;
        private View mHeaderView;
        private OnItemClickListener mListener;

        public RecycleAdapter(Context mContext, RecyclerView mRecycle) {
            mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.header_item, mRecycle, false);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                CouponHolder couponHolder = new CouponHolder(mHeaderView);
                return couponHolder;
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_record, null);
                CouponHolder couponHolder = new CouponHolder(view);
                return couponHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final CouponHolder couponHolder = (CouponHolder) holder;
            if (position == 0) {
                //couponHolder.setBuyData( mHeaderView);
                if (listCouponBean != null) {
                    couponHolder.headNumber.setText("共领取" + listCouponBean.size() + "张优惠券");
                } else {
                    Toast.makeText(mContext, Constants.server_remind, Toast.LENGTH_SHORT).show();
                }
                couponHolder.headName.setText(mBuyerBean.getV_name());
                Glide.with(mContext).load(mBuyerBean.getV_head()).into(couponHolder.headImage);
            } else {
                couponHolder.setCouponData(listCouponBean.get(position));

                couponHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(couponHolder.itemView, position);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (listCouponBean != null) {
                return listCouponBean.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;

        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.mListener = itemClickListener;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    static class CouponHolder extends RecyclerView.ViewHolder {

        private TextView headName;
        private TextView headNumber;
        private ImageButton headImage;
        private TextView name;
        private TextView number;
        private TextView date;
        private TextView overTime;
        // private View mHeaderView=LayoutInflater.from(mContext).inflate(R.layout.header_item, mRecycle, false);

        public CouponHolder(View view) {
            super(view);
            // if (view == mHeaderView) {
            headImage = (ImageButton) view.findViewById(R.id.coupon_img);
            headName = (TextView) view.findViewById(R.id.coupon_name);
            headNumber = (TextView) view.findViewById(R.id.coupon_number);
            //  } else {
            name = (TextView) view.findViewById(R.id.name_coupon_item);
            number = (TextView) view.findViewById(R.id.received_coupon_item);
            date = (TextView) view.findViewById(R.id.time_coupon_item);
            overTime = (TextView) view.findViewById(R.id.limittime_coupon_item);
            // }

        }

        public void setCouponData(SellerCouponBean packetBean) {
            name.setText("店铺:" + packetBean.getN_shops());
            date.setText("领取时间:" + packetBean.getP_time());
            number.setText("全场" + packetBean.getP_amount());
            overTime.setText("有效日期至:" + packetBean.getP_enddate());
        }


    }

}
