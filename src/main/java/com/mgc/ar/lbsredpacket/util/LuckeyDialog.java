package com.mgc.ar.lbsredpacket.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.test2Dlibrary.R;
import com.bumptech.glide.Glide;
import com.mgc.ar.lbsredpacket.activity.LBSAndPacketActivity;

/**
 * Created by SkyCheng on 2017/9/25.
 */

public class LuckeyDialog extends Dialog {

    public LuckeyDialog(Context context) {
        super(context);

    }
    public static class Builder {
        private Context mContext;
        private String name;//发红包者的名称
        public Button red_page;
        //拆红包按钮
        private String openButtonText;
        private OnClickListener openButtonClickListener;
        //关闭按钮
        private String closeButtonText;
        private OnClickListener closeButtonClickListener;
        private int mBao;
        private int mImageHead;
        public ImageButton mImage;
        private LBSAndPacketActivity location;
        private String path;
        private AnimationSet set;

        public Builder(Context context, int dialog) {
        }

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param name
         * @return
         */
        public Builder setName(int name) {
            this.name = (String) mContext.getText(name);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param name
         * @return
         */

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the positive open_button resource and it's listener
         *
         * @param closeButtonText
         * @return
         */
        public Builder setCloseButton(int closeButtonText,
                                      OnClickListener listener) {
            this.closeButtonText = (String) mContext
                    .getText(closeButtonText);
            this.closeButtonClickListener = listener;
            return this;
        }

        public Builder setCloseButton(String closeButtonText,
                                      OnClickListener listener) {
            this.closeButtonText = closeButtonText;
            this.closeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive open_button resource and it's listener
         *
         * @param openButtonText
         * @return
         */
        public Builder setOpenButton(int openButtonText,
                                     OnClickListener listener) {
            this.openButtonText = (String) mContext
                    .getText(openButtonText);
            this.openButtonClickListener = listener;
            return this;
        }

        public Builder setOpenButton(String openButtonText,
                                     OnClickListener listener) {
            this.openButtonText = openButtonText;
            this.openButtonClickListener = listener;
            return this;
        }

        public void setHeadImage(int image) {
            mImageHead = image;
        }

        public LuckeyDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //加载布局
            // final LuckeyDialog dialog = new LuckeyDialog(mContext, R.style.Dialog);
            final LuckeyDialog dialog = new LuckeyDialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无title
            dialog.setCanceledOnTouchOutside(true);  //外围可点击关闭

            View layout = inflater.inflate(R.layout.luckey_dialog, null);
            mImage = (ImageButton) layout.findViewById(R.id.head_img);
            Glide.with(mContext).load(path).into(mImage);

            LinearLayout lin = (LinearLayout) layout.findViewById(R.id.lin);
            //加入缩放动画
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(lin, "scaleX", 0, 1.2f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(lin, "scaleY", 0, 1.2f, 1f);
            animatorSet.setDuration(500);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
            animatorSet.start();

            red_page = (Button) layout.findViewById(R.id.l_open_btn);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //设置发红包者姓名
            ((TextView) layout.findViewById(R.id.name)).setText(name);

            //设置拆红包的按钮
            if (openButtonText != null) {
                ((Button) layout.findViewById(R.id.l_open_btn))
                        .setText("");
                if (openButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.l_open_btn))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    openButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);

                                }
                            });
                }
            } else {
                // if no confirm open_button just set the visibility to GONE
                layout.findViewById(R.id.l_open_btn).setVisibility(
                        View.GONE);
            }

            //设置关闭按钮
            if (closeButtonText != null) {
                ((Button) layout.findViewById(R.id.close))
                        .setText(closeButtonText);
                if (closeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.close))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    closeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm open_button just set the visibility to GONE
                layout.findViewById(R.id.close).setVisibility(
                        View.GONE);
            }

            dialog.setContentView(layout);
            return dialog;
        }

        public void setImage(String n_picture) {

            path = n_picture;
        }
    }
}

