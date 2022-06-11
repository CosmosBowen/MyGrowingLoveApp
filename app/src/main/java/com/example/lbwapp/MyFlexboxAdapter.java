package com.example.lbwapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;//import android.support.v7.widget.RecyclerView

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;
import java.util.Random;

public class MyFlexboxAdapter extends RecyclerView.Adapter<MyFlexboxAdapter.MyViewHolder>{
    private List<CardItemEntity> list;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image_card;
        MyTextView title_card;
        MyTextView label_card;

        public MyViewHolder(View itemView) {
            super(itemView);
            image_card=(ImageView)itemView.findViewById(R.id.small_card_image);
            title_card=(MyTextView)itemView.findViewById(R.id.small_card_title);
            label_card=(MyTextView)itemView.findViewById(R.id.small_card_label);
        }
    }

    public MyFlexboxAdapter(Context context, List<CardItemEntity> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyFlexboxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.small_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyFlexboxAdapter.MyViewHolder myHolder, final int position) {
        final CardItemEntity cardItemEntity=list.get(position);
        final int stored_position=position;

        //用户照片
        ImageView mImageView=myHolder.image_card;

        //生成随机照片高度random
//        Random random=new Random();
//        int height=random.nextInt(401)+300;
//        mImageView.setMaxHeight(height);
        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(cardItemEntity.getImagePath_card()).apply(requestOptions).into(mImageView);

        myHolder.image_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //全局！
                MyApplication.STATE=3;
                MyApplication.position=stored_position;

                Toast.makeText(view.getContext(), "label: " + cardItemEntity.getLabel_card(), Toast.LENGTH_SHORT).show();
                Intent intent=null;
                intent=new Intent(context,PhotoPortrait.class);
                intent.putExtra("ImagePath",cardItemEntity.getImagePath_card());
//                intent.putExtra("fromActivity","MyFlexboxAdapter");
                context.startActivity(intent);
            }
        });

        String label=cardItemEntity.getLabel_card();
        myHolder.label_card.setText(label);

        String title=cardItemEntity.getTitle_card();
        if(title.equals("")){
            myHolder.title_card.setVisibility(View.INVISIBLE);
            myHolder.title_card.setClickable(false);
//            myHolder.title_card.setText("[无标题]");
        }else {
            myHolder.title_card.setVisibility(View.VISIBLE);
            myHolder.title_card.setClickable(true);
            myHolder.title_card.setText(title);
        }

        //没啥用啊
//        ViewGroup.LayoutParams lp=mImageView.getLayoutParams();
//        if(lp instanceof FlexboxLayoutManager.LayoutParams){
//            FlexboxLayoutManager.LayoutParams flexboxLp=(FlexboxLayoutManager.LayoutParams)mImageView.getLayoutParams();
//            flexboxLp.setFlexGrow(1.0f);
//        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}
