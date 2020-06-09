package com.yyxnb.widget.adapter;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDelegate;
import com.yyxnb.adapter.ItemDiffCallback;
import com.yyxnb.adapter.MultiItemTypePagedAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.bean.MainBean;


public class MainListAdapter extends MultiItemTypePagedAdapter<MainBean> {

    public MainListAdapter() {
        super(new ItemDiffCallback<>());
        addItemDelegate(new ItemDelegate<MainBean>() {
            @Override
            public int layoutId() {
                return R.layout.item_main_title_layout;
            }

            @Override
            public boolean isThisType(MainBean item, int position) {
                return item.type == 1;
            }

            @Override
            public void convert(BaseViewHolder holder, MainBean mainBean, int position) {
                holder.setText(R.id.tvText, mainBean.title);
            }
        });

        addItemDelegate(new ItemDelegate<MainBean>() {
            @Override
            public int layoutId() {
                return R.layout.item_main_list_layout;
            }

            @Override
            public boolean isThisType(MainBean item, int position) {
                return item.type == 0;
            }

            @Override
            public void convert(BaseViewHolder holder, MainBean mainBean, int position) {
                String url = "http://img0.imgtn.bdimg.com/it/u=4073821464,3431246218&fm=26&gp=0.jpg";

                holder.setText(R.id.tvText, mainBean.title);
            }
        });
    }

}
