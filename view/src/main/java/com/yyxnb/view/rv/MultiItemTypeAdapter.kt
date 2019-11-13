package com.yyxnb.view.rv

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList

open class MultiItemTypeAdapter<T>(var data: MutableList<T>) : RecyclerView.Adapter<ViewHolder>() {
    
    private val mHeaderViews = SparseArrayCompat<View>()
    private val mFootViews = SparseArrayCompat<View>()

    protected var mItemDelegateManager: ItemDelegateManager<T> = ItemDelegateManager()
    protected var mOnItemClickListener: OnItemClickListener? = null

    private var mRecyclerView : RecyclerView? = null

    private val realItemCount: Int
        get() = itemCount - headersCount - footersCount

    val headersCount: Int
        get() = mHeaderViews.size()

    val footersCount: Int
        get() = mFootViews.size()
    
    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - realItemCount)
        }
        return if (!useItemDelegateManager()) super.getItemViewType(position) else mItemDelegateManager.getItemViewType(data[position - headersCount], position - headersCount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mHeaderViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mHeaderViews.get(viewType)!!)

        } else if (mFootViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mFootViews.get(viewType)!!)
        }
        val itemViewDelegate = mItemDelegateManager.getItemViewDelegate(viewType)

        val layoutId = itemViewDelegate.layoutId
        val holder = ViewHolder.createViewHolder(parent.context, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holder: ViewHolder, itemView: View) { }

    fun convert(holder: ViewHolder, t: T) {
        mItemDelegateManager.convert(holder, t, holder.adapterPosition - headersCount)
    }

    protected fun isEnabled(viewType: Int): Boolean {
        return true
    }

    fun setNewList(list: List<T>) {
        data.clear()
        data.addAll(list)
    }

    fun refreshData(list: List<T>?) {
        if (mRecyclerView != null && list != null) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun insertList(list: List<T>?) {
        if (mRecyclerView != null && list != null && list.isNotEmpty()) {
            val p = data.size
            data.addAll(list)
            notifyItemRangeInserted(p, list.size)
//            mRecyclerView?.scrollBy(0, 50)
        }
    }

    fun clearData() {
        if (mRecyclerView != null) {
            data.clear()
            notifyDataSetChanged()
        }
    }

    protected fun setListener(parent: ViewGroup, viewHolder: ViewHolder, viewType: Int) {
        if (!isEnabled(viewType)) return
        viewHolder.convertView.setOnClickListener { v ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition - headersCount
                mOnItemClickListener!!.onItemClick(v, viewHolder, position)
            }
        }

        viewHolder.convertView.setOnLongClickListener(View.OnLongClickListener { v ->
            if (mOnItemClickListener != null) {
                val position = viewHolder.adapterPosition - headersCount
                return@OnLongClickListener mOnItemClickListener!!.onItemLongClick(v, viewHolder, position)
            }
            false
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        convert(holder, data[position - headersCount])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        WrapperUtils.onAttachedToRecyclerView(
                recyclerView
        ) { layoutManager, oldLookup, position ->
            val viewType = getItemViewType(position)
            when {
                mHeaderViews.get(viewType) != null -> layoutManager.spanCount
                mFootViews.get(viewType) != null -> layoutManager.spanCount
                else -> oldLookup.getSpanSize(position)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    override fun getItemCount(): Int {
        val itemCount = data.size
        return headersCount + footersCount + itemCount
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + realItemCount
    }

    fun addHeaderView(view: View) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun addFootView(view: View) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
    }

    fun addItemDelegate(itemViewDelegate: ItemDelegate<T>): MultiItemTypeAdapter<T> {
        mItemDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    fun addItemDelegate(viewType: Int, itemViewDelegate: ItemDelegate<T>): MultiItemTypeAdapter<T> {
        mItemDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    protected fun useItemDelegateManager(): Boolean {
        return mItemDelegateManager.itemViewDelegateCount > 0
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int)

        fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    open class SimpleOnItemClickListener : OnItemClickListener {
        override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {}

        override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
            return false
        }
    }

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000
        private const val BASE_ITEM_TYPE_FOOTER = 200000
    }
    
}