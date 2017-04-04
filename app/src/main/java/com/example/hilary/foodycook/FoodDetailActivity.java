package com.example.hilary.foodycook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

/**
 * Created by hilary on 4/4/17.
 */
public class FoodDetailActivity extends AppCompatActivity {
    Food food;
    ImageView foodDetailImage;
    TextView ratings;
    TextView description;
    Button buyButton;
    Button requestReceipe;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference imageReference;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail_activity);
        foodDetailImage = (ImageView) findViewById(R.id.foodDetailImage);
        description = (TextView) findViewById(R.id.longDescription);
        buyButton = (Button) findViewById(R.id.buyButton);
        requestReceipe = (Button) findViewById(R.id.requestReceipe);
        title = (TextView) findViewById(R.id.detailTitle);


        food =  Parcels.unwrap(getIntent().getParcelableExtra("food"));

        imageReference =  storageReference.child("foodImages/"+food.getImageUrl());

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(imageReference)
                .into(foodDetailImage);
        description.setText(food.getDescription());
        title.setText(food.title);


    }

}
