package com.yyxnb.widget.skin;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yyxnb.adapter.BaseViewHolder;

public class RecyclerViewSetter extends ViewGroupSetter {


    public RecyclerViewSetter(ViewGroup targetView, int resId) {
        super(targetView, resId);
    }

    public RecyclerViewSetter(ViewGroup targetView) {
        super(targetView);
    }

    @Override
    public void clearRecyclerViewRecyclerBin(View rootView) {
        super.clearRecyclerViewRecyclerBin(rootView);

        ((RecyclerView) rootView).getRecycledViewPool().clear();
    }

    @Override
    public void setValue(Resources.Theme newTheme, int themeId) {

        clearRecyclerViewRecyclerBin(mView);

        if (((RecyclerView) mView).getAdapter() == null){
            return;
        }

        // 遍历子元素与要修改的属性,如果相同那么则修改子View的属性
        for (ViewSetter setter : mItemViewSetters) {

            for (int i = 0; i < ((RecyclerView) mView).getAdapter().getItemCount(); i++) {

                View itemView = ((RecyclerView) mView).getChildAt(i);
                if (itemView == null) {
                    continue;
                }

                boolean isBaseAdapterHelper = ((RecyclerView) mView).getChildViewHolder(itemView) instanceof BaseViewHolder;

                if (!isBaseAdapterHelper) {
                    continue;
                }

                BaseViewHolder baseAdapterHelper = (BaseViewHolder) ((RecyclerView) mView).getChildViewHolder(itemView);
                setter.mView = baseAdapterHelper.getView(setter.mViewId);
                int itemId = setter.getViewId();

                if (baseAdapterHelper.getView(itemId) == null) {
                    continue;
                }
                setter.setValue(newTheme, themeId);
            }
        }
    }

}