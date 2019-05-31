package com.endorocket.gallery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.endorocket.gallery.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    // vars
    private Context mContext;
    private ArrayList<String> mImages;
    private OnImageListener mOnImageListener;


    public RecyclerViewAdapter(Context context, ArrayList<String> images, OnImageListener onImageListener) {
        mContext = context;
        mImages = images;
        mOnImageListener = onImageListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);

        ViewHolder holder = new ViewHolder(view, mOnImageListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .placeholder(R.color.colorPrimaryDark)
                .load(mImages.get(position))
                .into(viewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // widgets
        private ImageView mImageView;

        // vars
        private OnImageListener mOnImageListener;

        ViewHolder(@NonNull View itemView, OnImageListener onImageListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image);

            mImageView.setMaxHeight(mImageView.getWidth());
//            mImageView.setMinimumHeight(mImageView.getMinimumWidth());

            Log.d(TAG, "ViewHolder: mImageView.getWidth(): " + mImageView.getWidth());

            mOnImageListener = onImageListener;

            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnImageListener.onImageClick(getAdapterPosition());
        }
    }

    public interface OnImageListener {
        void onImageClick(int position);
    }
}
