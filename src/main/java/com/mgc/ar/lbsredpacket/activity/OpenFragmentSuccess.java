package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerBean;
import com.mgc.ar.lbsredpacket.bean.SellerFragmentBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;

public class OpenFragmentSuccess extends AppCompatActivity {


    private TextView search;
    private SellerFragmentBean mFragmentBean;
    private SellerBean mSellerBean;
    private BuyerBean mBuyerBean;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_fragment_success);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        initIntent();
        initData();
        setListener();
    }

    private void setListener() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
                if (activeNetworkInfo != null) {
                    //参数一会员id,参数2红包状态
                    Intent intent = new Intent(OpenFragmentSuccess.this, FragmentRecord.class);
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

    private void initData() {
        ImageButton head = (ImageButton) findViewById(R.id.head_img_fragment);
        TextView name = (TextView) findViewById(R.id.name_fragment);
        ImageView fragment = (ImageView) findViewById(R.id.money_fragment);
        search = (TextView) findViewById(R.id.search_fragment);

        name.setText(mSellerBean.getN_shops());
       // money.setText(mFragmentBean.getVi_chipcol());
        Glide.with(mContext).load(mFragmentBean.getVi_chipcol()).into(fragment);
        Glide.with(mContext).load(mSellerBean.getN_picture()).into(head);
    }

    private void initIntent() {
        if (getIntent() != null) {
            mSellerBean = (SellerBean) getIntent().getSerializableExtra("sellerBean");
            mFragmentBean = (SellerFragmentBean) getIntent().getSerializableExtra("mFragmentBean");
            mBuyerBean = (BuyerBean) getIntent().getSerializableExtra("mBuyerBean");
        }
    }

    public void fragment(View view) {
        finish();
    }
}
