package com.hitachi.movieapp.presentation.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }
    public void onBind(int position) {}
}