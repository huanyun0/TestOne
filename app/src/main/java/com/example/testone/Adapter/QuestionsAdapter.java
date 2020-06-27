package com.example.testone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.testone.Model.Question;
import com.example.testone.Model.SimpleQuestion;
import com.example.testone.R;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<SimpleQuestion> list;
    private Context context;

    public QuestionsAdapter(Context context, List<SimpleQuestion> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.question, viewGroup, false);
//        Log.d("onCreateViewHolder"+i,view.toString());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SimpleQuestion question = list.get(i);
        viewHolder.title.setText(question.getTitle());
        viewHolder.content.setText(question.getContent());
        viewHolder.info.setText(question.getPraise()+"赞 · "+question.getComment()+"评论");
//        Log.d("onBindViewHolder"+question.getId(),"标题："+question.getTitle()+"\n内容:"+question.getContent()+"\n"+question.getPraise()+"赞 · "+question.getComment()+"评论");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.info)
        TextView info;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.content)
        void answerDetail() {
            //context.startActivity(new Intent(context, AnswerActivity.class));
        }
    }
    public void addItem(List<SimpleQuestion> newList,int type){
        int position;
        if(type==0){
            List<SimpleQuestion> oldList=new ArrayList<>(list);
            list.clear();
            list.addAll(newList);
            position=list.size();
            list.addAll(position,oldList);
            notifyDataSetChanged();
        }
        else {
            position = list.size();
            list.addAll(position, newList);
            notifyItemInserted(position);
        }
    }
    public int getId(boolean type){
        if(list.size()==0){
            return 3;
        }
        if(type) {
            return list.get(0).getId();
        }
        else {
            return list.get(list.size()-1).getId();
        }
    }
}