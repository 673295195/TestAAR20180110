package com.mgc.ar.lbsredpacket.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.test2Dlibrary.R;
import com.mgc.ar.lbsredpacket.bean.BuyerBean;
import com.mgc.ar.lbsredpacket.bean.SellerBean;
import com.mgc.ar.lbsredpacket.bean.SellerCouponBean;
import com.mgc.ar.lbsredpacket.bean.SellerFragmentBean;
import com.mgc.ar.lbsredpacket.bean.SellerPacketBean;
import com.mgc.ar.lbsredpacket.util.Constants;
import com.mgc.ar.lbsredpacket.util.LogUtils;
import com.mgc.ar.lbsredpacket.util.LuckeyDialog;
import com.mgc.ar.lbsredpacket.util.MyYAnimation;
import com.mgc.ar.lbsredpacket.util.OkHttpUtil;
import com.mgc.ar.lbsredpacket.util.PermissionAndNetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.R.attr.defaultValue;
import static com.amap.test2Dlibrary.R.id.map;


public class LBSAndPacketActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener,
        AMap.OnMarkerClickListener, AMap.OnMapClickListener
        , AMap.OnMapLongClickListener, AMap.OnMapLoadedListener, AMap.OnMapTouchListener {

    private MapView mMapView;
    private AMap aMap;
    private Circle circle;
    private MyLocationStyle myLocationStyle;
    private double mWei;
    private double mJing;
    private Location mLocation;
    private MarkerOptions markerOption;
    private LatLng latlng = new LatLng(mWei, mJing);
    private Marker marker2;
    private final float R1 = (float) 200.0;  //范围圆200米
    private LatLng mLatLng;
    private OkHttpUtil mOkHttpUtil = new OkHttpUtil(this);
    private ArrayList<SellerBean> mSellerBean;
    private double mLat1;
    private double mLon1;
    private HashMap<String, Integer> mHashMap;
    private ArrayList<BuyerBean> buyerBeanArrayList;
    private SellerPacketBean mPacketBean;
    private SellerCouponBean mCouponBean;
    private SellerFragmentBean mFragmentBean;
    private Context mContext;
    private ArrayList list;
    private BuyerBean mBuyerBean;
    private Bundle bundle;
    private SensorManager mSM;
    private Button refresh;
    private ImageView load;
    private RelativeLayout mMenu;
    private TextView mRed_record;
    private TextView mRefresh;
    private LinearLayout mLinBar;
    private TextView mRecord;
    private TextView mReturn1;
    private boolean click = true;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};

    // 打开相机请求Code，多个权限请求Code
    private final int REQUEST_CODE_PERMISSIONS = 2;
    private String redNum;
    private int mUserId;
    private Marker mMarker;
    private final LatLng CENTER = new LatLng(mJing, mWei);
    private float mPreviousX;
    private float mPreviousY;
    private static final float TOUCH_SCALE_FACTOR = 180.0f / 640;
    private ImageView mSendPacket;
    private HashMap<Integer, Integer> position = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity_main);
        PermissionAndNetUtils.StatusTitleColor(this);
        mContext = this;
        initIntent();//传会员id
        requestMorePermissions1(); //6.0权限
        initView();
        mMapView.onCreate(savedInstanceState);
        bundle = new Bundle();
        //初始化地图
        initData();
        //SHA1AndPackageNameUtils.sHA1(mContext);
    }

    private void initIntent() {
        if (getIntent() != null) {
            mUserId = getIntent().getIntExtra("id", defaultValue);
        }
    }

    // 自定义申请多个权限
    private void requestMorePermissions1() {
        PermissionAndNetUtils.checkMorePermissions(mContext, PERMISSIONS, new PermissionAndNetUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                //toCamera();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                showExplainDialog(permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionAndNetUtils.requestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    }
                });
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionAndNetUtils.requestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }
        });
    }

    /**
     * 解释权限的dialog
     */
    private void showExplainDialog(String[] permission, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(mContext)
                .setTitle("申请权限")
                .setMessage("我们需要" + Arrays.toString(permission) + "权限")
                .setPositiveButton("确定", onClickListener)
                .show();
    }

    /**
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("需要权限")
                .setMessage("我们需要相关权限，才能实现功能，点击前往，将转到应用的设置界面，请开启应用的相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionAndNetUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                PermissionAndNetUtils.onRequestMorePermissionsResult(mContext, PERMISSIONS, new PermissionAndNetUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        //toCamera();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(mContext, "我们需要" + Arrays.toString(permission) + "权限", Toast.LENGTH_SHORT).show();
                        LogUtils.error("需要权限" + Arrays.toString(permission));
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(mContext, "我们需要" + Arrays.toString(permission) + "权限", Toast.LENGTH_SHORT).show();
                        LogUtils.error("需要权限ask" + Arrays.toString(permission));
                        showToAppSettingDialog();
                    }
                });
        }
    }

    private void initView() {
        mMapView = (MapView) findViewById(map);//地图
        mReturn1 = (TextView) findViewById(R.id.return1); //返回
        mRecord = (TextView) findViewById(R.id.record);
        mRed_record = (TextView) findViewById(R.id.red_record);//红包
        mRefresh = (TextView) findViewById(R.id.red_refresh);//刷新
        mLinBar = (LinearLayout) findViewById(R.id.bar_record); //弹出菜单
        mMenu = (RelativeLayout) findViewById(R.id.menu);
        mSendPacket = (ImageView) findViewById(R.id.sendPacket); //发红包
    }


    private void addRedPacket() {
        // for循环添加marker
        mHashMap = new HashMap<String, Integer>();
        list = new ArrayList();
        markerOption = new MarkerOptions();
        for (int i = 0; i < mSellerBean.size(); i++) {
            int id = mSellerBean.get(i).getId();
            mLat1 = mSellerBean.get(i).getN_lat();
            mLon1 = mSellerBean.get(i).getN_location();
            String name = mSellerBean.get(i).getN_shops();
            mHashMap.put(name, id);
            list.add(name);
            redNum = mSellerBean.get(i).getN_notopen();
            markerOption.position(new LatLng(mLat1, mLon1));
            markerOption.title(name).snippet("剩" + redNum + "个可抢红包");

            markerOption.draggable(true);
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_packet));
            marker2 = aMap.addMarker(markerOption);

          /*  Point point = aMap.getProjection().toScreenLocation(new LatLng(mLat1, mLon1));
            int y = point.y;
            int x = point.x;
            LogUtils.error("屏幕y=" + y);
            position.put(x, y);
            xPosition.add(x);*/

        }

        //添加地图上红包的点击事件
        aMap.setOnMarkerClickListener(this);

        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        // aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器,暂时不用
        //drawMarkers();// 添加10个带有系统默认icon的marker
    }

    public void drawMarkers() {
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    /**
     * 初始化AMap对象
     */
    private void initData() {
        NetworkInfo networkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (networkInfo != null) {
            if (aMap == null) {
                aMap = mMapView.getMap();
                setUpMap();
                UiSettings uiSettings = aMap.getUiSettings();
                //放大缩小图标隐藏
                uiSettings.setScaleControlsEnabled(false);  //比例尺
                uiSettings.setZoomControlsEnabled(false);//隐藏缩小放大图标
                //所有手势锁定
                uiSettings.setAllGesturesEnabled(true);  //所有手势
                //uiSettings.setZoomGesturesEnabled(true);  //缩放手势
                // uiSettings.setScrollGesturesEnabled(true);  //滑动手势
                //uiSettings.setRotateGesturesEnabled(true); //旋转手势
            }
            //设置SDK 自带定位消息监听
            aMap.setOnMyLocationChangeListener(this);
            //aMap.setOnMapLoadedListener(this);
            //aMap.setOnInfoWindowClickListener(this);
            //aMap.setOnMapTouchListener(this);
            loadNetData();
            //circleAndLocation();
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    // 加载商家bean数据,会员
    private void loadNetData() {

        mOkHttpUtil.getSellerBean();
        mOkHttpUtil.getBuyerBean(mUserId);
    }

    /**
     * 方法必须重写
     */
    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        //myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)); //定位1次
        // aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER));
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色

        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色  。
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示  设置不显示
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        circleAndLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.error("销毁");
        mMapView.onDestroy();
        // TIME=0;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.error("获取焦点");
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.error("暂停");
        mMapView.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.error("重新开始");
        refresh(refresh); //刷新地图
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * Circle中对填充颜色，透明度，画笔宽度设置响应事件
     */
    @Override
    public void onMyLocationChange(Location location) {  //位置改变时调用
        mLocation = location;
        // 定位回调监听

        if (location != null) {
            LogUtils.error("onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();

            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                LogUtils.error("定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
                //circleAndLocation();
                mJing = mLocation.getLongitude();
                mWei = mLocation.getLatitude();

                //限定周围范围
                circleAndLocation();
                //aMap.setMyLocationEnabled(false);
               /* TIME++;
                if ( TIME > 1) {
                   // circleAndLocation();
                }*/
            } else {
                LogUtils.error("定位信息， bundle is null ");
            }
        } else {
            LogUtils.error("定位失败");
        }
    }

    //圆圈范围
    private void circleAndLocation() {
        LogUtils.error("1");
        if (circle != null) {
            circle.remove();
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mWei, mJing), 17.5f));// 设置指定的可视区域地图
        circle = aMap.addCircle(new CircleOptions().center(new LatLng(mWei, mJing))
                .radius(200).strokeColor(Color.RED)
                .fillColor(Color.argb(40, 20, 20, 20)).strokeWidth(0));
        //fillColor(Color.argb(40, 20, 20, 20))
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        mMarker = marker;   //marker点击事件

        //aMap.setInfoWindowAdapter(this);
        //计算2个经纬度之间的距离,精准度更高
        LatLng position = marker.getPosition();
        final String title = marker.getTitle();
        LogUtils.error("title=" + title);

        double longitude = position.longitude;
        double latitude = position.latitude;
        // marker定位的经纬度
        LatLng latlng3 = new LatLng(latitude, longitude);
        /*当前定位的经纬度*/
        LatLng latlng1 = new LatLng(mWei, mJing);
        float calculateLineDistance = AMapUtils.calculateLineDistance(latlng1, latlng3);
        LogUtils.error("2点距离marker:= " + calculateLineDistance + "米");

        //判断是否在范围内,在则弹出红包
        // if (title.equals(name)) {
        if (calculateLineDistance <= R1 && calculateLineDistance > 0) {

            final int buyerId = mHashMap.get(title);
            LogUtils.error("点击的位置=" + buyerId);
            //弹出比例红包
            // final LuckeyDialog.Builder builder = new LuckeyDialog.Builder(mContext, R.style.Dialog);//调用style中的Diaog
            final LuckeyDialog.Builder builder = new LuckeyDialog.Builder(mContext);//调用style中的Diaog
            //商家名字
            final String shopsName = title;
            //商家头像
            final int i = list.indexOf(title);//点击的位置
            //传入头像链接到dialog
            builder.setImage(mSellerBean.get(i).getN_picture());
            //设置商家名字
            builder.setName(shopsName);
            /*//商家头像
            final int i = list.indexOf(title);//点击的位置
            //传入头像链接到dialog
            builder.setImage(mSellerBean.get(i).getN_picture());*/
            //点击的商家
            final SellerBean sellerBean = mSellerBean.get(i);

            builder.setOpenButton("", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int which) {
                    //判断网络是否有链接
                    NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
                    if (activeNetworkInfo != null) {
                        //网络请求,获取红包金额
                        mOkHttpUtil.getPacketMoney(buyerId, mSellerBean.get(i).getN_redstate(), mUserId);

                        //旋转动画
                        final MyYAnimation myYAnimation = new MyYAnimation();
                        builder.red_page.setBackgroundResource(R.drawable.open_button);
                        builder.red_page.startAnimation(myYAnimation);

                        myYAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            //动画结束跳转
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (mSellerBean.get(i).getN_redstate().equals("0")) {  //现金红包

                                    //状态为0,并且不为空
                                    if (mPacketBean != null && mPacketBean.getCa_switchstate().equals("0") && mPacketBean.getCa_amount() != null) {
                                        Intent intent = new Intent(LBSAndPacketActivity.this, OpenPacketSuccess.class);
                                        //Bundle bundle = new Bundle();
                                        bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                        bundle.putSerializable("mPacketBean", mPacketBean); //商家现金信息
                                        bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        marker.destroy();
                                    } else {
                                        Intent intent = new Intent(LBSAndPacketActivity.this, OpenFail.class);
                                        //Bundle bundle = new Bundle();
                                        bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                        bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        marker.destroy();
                                        Toast.makeText(LBSAndPacketActivity.this, "手速慢了", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (mSellerBean.get(i).getN_redstate().equals("1")) {  //优惠券
                                    //状态为0,并且不为空
                                    if (mCouponBean != null && mCouponBean.getP_switchstate().equals("0") && mCouponBean.getP_amount() != null) {
                                        Intent intent = new Intent(LBSAndPacketActivity.this, OpenCouponSuccess.class);
                                        //Bundle bundle = new Bundle();
                                        bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                        bundle.putSerializable("mCouponBean", mCouponBean); //商家现金信息
                                        bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        marker.destroy();
                                    } else {
                                        Intent intent = new Intent(LBSAndPacketActivity.this, OpenFail.class);
                                        intent.putExtra("name", shopsName);  //商家名字
                                        //Bundle bundle = new Bundle();
                                        bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                        bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        marker.destroy();
                                        Toast.makeText(LBSAndPacketActivity.this, "手速慢了", Toast.LENGTH_SHORT).show();
                                    }
                                } else {  //活动碎片
                                    //状态为0,并且不为空
                                    if (mFragmentBean != null && mFragmentBean.getVi_switchstate().equals("0") && mFragmentBean.getVi_chipcol() != null) {
                                        //String chipcol = mFragmentBean.getVi_chipcol();
                                        Intent intent = new Intent(LBSAndPacketActivity.this, OpenFragmentSuccess.class);
                                        //Bundle bundle = new Bundle();
                                        bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                        bundle.putSerializable("mFragmentBean", mFragmentBean); //商家现金信息
                                        bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        marker.destroy();

                                    } else {
                                        Intent intent = new Intent(LBSAndPacketActivity.this, OpenFail.class);
                                        intent.putExtra("name", shopsName);  //商家名字

                                        //Bundle bundle = new Bundle();
                                        bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                        bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        marker.destroy();
                                        Toast.makeText(LBSAndPacketActivity.this, "手速慢了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    } else {
                        //网络未连接时
                        final MyYAnimation myYAnimation = new MyYAnimation();
                        builder.red_page.setBackgroundResource(R.drawable.open_button);
                        builder.red_page.startAnimation(myYAnimation);
                        myYAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Intent intent = new Intent(LBSAndPacketActivity.this, OpenFail.class);
                                //Bundle bundle = new Bundle();
                                bundle.putSerializable("sellerBean", sellerBean); //商家信息
                                bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
                                intent.putExtras(bundle);
                                startActivity(intent);
                                dialog.dismiss();
                                marker.destroy();
                                Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                }
            });

            builder.setCloseButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            Dialog dialog = builder.create();
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setBackgroundDrawableResource(R.drawable.transparent_bg);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            Point size = new Point();
            d.getSize(size);
            int width = size.x;
            int height = size.y;

            p.height = (int) (height * 0.75); // 高度设置为屏幕的0.7
            p.width = (int) (width * 0.8); // 宽度设置为屏幕的0.75
           /* //谷歌弃用的API
            p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的0.7
            p.width = (int) (d.getWidth() * 0.75); // 宽度设置为屏幕的0.75*/

            dialogWindow.setAttributes(p);
            dialog.show();
            //true表示消费该事件,可以点击
            return true;
            //表示超出范围200米
        } else if (calculateLineDistance > 200) {
//            markerOption.s
            Toast.makeText(this, "距离商家太远,请靠近试试,", Toast.LENGTH_SHORT).show();
            //marker.destroy();
            return false;
            //距离等于0
        } else {
            return false;
        }
    }

    // 点击地图,显示点击坐标
    @Override
    public void onMapClick(LatLng latLng) {
        mMarker.hideInfoWindow();
        mLatLng = latLng;
        double latitude = mLatLng.latitude;
        double longitude = mLatLng.longitude;
        LogUtils.error("onMapClick: 点击坐标" + latitude + "==" + longitude);
        LatLng latlng1 = new LatLng(mWei, mJing);
        //高德自带的util工具类
        float calculateLineDistance = AMapUtils.calculateLineDistance(latlng1, mLatLng);
        LogUtils.error("2个marker间距离=" + calculateLineDistance + "米");
    }

    //长按事件
    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    //进入时,网络获取的所有商家数据
    public void setData(ArrayList<SellerBean> sellerBean) {
        if (sellerBean != null) {
            mSellerBean = sellerBean;
            addRedPacket();
        } else {
            mOkHttpUtil.getSellerBean();
        }
    }

    public void setBuyerData(ArrayList<BuyerBean> buyerList) {
        if (buyerList != null) {
            buyerBeanArrayList = buyerList;
            mBuyerBean = buyerBeanArrayList.get(0);
            //mBuyerBean.setId(mUserId);
            LogUtils.error("buyID=" + mBuyerBean.getId());
        } else {
            mOkHttpUtil.getBuyerBean(mUserId);
        }
    }

    //获取1个红包金额
    public void getPacketData(SellerPacketBean sellerPacketBean) {
        mPacketBean = sellerPacketBean;
    }

    //获取1个优惠券
    public void getCouponData(SellerCouponBean couponBean) {
        mCouponBean = couponBean;
    }

    //获取1个活动碎片
    public void getFragmentData(SellerFragmentBean packetFragmentBean) {
        mFragmentBean = packetFragmentBean;
    }

    //会员信息 1个
    public void historyRecord(View view) {
        if (click) {
            translationDown();
            if (view.getId() == R.id.red_record) {
                redRecord(view);
            } else if (view.getId() == R.id.red_refresh) {
                refresh(view);
            }
        } else {
            translationUp();
        }
        click = !click;
    }

    public void refresh(View view) {  //刷新地图
        mLinBar.setVisibility(View.GONE);
        click = !click;
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            aMap.clear();
            setUpMap();
            circleAndLocation();
            loadNetData();
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    //返回上一级
    public void returnLast(View view) {
        finish();
    }

    public void redRecord(View view) {  //查看记录
        mLinBar.setVisibility(View.GONE);
        click = !click;
        NetworkInfo activeNetworkInfo = PermissionAndNetUtils.getActiveNetworkInfo(mContext);
        if (activeNetworkInfo != null) {
            Intent intent = new Intent(LBSAndPacketActivity.this, MyHistoryRecord.class);
            intent.putExtra("userId", mUserId);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, Constants.net_remind, Toast.LENGTH_SHORT).show();
        }
    }

    public void translationDown() {
        mLinBar.setVisibility(View.VISIBLE);
        //int height = mLinBar.getMeasuredHeight()//360;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLinBar, "translationY", -360, 0);  //往下走
        translationY.setDuration(500);
        translationY.start();
    }

    public void translationUp() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLinBar, "translationY", 0, -360); //往上走
        translationY.setDuration(500);
        translationY.start();
    }

    public void sendPacket(View view) {  //发红包
        if (mSellerBean != null && mBuyerBean != null) {
            Intent intent = new Intent(LBSAndPacketActivity.this, SendPacketActivity.class);
            intent.putExtra("jing", mJing);
            intent.putExtra("wei", mWei);
            intent.putExtra("userId", mUserId); //用户id
            bundle.putSerializable("mBuyerBean", mBuyerBean);  //消费者信息
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(mContext, Constants.server_slow, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapLoaded() {  //2d地图用不到
        //设置地图中心点为屏幕的1/2和3/4位置
        LogUtils.error("tk");
        aMap.setPointToCenter(mMapView.getWidth() / 2, mMapView.getHeight() / 4 * 3);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(mJing, mWei), 17.5f, 60, 0)));
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {

        //followMove = false;
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > mMapView.getHeight() / 4 * 3) {
                    dx = dx * -1;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < mMapView.getWidth() / 2) {
                    dy = dy * -1;
                }
                CameraPosition cameraPosition = aMap.getCameraPosition();
                cameraPosition = new CameraPosition(CENTER, 17.5f, 60, (cameraPosition.bearing - ((dx + dy) * TOUCH_SCALE_FACTOR)));
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
        mPreviousX = x;
        mPreviousY = y;

    }
}


