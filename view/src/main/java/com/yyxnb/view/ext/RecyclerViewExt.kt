package com.yyxnb.view.ext

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.*
import android.view.View
import com.yyxnb.view.rv.BaseAdapter
import com.yyxnb.view.rv.ItemDelegate
import com.yyxnb.view.rv.MultiItemTypeAdapter
import com.yyxnb.view.rv.BaseViewHolder

/**
 * 设置分割线
 * @param color 分割线的颜色，默认是#DEDEDE
 * @param size 分割线的大小，默认是1px
 * @param isReplace 是否覆盖之前的ItemDecoration，默认是true
 *
 */
fun RecyclerView.divider(color: Int = Color.parseColor("#DEDEDE"), size: Int = 1, isReplace: Boolean = true): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(size, size)
    })
    if (isReplace && itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
    addItemDecoration(decoration)
    return this
}


fun RecyclerView.vertical(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}


inline val RecyclerView.data
    get() = (adapter as BaseAdapter<*>).getData()

inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }


fun <T> RecyclerView.bindData(layoutId: Int, bindFn: (holderBase: BaseViewHolder, t: T, position: Int) -> Unit): RecyclerView {
    adapter = object : BaseAdapter<T>(layoutId) {
        override fun bind(holderBase: BaseViewHolder, t: T, position: Int) {
            bindFn(holderBase, t, position)
        }
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addHeader(headerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseAdapter<*>).addHeaderView(headerView)
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addFooter(footerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseAdapter<*>).addFootView(footerView)
    }
    return this
}

fun <T> RecyclerView.multiTypes(itemDelegates: List<ItemDelegate<T>>): RecyclerView {
    adapter = MultiItemTypeAdapter<T>().apply {
        itemDelegates.forEach { addItemDelegate(it) }
    }
    return this
}

//fun <T> RecyclerView.itemClick(listener: (data: List<T>, holder: RecyclerView.ViewHolder, position: Int) -> Unit): RecyclerView {
//    adapter?.apply {
//        (adapter as MultiItemTypeAdapter<*>).setOnItemClickListener(object : MultiItemTypeAdapter.SimpleOnItemClickListener() {
//            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
//                listener(data as List<T>, holder, position)
//            }
//        })
//    }
//    return this
//}
//
//fun <T> RecyclerView.itemLongClick(listener: (data: List<T>, holder: RecyclerView.ViewHolder, position: Int) -> Unit): RecyclerView {
//    adapter?.apply {
//        (adapter as MultiItemTypeAdapter<*>).setOnItemClickListener(object : MultiItemTypeAdapter.SimpleOnItemClickListener() {
//            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
//                listener(data as List<T>, holder, position)
//                return super.onItemLongClick(view, holder, position)
//            }
//        })
//    }
//    return this
//}