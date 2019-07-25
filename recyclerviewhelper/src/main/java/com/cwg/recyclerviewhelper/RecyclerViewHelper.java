package com.cwg.recyclerviewhelper;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class RecyclerViewHelper {
    private RecyclerView recyclerView;
    float startY = 0;
    float startX = 0;
    float endY = 0;
    float endX = 0;
    private RecyclerView.Adapter adapter;
    private float distanceY;
    private OnListener listener;

    public RecyclerViewHelper(RecyclerView recyclerView, RecyclerView.Adapter adapter, OnListener listener) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.listener = listener;
        setting();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setting() {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager == null) {
            throw new RuntimeException("请先设置LayoutManager");
        }

        if (adapter == null) {
            throw new RuntimeException("adapter");
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int childCount = adapter.getItemCount();
                if (newState == SCROLL_STATE_IDLE) {
                    //判断是否需要刷新
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

                        if (linearLayoutManager.findFirstVisibleItemPosition() == 0 && distanceY > 0) {
                            Log.i("RecyclerViewHelper", "需要下拉刷新了");
                            if (listener != null) {
                                listener.refresh();
                            }
                        } else if (linearLayoutManager.findLastVisibleItemPosition() == childCount - 1 && distanceY < 0) {
                            Log.i("RecyclerViewHelper", "需要上拉加载更多了");
                            if (listener != null) {
                                listener.loadMore();
                            }
                        }
                    }

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();//获得按下时的X坐标

                        startY = event.getY();//获得按下时的Y坐标

                        break;
                    case MotionEvent.ACTION_UP:
                        endY = event.getY();
                        endX = event.getX();
                        distanceY = endY - startY;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        float movedX = event.getX();//获得移动时候的X坐标

                        float movedY = event.getY();//获得移动时候的Y坐标

                        break;
                    default:
                        break;
                }

                return false;
            }

        });
    }


    interface OnListener {
        /**
         * 上拉加载更多回调方法
         */
        void loadMore();

        /**
         * 下拉刷新回调方法
         */
        void refresh();
    }
}
