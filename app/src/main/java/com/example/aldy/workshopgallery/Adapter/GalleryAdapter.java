package com.example.aldy.workshopgallery.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.aldy.workshopgallery.Model.GalleryModel;
import com.example.aldy.workshopgallery.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DBSS014 on 4/4/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<GalleryModel> listModels;
    private Context context;

    FirebaseStorage mstorage = FirebaseStorage.getInstance();
    final StorageReference mstorageReference = mstorage.getReference();

    public GalleryAdapter(List<GalleryModel> listModels, Context context) {
        this.listModels = listModels;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, caption;
        ImageView foto;

        public ViewHolder (View v){
            super(v);
            nama = v.findViewById(R.id.listitem_text1);
            caption = v.findViewById(R.id.listitem_text2);
            foto = v.findViewById(R.id.listitem_image);
        }
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        GalleryModel listModel = listModels.get(position);
        holder.nama.setText(listModel.getNama());
        holder.caption.setText(listModel.getCaption());
        holder.foto.setImageResource(R.mipmap.ic_launcher);
        StorageReference gallery = mstorageReference.child("gallery/"+listModel.getPhoto());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(gallery)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }
}
