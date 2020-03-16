package com.yyxnb.arch.base.mvvm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.yyxnb.arch.base.BaseFragment
import com.yyxnb.arch.common.Message
import com.yyxnb.utils.AppConfig


/**
 * Description: mvvm
 *
 * @author : yyx
 * @date ：2018/6/10
 */
@Deprecated("用注解@BindViewModel代替")
abstract class BaseFragmentVM<VM : BaseViewModel> : BaseFragment() {

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = initViewModel(AppConfig.getInstance(this, 0)!!)
        lifecycle.addObserver(mViewModel)
    }

    override fun initViewData() {
//        super.initViewData()
        registerDefUIChange()
        initObservable()
    }

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        mViewModel.defUI.toastEvent.observe(this, Observer {
            AppConfig.toast(it.toString())
        })
        mViewModel.defUI.msgEvent.observe(this, Observer {
            handleEvent(it)
        })
    }

    /**
     * 初始化界面观察者的监听
     * 接收数据结果
     */
    override fun initObservable() {}

    /**
     * 返回消息
     */
    override fun handleEvent(msg: Message?) {}

    open fun isShare() = false

    /**
     * 初始化ViewModel
     * create ViewModelProviders
     *
     * @return ViewModel
     */
    private fun initViewModel(modelClass: Class<VM>): VM {
        return if (isShare()){
            ViewModelProviders.of(mActivity).get(modelClass)
        }else{
            ViewModelProviders.of(this).get(modelClass)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //移除LifecycleObserver
        lifecycle.removeObserver(mViewModel)
        this.mViewModel to null
    }

}