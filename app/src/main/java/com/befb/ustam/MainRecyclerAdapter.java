package com.befb.ustam;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.befb.ustam.databinding.RecyclerRowBinding;
import com.befb.ustam.ui.ProfilePage.DashboardFragment;
import com.befb.ustam.ui.ProfilePage.ProfilePageActivity;
import com.befb.ustam.ui.ProfilePage.ProfilePageFragment;

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
        holder.recyclerRowBinding.recyclerviewRowCommentText.setText("Açıklama: "+postArrayList.get(position).comment);
        holder.recyclerRowBinding.dateTextView.setText(postArrayList.get(position).date);
        holder.recyclerRowBinding.cityTextView.setText("Şehir: " + postArrayList.get(position).city);
        holder.recyclerRowBinding.ConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity, ProfilePageActivity.class);
                intent.putExtra("expertUUID", postArrayList.get(position).expertUUID);
                System.out.println("ZORTRT:  "  +postArrayList.get(position).expertUUID);
                activity.startActivity(intent);
            }
        });
        //Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerviewRowImageview);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }


}