package com.ks.library.log;

import android.util.Log;

/**
 * <p>
 *
 * @author kangsen
 * @version v9.0
 * @since 2017/7/28
 */

public class LogUtil {
    private static final boolean isDebugOn = true;
    private static final String TAG = "KS_LOG";
    
    public static void v(String str) {
        if (isDebugOn) {
            Log.v(TAG, str);
        }
    }
    
    public static void d(String str) {
        if (isDebugOn) {
            Log.d(TAG, str);
        }
    }
    
    public static void i(String str) {
        if (isDebugOn) {
            Log.i(TAG, str);
        }
    }
    
    public static void e(String str) {
        if (isDebugOn) {
            Log.e(TAG, str);
        }
    }
    
    public static void w(String str) {
        if (isDebugOn) {
            Log.w(TAG, str);
        }
    }
}
