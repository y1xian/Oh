package com.yyxnb.oh.core;


import com.yyxnb.oh.log.LogUtils;
import com.yyxnb.oh.application.ApplicationUtils;

/**
 * 常用管理
 *
 * @author yyx
 */
public class CommonManager {

    private static volatile CommonManager mInstance = null;

    private CommonManager() {
    }

    public static CommonManager getInstance() {
        if (null == mInstance) {
            synchronized (CommonManager.class) {
                if (null == mInstance) {
                    mInstance = new CommonManager();
                }
            }
        }
        return mInstance;
    }


    public void toast(String s) {
        ToastUtils.normal(s);
    }

    public void log(String tag, String s) {
        if (ApplicationUtils.isDebug()) {
            LogUtils.w(s, tag);
        }
    }

    public void log(String s) {
        if (ApplicationUtils.isDebug()) {
            log("------AppConfig------", s);
        }
    }


}