package com.yyxnb.view.proxy.imageloader

/**
 * 代理模式中用于规范代理类和真实类行为的接口
 */
interface ILoaderProxy {

    fun loadImage(options: LoaderOptions)

    /**
     * 清理内存缓存
     */
    fun clearMemoryCache()

    /**
     * 清理磁盘缓存
     */
    fun clearDiskCache()

}