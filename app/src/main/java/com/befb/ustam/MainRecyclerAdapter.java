package com.befb.ustam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.befb.ustam.databinding.RecyclerRowBinding;

import java.util.ArrayList;


public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.PostHolder> {

    private ArrayList<Post> postArrayList;

    public MainRecyclerAdapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    class PostHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;

        public PostHolder(@NonNull RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;

        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.recyclerRowBinding.recyclerviewRowUseremailText.setText("E-mail: "+postArrayList.get(position).email);
        holder.recyclerRowBinding.recyclerviewRowCommentText.setText(postArrayList.get(position).comment);
        holder.recyclerRowBinding.dateTextView.setText(postArrayList.get(position).date);


        //Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerviewRowImageview);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }


}