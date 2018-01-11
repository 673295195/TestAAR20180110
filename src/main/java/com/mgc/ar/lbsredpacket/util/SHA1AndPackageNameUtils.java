package com.mgc.ar.lbsredpacket.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by SkyCheng on 2017/12/19 0019.
 */

public class SHA1AndPackageNameUtils {
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            String packageName = context.getPackageName();
            LogUtils.error("包名" + packageName);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            LogUtils.error("sHA1= " + result);
            return result.substring(0, result.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
