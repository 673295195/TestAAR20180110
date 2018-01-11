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
import android.widget.Button;
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
import com.mgc.ar.lbsredpacket.bean.SellerFragmentBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;
import com.smile.okhttpintegration.OkCallback;
import com.smile.okhttpintegration.OkHttp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FragmentRecord extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button button;
    private ArrayList<SellerFragmentBean> listFragmentBean;
    private BuyerBean mBuyerBean;
    private Context mContext;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_record);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        init();
        initIntent();
        loadNet();

    }


    private void init() {
        button = (Button) findViewById(R.id.fragment_detail_close);
      /*  image = (ImageButton) findViewById(R.id.fragment_img);
        name = (TextView) findViewById(R.id.fragment_name);
        number = (TextView) findViewById(R.id.fragment_number);*/
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recycle);

    }

    private void initIntent() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            mBuyerBean = (BuyerBean) intent.getSerializableExtra("mBuyerBean");

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadNet();
    }

    private void loadNet() {

        mId = mBuyerBean.getId();
        LogUtils.error("mid=" + mId);
        String url = Constants.address + "lbsbonustext/fraGment.action";//?userid=" + mId;
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("userid", mId);
        OkHttp.post(url, mParams, new OkCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.error("返回碎片记录" + response);
                Gson gson = new Gson();
                listFragmentBean = new ArrayList<SellerFragmentBean>();
                Type type = new TypeToken<List<SellerFragmentBean>>() {
                }.getType();
                listFragmentBean = gson.fromJson(response, type);
            }

            @Override
            public void onFailure(String error) {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(FragmentRecord.this, Constants.server_remind, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            final LinearLayout loading = (LinearLayout) findViewById(R.id.load_fragment);
            loading.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listFragmentBean == null) {
                        //Toast.makeText(mContext, "服务器繁忙,请稍后再试!", Toast.LENGTH_SHORT).show();
                        //SystemClock.sleep(2000);
                        loadNet();
                    } else {
                        loading.setVisibility(View.GONE);
                        //setData();
                        initData();
                    }
                }
            }, 1000);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FragmentAdapter adapter = new FragmentAdapter(recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.error("fragment位置=" + position);
                Intent intent = new Intent(mContext, FragmentDetail.class);
                intent.putExtra("userId", mId);
                intent.putExtra("position", position);
                LogUtils.error("碎片id=" + listFragmentBean.get(position).getVi_nid());
                intent.putExtra("sellerId", listFragmentBean.get(position).getVi_nid());
                intent.putExtra("sellerName", listFragmentBean.get(position).getN_shops());
                startActivity(intent);
            }
        });
    }

    public void fragmentClose(View view) {
        finish();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private class FragmentAdapter extends RecyclerView.Adapter {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_NORMAL = 1;
        private final View mHeaderView;
        private OnItemClickListener mListener;

        public FragmentAdapter(RecyclerView recyclerView) {
            mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.header_item, recyclerView, false);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_HEADER == viewType) {
                return new FragmentHolder(mHeaderView);
            } else {
                View inflate = LayoutInflater.from(FragmentRecord.this).inflate(R.layout.item_fragment_record, null);
                FragmentHolder fragmentHolder = new FragmentHolder(inflate);
                return fragmentHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final FragmentHolder fragmentHolder = (FragmentHolder) holder;
            if (position == 0) {
                fragmentHolder.headName.setText(mBuyerBean.getV_name());
                Glide.with(mContext).load(mBuyerBean.getV_head()).into(fragmentHolder.headImage);
                if (listFragmentBean != null) {
                    //fragmentHolder.headNumber.setText("共领取了" + (listFragmentBean.size()-1) + "个碎片");
                    fragmentHolder.headNumber.setText("您已领取了" + listFragmentBean.size() + "个商家碎片");
                } else {
                    //number.setText("您还没有领到碎片哦!");
                    Toast.makeText(FragmentRecord.this, Constants.server_remind, Toast.LENGTH_SHORT).show();
                }

            } else {

                fragmentHolder.setFragmentData(listFragmentBean.get(position - 1));
                Glide.with(mContext).load(listFragmentBean.get(position - 1).getN_picture()).into(fragmentHolder.image);
                fragmentHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(fragmentHolder.itemView, position - 1);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (listFragmentBean != null) {
                return listFragmentBean.size() + 1;
            } else return 0;
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

    private static class FragmentHolder extends RecyclerView.ViewHolder {
        private final TextView sellerName;
        private final TextView date;
        private final ImageView image;
        private TextView headName;
        private TextView headNumber;
        private ImageButton headImage;

        public FragmentHolder(View view) {
            super(view);
            headImage = (ImageButton) view.findViewById(R.id.coupon_detailimg);  //头像
            headName = (TextView) view.findViewById(R.id.coupon_detailname);
            headNumber = (TextView) view.findViewById(R.id.coupon_detailnumber);

            sellerName = (TextView) view.findViewById(R.id.name_fragment_item); //recycleView里面的
            date = (TextView) view.findViewById(R.id.time_fragment_item);
            // number = (TextView) view.findViewById(R.id.received_fragment_item);
            image = (ImageView) view.findViewById(R.id.image_seller_fragment);

        }

        public void setFragmentData(SellerFragmentBean packetBean) {
            sellerName.setText(packetBean.getN_shops());
            date.setText(packetBean.getVi_time());
            //number.setText(packetBean.getVi_chipcol());
        }
    }
}
