package com.yyxnb.widget.fragments.adapter;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.view.rv.MultiItemTypeAdapter;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.NetWorkListAdapter;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.config.DataConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * 适配器 头 + 底.
 */
public class AdapterHeaderAndFooterFragment extends BaseFragment {

    private NetWorkListAdapter mAdapter;
    private TextView tvAddHeader;
    private TextView tvAddFooter;
    private TextView tvClear;
    private TextView tvAddData;
    private RecyclerView mRecyclerView;

    public static AdapterHeaderAndFooterFragment newInstance() {

        Bundle args = new Bundle();

        AdapterHeaderAndFooterFragment fragment = new AdapterHeaderAndFooterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_adapter_header_and_footer;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        tvAddHeader = findViewById(R.id.tvAddHeader);
        tvAddFooter = findViewById(R.id.tvAddFooter);
        tvClear = findViewById(R.id.tvClear);
        tvAddData = findViewById(R.id.tvAddData);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mAdapter = new NetWorkListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        View view = LayoutInflater.from(mContext).inflate(R.layout._loading_layout_empty, (ViewGroup) getMRootView(), false);
        mAdapter.setEmptyView(R.layout._loading_layout_error);
//        mAdapter.setEmptyView(view);

        tvAddHeader.setOnClickListener(v -> {
            mAdapter.addHeaderView(createView("头    第 " + mAdapter.getHeadersCount(), true));
        });

        tvAddFooter.setOnClickListener(v -> {
            mAdapter.addFootView(createView("尾    第 " + mAdapter.getFootersCount(), false));
        });

        tvClear.setOnClickListener(v -> {
            mAdapter.clearAllData();
        });

        tvAddData.setOnClickListener(v -> {
            mAdapter.addDataItem(new TestData(new Random().nextInt(1000),"666"));
        });


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
                ToastUtils.INSTANCE.normal("" + position);
            }

            @Override
            public void onItemChildClick(@Nullable MultiItemTypeAdapter<?> adapter, @Nullable View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (view.getId() == R.id.btnDel) {
                    ToastUtils.INSTANCE.normal("Del " + position);
                } else if (view.getId() == R.id.btnDel1) {
                    ToastUtils.INSTANCE.normal("Del2 " + position);
                }
            }
        });

        mAdapter.setDataItems(DataConfig.INSTANCE.getDataTestData());
    }

    @Override
    public void initViewData() {
        super.initViewData();


    }

    private TextView createView(String text, Boolean isHeader) {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
//        textView.setPadding(80, 80, 80, 80);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        switch (new Random().nextInt(4)){
            case 0:
                textView.setBackgroundResource(R.color.red);
                break;
            case 1:
                textView.setBackgroundResource(R.color.yellow);
                break;
            case 2:
                textView.setBackgroundResource(R.color.blue);
                break;
            default:
                textView.setBackgroundResource(R.color.black);
                break;
        }
        textView.setText(text);
        textView.setOnClickListener(v -> {
            if (isHeader) {
//                mAdapter
            } else {

            }
            ToastUtils.INSTANCE.normal(text);
        });
        return textView;
    }
}
