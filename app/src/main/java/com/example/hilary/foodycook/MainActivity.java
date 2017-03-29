package com.example.hilary.foodycook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button photoButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File directory;
    File file = null;
    ImageView chakulaImage;
    Uri tempUri;
    Button postItem;
    EditText title;
    EditText description;
    EditText price;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionReference = mRootRef.child("foods");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chakulaImage = (ImageView) findViewById(R.id.imageview);
        photoButton = (Button) findViewById(R.id.photoImage);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhoto();

            }
        });
        postItem = (Button) findViewById(R.id.postButton);
        postItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFoodItem();
            }
        });
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);


        file = new File(directory, "chakulloadin12.jpg");
        tempUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //chakulaImage.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
        if(file.exists()){
            int targetW = chakulaImage.getWidth();
            int targetH = chakulaImage.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bMap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);

            chakulaImage.setImageBitmap(bMap);

        }

    }

    public void postFoodItem(){
        String foodTitle;
        String foodDesc;
        int foodPrice;
        DatabaseReference reference;

        foodPrice = Integer.parseInt(price.getText().toString());
        foodTitle = title.getText().toString();
        foodDesc = description.getText().toString();

        Food food = new Food(foodTitle, foodDesc, foodPrice);

       // mConditionReference.child("one").setValue(food);

        reference = mConditionReference.push();
        reference.setValue(food);

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        encodeBitmapAndSaveToFirebase(bitmap, reference);

    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, DatabaseReference reference) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference imageUrl = reference.child("imageUrl");
        imageUrl.setValue(imageEncoded);
        Log.e("image",imageEncoded);
    }
}

