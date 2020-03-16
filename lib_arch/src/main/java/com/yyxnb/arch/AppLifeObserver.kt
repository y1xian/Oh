package com.yyxnb.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.yyxnb.utils.AppConfig

/**
 * 监听应用状态
 */
class AppLifeObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        AppConfig.debugLog("应用进入前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        AppConfig.debugLog("应用进入后台")
    }
}