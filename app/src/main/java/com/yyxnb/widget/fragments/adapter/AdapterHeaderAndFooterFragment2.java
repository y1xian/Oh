package com.yyxnb.widget.fragments.adapter;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.ItemDecoration;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.adapter.ext.RecyclerViewExtKt;
import com.yyxnb.adapter.rv.BaseState;
import com.yyxnb.adapter.rv.BaseRecyclerView;
import com.yyxnb.arch.base.BaseFragment;
import com.yyxnb.utils.AppConfig;
import com.yyxnb.utils.ToastUtils;
import com.yyxnb.utils.log.LogUtils;
import com.yyxnb.widget.R;
import com.yyxnb.widget.adapter.NetWorkListAdapter;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.config.DataConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

/**
 * 状态视图 头 + 底.
 */
public class AdapterHeaderAndFooterFragment2 extends BaseFragment {

    private int page = 1;
    private NetWorkListAdapter mAdapter;
    private TextView tvAddHeader;
    private TextView tvAddFooter;
    private TextView tvClear;
    private TextView tvAddData;
    private BaseRecyclerView mRecyclerView;

    public static AdapterHeaderAndFooterFragment2 newInstance() {

        Bundle args = new Bundle();

        AdapterHeaderAndFooterFragment2 fragment = new AdapterHeaderAndFooterFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_adapter_header_and_footer2;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        tvAddHeader = findViewById(R.id.tvAddHeader);
        tvAddFooter = findViewById(R.id.tvAddFooter);
        tvClear = findViewById(R.id.tvClear);
        tvAddData = findViewById(R.id.tvAddData);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mAdapter = new NetWorkListAdapter();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setAdapter(mAdapter);
        ItemDecoration decoration = new ItemDecoration(mContext);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        decoration.setDrawBorderTopAndBottom(true);
        decoration.setDrawBorderLeftAndRight(true);
//        decoration.setDividerHeight(20);
//        decoration.setDividerWidth(20);
        RecyclerViewExtKt.wrapLinear(mRecyclerView, mContext,decoration).setAdapter(mAdapter);


        View view = LayoutInflater.from(mContext).inflate(R.layout._loading_layout_error, (ViewGroup) getMRootView(), false);
//        mAdapter.setEmptyView(R.layout._loading_layout_error);
//        mAdapter.setEmptyView(view);
//        view.setOnClickListener(v -> {
//            ToastUtils.INSTANCE.normal("213");
//        });

        tvAddHeader.setOnClickListener(v -> {
            mAdapter.addHeaderView(createView("头    第 " + mAdapter.getHeadersCount(), true));
        });

        tvAddFooter.setOnClickListener(v -> {
            mAdapter.addFooterView(createView("尾    第 " + mAdapter.getFootersCount(), false));
        });

        tvClear.setOnClickListener(v -> {
//            mRecyclerView.removeAllHeaderView();
//            mRecyclerView.removeAllFooterView();
            mAdapter.clearData();
            mRecyclerView.setStateType(BaseState.EMPTY);
        });

        tvAddData.setOnClickListener(v -> {

//            mAdapter.addDataItem(0, new TestData(new Random().nextInt(100), "666"));
            mAdapter.addDataItem(DataConfig.INSTANCE.getDataTestData2());
            LogUtils.INSTANCE.w("size " + mAdapter.getDataCount());
            mAdapter.notifyDataSetChanged();
        });

        mRecyclerView.setOnRefreshListener(() -> {
            page = 1;
            setData(DataConfig.INSTANCE.getDataTestData2());
        }, 500);

        mRecyclerView.setOnLoadMoreListener(() -> {

//            if (page % 2 == 0) {
//                mRecyclerView.loadMoreFail();
//            } else if (page == 5) {
//                mRecyclerView.loadMoreEnd();
//            } else {
//                mRecyclerView.loadMoreComplete();
//            }

            page++;
            setData(DataConfig.INSTANCE.getDataTestData2());

        }, 500);

//        mAdapter.setOnItemChildClickListener(new IOnItemChildClick() {
//            @Override
//            public void onItemChildClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
//                AppConfig.INSTANCE.toast("Add " + position);
//            }
//        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                ToastUtils.INSTANCE.normal("" + position);
            }

            @Override
            public void onItemChildClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                super.onItemChildClick(view, holder, position);
                if (view.getId() == R.id.btnAdd) {
                    mAdapter.addDataItem(position + 1, new TestData(new Random().nextInt(100), "666"));
                    AppConfig.INSTANCE.toast("Add " + position);
                } else if (view.getId() == R.id.btnTop) {
                    mAdapter.changeDataItem(0, mAdapter.getData().get(position), true);
                    AppConfig.INSTANCE.toast("btnTop " + position);
                } else if (view.getId() == R.id.btnDelete) {
                    mAdapter.removeDataItem(position);
                    AppConfig.INSTANCE.toast("btnDelete " + position);
                } else if (view.getId() == R.id.mLinearLayout) {
                    AppConfig.INSTANCE.toast("mLinearLayout " + position);
                }
            }

            @Override
            public boolean onItemChildLongClick(@NotNull View view, @NotNull BaseViewHolder holder, int position) {
                AppConfig.INSTANCE.toast("Add lllllllllllll" + position);
                return true;
            }
        });

//        mAdapter.addHeaderView(createView("头    第 " + mAdapter.getHeadersCount(), true));

        mRecyclerView.setStateType(BaseState.LOADING);

        new Handler().postDelayed(() -> {
//            mRecyclerView.setStateType(BaseState.EMPTY);
//            mAdapter.setDataItems(DataConfig.INSTANCE.getDataTestData2());
            setData(DataConfig.INSTANCE.getDataTestData2());
//            mAdapter.setDataItems(new ArrayList<>());
        }, 1000);


    }

    @Override
    public void initViewData() {
        super.initViewData();


    }

    public void setData(ArrayList<TestData> data) {
        RecyclerViewExtKt.wrapData(mRecyclerView, page, data);
    }

    private TextView createView(String text, Boolean isHeader) {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
//        textView.setPadding(80, 80, 80, 80);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        switch (new Random().nextInt(4)) {
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
            AppConfig.INSTANCE.toast(text);
        });
        return textView;
    }
}
