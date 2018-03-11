package com.example.yc.lab11.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yc.lab11.model.Github;
import com.example.yc.lab11.R;

import java.util.List;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.ViewHolder>{

    private List<Github> cardList;
    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        TextView Login;
        TextView Id;
        TextView Blog;

        public ViewHolder(View view) {
            super(view);
            cardView = view;
            Login = (TextView) view.findViewById(R.id.login);
            Id = (TextView) view.findViewById(R.id.id);
            Blog = (TextView) view.findViewById(R.id.blog);
        }
    }

    public cardAdapter(List<Github> CardList) {
        cardList = CardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
            }
        });
        return holder;
    }

    public interface OnItemClickListener {//回调接口
        void onClick(int position);//单击
        void onLongClick(int position);//长按
    }

    //定义这个接口的set方法，便于调用

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Github users = cardList.get(position);
        holder.Login.setText(users.getLogin());
        holder.Id.setText(users.getId());
        holder.Blog.setText(users.getBlog());

        //设置点击和长按事件
        if (mOnItemClickListener != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

}