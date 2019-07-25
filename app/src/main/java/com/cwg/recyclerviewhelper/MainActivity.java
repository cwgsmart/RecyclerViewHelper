package com.cwg.recyclerviewhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewHelper.OnListener {
    private TextAdapter adapter;
    private List<String> dataList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        adapter = new TextAdapter(R.layout.item_text, dataList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        RecyclerViewHelper recyclerViewHelper = new RecyclerViewHelper(recyclerView, adapter, this);
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            dataList.add(String.valueOf(i));
        }
    }

    @Override
    public void loadMore() {
        TextView textView = new TextView(this);
        textView.setText("我是上拉出来的数据");
        adapter.addFooterView(textView);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void refresh() {
        TextView textView = new TextView(this);
        textView.setText("我是下拉刷新出来的数据");
        adapter.addHeaderView(textView);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
    }
}
