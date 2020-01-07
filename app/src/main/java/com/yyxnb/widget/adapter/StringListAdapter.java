package com.yyxnb.widget.adapter;

import com.yyxnb.view.rv.BaseAdapter;
import com.yyxnb.view.rv.BaseViewHolder;
import com.yyxnb.widget.R;

import org.jetbrains.annotations.NotNull;

public class StringListAdapter extends BaseAdapter<String> {

    public StringListAdapter() {
        super(R.layout.item_string_list_layout);
    }

    @Override
    protected void bind(@NotNull BaseViewHolder holder, String s, int position) {
        holder.setText(R.id.tvText, s);
    }
}