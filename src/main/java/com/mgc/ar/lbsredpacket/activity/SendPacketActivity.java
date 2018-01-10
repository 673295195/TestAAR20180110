package com.mgc.ar.lbsredpacket.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.test2Dlibrary.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.StateBean;
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
import java.util.Map;

import static android.R.attr.defaultValue;
import static com.amap.test2Dlibrary.R.id.num;

public class SendPacketActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private static final String TAG = "SendPacketActivity";
    private static final double MONEY = 100;
    private Context mContext;
    private EditText mMoney;
    private TextView mTextView;
    private BuyerBean mBuyerBean;
    private EditText mNum;
    private EditText mwords;
    private double mJing;
    private double mWei;
    private Button mSend;
    private String mSNum;
    private String mSMoney;
    private Button mLock;
    private DecimalFormat mDf = new DecimalFormat("0.00");
    private String mFormat;
    private int mUserId;
    private ArrayList<StateBean> stateBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_packet);
        mContext = this;
        PermissionAndNetUtils.StatusTitleColor(mContext);
        initView();
        getIntentInfo();
    }

    private void initView() {
        mMoney = (EditText) findViewById(R.id.editText);
        mNum = (EditText) findViewById(num);
        mwords = (EditText) findViewById(R.id.words);
        mTextView = (TextView) findViewById(R.id.sendMoney);
        mSend = (Button) findViewById(R.id.send);
        mLock = (Button) findViewById(R.id.lock);
        mMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        mMoney.addTextChangedListener(this);
        mSend.addTextChangedListener(this);
        mNum.addTextChangedListener(this);
        mSend.setOnClickListener(this); //这种情况添加点击事件

    }

    private void getIntentInfo() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            mBuyerBean = (BuyerBean) getIntent().getSerializableExtra("mBuyerBean");
            mJing = intent.getDoubleExtra("jing", defaultValue);  //
            mWei = intent.getDoubleExtra("wei", defaultValue);
            mUserId = getIntent().getIntExtra("userId", defaultValue);
            LogUtils.error("jing=" + mJing + ";wei=" + mWei);
            LogUtils.error("buyerId=" + mBuyerBean.getId());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int
            count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        mSNum = mNum.getText().toString();
        mSMoney = mMoney.getText().toString();
        //String sWords = mwords.getText().toString();
        //1开头为1,\d{0,2} 数字重复0-2次,.后面数字重复1或0次    0.接数字在再加一个非零数字  0.01

        if (mSMoney.length() != 0) {
            Double aMoney = Double.valueOf(mSMoney);
            mFormat = mDf.format(aMoney);
            LogUtils.error("amoeny=" + aMoney + ";format=" + mFormat);
            mTextView.setText(mFormat);
            LogUtils.error("钱不为0");

            if (mSNum.length() != 0) {
                LogUtils.error("数目不为0");
                double MIN_MONEY = 0.01;
                double MAX_MONEY = 200.00;
                if ((aMoney >= MIN_MONEY && aMoney <= MAX_MONEY)         //0-99   |1.00-99.00       |100-199|100.00-199.00  |0.00-0.99  |200
                        && mSNum.matches("[1-9]\\d?") && mSMoney.matches("\\d{1,2}|\\d{1,2}.\\d{0,2}|1\\d{2}|\\d{3}.\\d{0,2}|0.\\d[1-9]?|2[0][0]")) { //1-99与0<amoney<=200
                    LogUtils.error("正确");
                    mLock.setVisibility(View.GONE);
                    mSend.setVisibility(View.VISIBLE);
                } else {
                    LogUtils.error("均不为0,错误");
                    mLock.setVisibility(View.VISIBLE);
                    mSend.setVisibility(View.GONE);
                    Toast.makeText(mContext, Constants.max_num, Toast.LENGTH_SHORT).show();
                }
            } else {
                LogUtils.error("数目为0");
                mLock.setVisibility(View.VISIBLE);
                mSend.setVisibility(View.GONE);
            }
        } else {
            LogUtils.error("钱为0");
            mLock.setVisibility(View.VISIBLE);
            mSend.setVisibility(View.GONE);
        }

    }

    @Override
    public void afterTextChanged(Editable edt) {  //000.00

        String temp = edt.toString();
        int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
        if (posDot <= 0) {//不包含小数点
            if (temp.length() <= 3) {
                return;//小于五位数直接返回
            } else {
                edt.delete(3, 4);//大于五位数就删掉第六位（只会保留五位）
                return;
            }
        }
        if (temp.length() - posDot - 1 > 2)//如果包含小数点
        {
            edt.delete(posDot + 3, posDot + 4);//删除小数点后的第三位
        }
    }

    public void sendClose(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        //正则判断

        Double sendMoney = Double.valueOf(mFormat);
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) { //用户id,经纬度,大小和个数

            if (MONEY >= sendMoney) { //余额>=发出的金额
                String sWords = mwords.getText().toString();
                Map<String, Object> params = new HashMap<>();
                params.put("userid", mUserId);
                params.put("money", mFormat);
                params.put("num", mSNum);
                params.put("language", sWords);
                params.put("longitude", mJing);
                params.put("latitude", mWei);
                Log.e(TAG, "红包数目: " + mSNum + ";金额=" + mFormat + ";祝福语=" + sWords);

                String path = Constants.address + "lbsbonustext/portcash.action";
                OkHttp.post(path, params, new OkCallback() { //上传红包数据
                    @Override
                    public void onResponse(String response) {
                        String json = response.toString();

                        LogUtils.error("json" + json);
                        Gson gson = new Gson();
                        stateBean = new ArrayList<StateBean>();
                        Type type = new TypeToken<List<StateBean>>() {
                        }.getType();
                        stateBean = gson.fromJson(json, type);
                        String state = stateBean.get(0).getState();
                        LogUtils.error("state" + state);
                        if (state.equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {//状态=1成功
                                    Toast.makeText(mContext, Constants.success, Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                        } else {  //其他失败
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, Constants.fail, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        LogUtils.error("2" + error.toString());
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, Constants.fail, Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    }
                });
            }else {//余额不足
                Toast.makeText(mContext, Constants.notEnough,Toast.LENGTH_SHORT).show();
            }
            //发到服务器,返回success,弹出toast提示发出成功,并关闭页面
            //发送失败,回调fail,toast=fail, 不关闭页面
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();

        }
    }

}
