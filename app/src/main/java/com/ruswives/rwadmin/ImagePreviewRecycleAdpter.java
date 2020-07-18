package com.ruswives.rwadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruswives.rwadmin.model.CoverImageData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImagePreviewRecycleAdpter  extends RecyclerView.Adapter<ImagePreviewRecycleAdpter.MyViewHolder> {
    List<CoverImageData> coverImageDataList;
    ImagePreviewRecylerItemListner imagePreviewRecylerItemListner;

    public ImagePreviewRecycleAdpter(List<CoverImageData> coverImageDataList) {
        this.coverImageDataList = coverImageDataList;
    }

    public ImagePreviewRecycleAdpter(List<CoverImageData> coverImageDataList, ImagePreviewRecylerItemListner imagePreviewRecylerItemListner) {
        this.coverImageDataList = coverImageDataList;
        this.imagePreviewRecylerItemListner = imagePreviewRecylerItemListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cutom_image_preview,parent,false)
                ,imagePreviewRecylerItemListner);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.imageView.setImageBitmap(coverImageDataList.get(position).getBitmap());
        holder.textView.setText(coverImageDataList.get(position).getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemListner.onItemClick(coverImageDataList.get(position).getBitmap());
            }
        });
    }

    @Override
    public int getItemCount() {
        return coverImageDataList!=null?coverImageDataList.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImagePreviewRecylerItemListner itemListner;
        public MyViewHolder(@NonNull View itemView,ImagePreviewRecylerItemListner imagePreviewRecylerItemListner) {
            super(itemView);
            imageView=itemView.findViewById(R.id.custom_image_cover_imageview);
            textView=itemView.findViewById(R.id.custom_image_cover_textview);
            itemListner=imagePreviewRecylerItemListner;
        }
    }
}
