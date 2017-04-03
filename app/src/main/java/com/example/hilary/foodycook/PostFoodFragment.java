package com.example.hilary.foodycook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by hilary on 3/29/17.
 */

public class PostFoodFragment extends Fragment{
    View rootView;
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
    DatabaseReference mRootRef;
    DatabaseReference mFoodReference;
    MainActivity mainActivity;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        mFoodReference = mainActivity.mRootRef.child("foods");

        rootView = inflater.inflate(R.layout.post_food_fragment, container, false);

        chakulaImage = (ImageView) rootView.findViewById(R.id.imageview);
        photoButton = (Button) rootView.findViewById(R.id.photoImage);
        title = (EditText) rootView.findViewById(R.id.title);
        description = (EditText) rootView.findViewById(R.id.description);
        price = (EditText) rootView.findViewById(R.id.price);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhoto();

            }
        });
        postItem = (Button) rootView.findViewById(R.id.postButton);
        postItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFoodItem();
            }
        });
        return rootView;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
        String imageUrl;
        imageUrl = uploadImage();
        foodPrice = Integer.parseInt(price.getText().toString());
        foodTitle = title.getText().toString();
        foodDesc = description.getText().toString();

        Food food = new Food(foodTitle, foodDesc, foodPrice, imageUrl);

       // mConditionReference.child("one").setValue(food);

        reference = mFoodReference.push();
        reference.setValue(food);

        //Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        //encodeBitmapAndSaveToFirebase(bitmap, reference);

    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap, DatabaseReference reference) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference imageUrl = reference.child("imageUrl");
        imageUrl.setValue(imageEncoded);

    }

    public String uploadImage(){

        String fileName = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        StorageReference imageRef = storageReference.child("foodImages/"+fileName);
        if(file.exists()){
            Uri uriFile = Uri.fromFile(file);
            imageRef.putFile(uriFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "image uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            //and displaying error message
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        return fileName;
    }
}



