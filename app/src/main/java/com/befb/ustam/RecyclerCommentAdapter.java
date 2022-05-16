package com.befb.ustam;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.befb.ustam.databinding.RecyclerCommentRowBinding;
import com.befb.ustam.databinding.RecyclerRowBinding;
import com.befb.ustam.ui.ProfilePage.DashboardFragment;
import com.befb.ustam.ui.ProfilePage.ProfilePageActivity;
import com.befb.ustam.ui.ProfilePage.ProfilePageFragment;

import java.util.ArrayList;


public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.PostHolder> {

    private ArrayList<Comment> commentArrayList;

    public RecyclerCommentAdapter(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    class PostHolder extends RecyclerView.ViewHolder {
        RecyclerCommentRowBinding recyclerCommentRowBinding;

        public PostHolder(@NonNull RecyclerCommentRowBinding recyclerCommentRowBinding) {
            super(recyclerCommentRowBinding.getRoot());
            this.recyclerCommentRowBinding = recyclerCommentRowBinding;

        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerCommentRowBinding recyclerCommentRowBinding = RecyclerCommentRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(recyclerCommentRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.recyclerCommentRowBinding.recyclerviewRowCommentText.setText(commentArrayList.get(position).name + ": " + commentArrayList.get(position).comment);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }


}