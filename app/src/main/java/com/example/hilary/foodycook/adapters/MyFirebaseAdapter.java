package com.example.hilary.foodycook.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hilary.foodycook.Food;
import com.example.hilary.foodycook.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by hilary on 3/31/17.
 */

public class MyFirebaseAdapter extends FirebaseRecyclerAdapter<MyFirebaseAdapter.ViewHolder, Food> {
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference imageReference;
    Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        ImageView foodImage;
        TextView price;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.foodTitle);
            description = (TextView) view.findViewById(R.id.description);
            price = (TextView) view.findViewById(R.id.price);
            foodImage = (ImageView) view.findViewById(R.id.foodImage);
        }
    }

    public MyFirebaseAdapter(Query query, Class<Food> foodClass, @Nullable ArrayList<Food> items,
                             @Nullable ArrayList<String> keys, Context context) {
        super(query, items, keys);
        this.context = context;
    }

    @Override public MyFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false);

        return new ViewHolder(view);
    }



    @Override public void onBindViewHolder(MyFirebaseAdapter.ViewHolder holder, int position) {
        Food item = getItem(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.price.setText(String.valueOf(item.getPrice()));
        imageReference =  storageReference.child("foodImages/"+item.getImageUrl());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(imageReference)
                .into(holder.foodImage);
    }

    @Override protected void itemAdded(Food item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(Food oldItem, Food newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(Food item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(Food item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}