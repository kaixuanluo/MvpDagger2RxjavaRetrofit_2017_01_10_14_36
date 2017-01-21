package com.example.admin.mvpdagger2rxjavaretrofit_2017_01_10_14_36.common.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.mvpdagger2rxjavaretrofit_2017_01_10_14_36.R;
import com.example.admin.mvpdagger2rxjavaretrofit_2017_01_10_14_36.common.bean.BaseListResult;
import com.example.admin.mvpdagger2rxjavaretrofit_2017_01_10_14_36.common.presenter.BaseListPresenter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

/**
 * 描述说明  <br/>
 * Author : luokaixuan <br/>
 * CreateDate : 2016/10/14 18:22 <br/>
 * Modified : luokaixuan <br/>
 * ModifiedDate : 2016/10/14 18:22 <br/>
 * Email : 1005949566@qq.com <br/>
 * Version 1.0
 */
public abstract class BaseStickListFragment<ITEM, DATA extends BaseListResult<ITEM>,
        P extends BaseListPresenter<ITEM, DATA>, HOLDER extends ViewHolder>
        extends BaseListFragment<ITEM, DATA, P, HOLDER> {

    StickAdapter mStickAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStickAdapter = new StickAdapter();

        super.onCreate(savedInstanceState);

        final StickyRecyclerHeadersDecoration decor = new StickyRecyclerHeadersDecoration(mStickAdapter);
        getRcv().addItemDecoration(decor);

        //添加头部点击监听
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(getRcv(), decor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        Toast.makeText(BaseStickListFragment.this.getContext(), "Header position: " + position + ", id: " + headerId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        getRcv().addOnItemTouchListener(touchListener);

        //添加头部刷新
        mStickAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                decor.invalidateHeaders();
            }
        });
    }

    protected abstract long initStickHeaderId(int position, ITEM item);
    protected abstract void bindStickHeardViewHolder(TextView tvHeard, ITEM item, int position);

    public class StickAdapter
            extends BaseListAdapter2
            implements StickyRecyclerHeadersAdapter<HeardHolderItem> {

        @Override
        public long getHeaderId(int position) {
            ITEM item = null;
            if (getBaseList() != null && getBaseList().size() > 0) {
                item = getBaseList().get(position);
            } else {
                Log.e(" getHeaderId ", " getHeaderId " + position);
            }
            return initStickHeaderId(position, item);
        }

        @Override
        public HeardHolderItem onCreateHeaderViewHolder(ViewGroup parent) {
            return new HeardHolderItem(
                    LayoutInflater.from(BaseStickListFragment.this.getContext())
                            .inflate(R.layout.item_flow_progress_heard, null));
        }

        @Override
        public void onBindHeaderViewHolder(HeardHolderItem holder, int position) {
            if (getBaseList() != null && getBaseList().size() > 0) {
                bindStickHeardViewHolder(((HeardHolderItem) holder).tvProgress, getBaseList().get(position), position);
            } else {
                Log.e("  HeaderViewHolder ", " onBindHeaderViewHolder " + position);
            }
        }

//        @Override
//        public int getItemViewType(int position) {
//            return TYPE_ITEM;
//        }
    }

    class HeardHolderItem extends ViewHolder {
        TextView tvProgress;
        public HeardHolderItem(View itemView) {
            super(itemView);

            tvProgress = (TextView) itemView.findViewById(R.id.flow_prg_heard);
        }
    }

    @Override
    protected void setRecyclerViewAdapter(Adapter adapter) {
        super.setRecyclerViewAdapter(mStickAdapter);
    }

}
