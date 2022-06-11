package com.example.lbwapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAda extends RecyclerView.Adapter implements View.OnClickListener{
    private List<ItemEntity> list;
    private Context context;
    private LayoutInflater inflater;

    public MyAda(Context context, List<ItemEntity> list) {
        this.context = context;
        this.list = list;
        //加载布局管理
        //从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了
        inflater = LayoutInflater.from(context);
    }
    private OnItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            int tag= (int) ((TextView) v.findViewById(R.id.content)).getTag();
            mOnItemClickListener.onItemClick(v,tag);
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item, null);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;
        ItemEntity itemEntity = list.get(position);
        holder1.content.setText(itemEntity.getContent());
        holder1.imageView.setImageResource(itemEntity.getImg());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder1.content.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView content;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
