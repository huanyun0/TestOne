package com.example.testone.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.testone.Adapter.QuestionsAdapter;
import com.example.testone.Model.Responses;
import com.example.testone.Model.SimpleQuestion;
import com.example.testone.Model.User;
import com.example.testone.Net.OkHttpHelper;
import com.example.testone.Net.RequestCode;
import com.example.testone.Net.ServerHelper;
import com.example.testone.Net.TaskRunner;
import com.example.testone.R;
import com.example.testone.Util.SPUtil;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class HomeFragment extends Fragment {
    @BindView(R.id.questions)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

//    private List<SimpleQuestion> list = new ArrayList<SimpleQuestion>();
    private QuestionsAdapter adapter;
    SPUtil spUtil;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);

        List<SimpleQuestion> list = new ArrayList<SimpleQuestion>();
//        for(int i=0;i<1000;i++){
//            SimpleQuestion question = new SimpleQuestion();
//            question.setTitle("网络流行语是否被禁止进入高考语文作文？为什么？");
//            question.setContent("王郑宁：关于阅卷老师的年龄，有不少人告诉我他们城市是研究生批高考作文。请恕我和孤陋寡闻。因为我参与过，了解过的情况，魔都的语文作文阅卷一直都是：普通评卷...");
//            question.setId(i+101);
//            question.setPraise(i+1);
//            question.setComment((int)(Math.random()*10000));
//            list.add(question);
//        }
        Context context = getContext();
        if (context != null) {
            adapter = new QuestionsAdapter(context, list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
        }

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            // 下拉刷新
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                try {
                    String url= ServerHelper.getUrlSimpleQuestion(adapter.getId(true),1);
                    TaskRunner.execute(()->{
                        Type type = new TypeToken<Responses<List<SimpleQuestion>>>() {
                        }.getType();
                        Responses<List<SimpleQuestion>> response = OkHttpHelper.get(url, type);
                        getActivity().runOnUiThread(()->{
                            if(response!=null){
                                switch (response.getCode()){
                                    case RequestCode.SUCCESS:
                                        update(response.getObject(),0);
                                        break;
                                    case RequestCode.NOT_FOUND:
                                        Toast.makeText(context,"没有新消息",Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    });
                }
                catch (Exception e){
                    Log.d("下拉刷新","失败");
                }
                refreshLayout.finishRefresh(500);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            //上拉加载更多
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                try {
                    String url= ServerHelper.getUrlSimpleQuestion(adapter.getId(false),0);
                    TaskRunner.execute(()->{
                        Type type = new TypeToken<Responses<List<SimpleQuestion>>>() {
                        }.getType();
                        Responses<List<SimpleQuestion>> response = OkHttpHelper.get(url, type);
                        getActivity().runOnUiThread(()->{
                            if(response!=null){
                                switch (response.getCode()){
                                    case RequestCode.SUCCESS:
                                        update(response.getObject(),1);
                                        break;
                                    case RequestCode.NOT_FOUND:
                                        Toast.makeText(context,"已经到底了",Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    });
                }
                catch (Exception e){
                    Log.d("上拉刷新","失败");
                }
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
            }
        });

        refreshLayout.autoRefresh(); // 刚进入界面自动下拉刷新

        return view;
    }

    public void update(List<SimpleQuestion> list,int type){
        adapter.addItem(list,type);
    }

    @OnClick(R.id.edit)
    void edit() {
        //startActivity(new Intent(getContext(), EditorActivity.class));
    }

}
