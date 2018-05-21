package com.example.stmak.loadimages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView arrow_back;
    static private Button add_avatar;
    static final int GALLERY_REQUEST = 16;
    static private boolean[] arr_lock;

    // Grid photos
    private GridView customGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        addListener();

        // Grid photos
        customGrid = (GridView) findViewById(R.id.grid_photos);
        customGrid.setAdapter(new CustomGridAdapter(this));
        arr_lock = new boolean[customGrid.getCount()];

        // clickListener for 0 item from gridView
        customGrid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(view.findViewById(R.id.item_image_view).getVisibility() == View.VISIBLE){
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, position);
                        }
                    }
                }
        );

    }

    public void addListener() {
        arrow_back = (ImageView) findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        // add avatar
        add_avatar = (Button) findViewById(R.id.main_photo);
        add_avatar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                    }
                }
        );
    }

    // Hell code
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        RoundedBitmapDrawable roundedBitmapDrawable = null;
        ImageView imageView = (ImageView) findViewById(R.id.avatar);

        if (resultCode == RESULT_OK && imageReturnedIntent != null) {
            Uri selectedImage = imageReturnedIntent.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            for(; width > 1000 && height > 1000; ){
                width -= width/2;
                height -= height/2;
            }

            Bitmap bitmapHalf = Bitmap.createScaledBitmap(bitmap, width, height, false);

            if (width > height) {
                int newwidth = (width - height) / 2;
                bitmapHalf = Bitmap.createBitmap(bitmapHalf, newwidth, 0, height, height);
            } else if (height > width) {
                int newheight = (height - width) / 2;
                bitmapHalf = Bitmap.createBitmap(bitmapHalf, 0, newheight, width, width);
            }
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapHalf);
            roundedBitmapDrawable.setCircular(true);

        if(requestCode == GALLERY_REQUEST) {

            imageView.setImageDrawable(roundedBitmapDrawable);

            add_avatar.setClickable(false);
            ImageButton add_avatar_but = (ImageButton) findViewById(R.id.change_avatar);
            add_avatar_but.setVisibility(View.VISIBLE);
            add_avatar_but.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                        }
                    }
            );
        }
        else if(requestCode >= 0 && requestCode <= 5){
            View current = (View)customGrid.getChildAt(requestCode);

            ImageView iv = (ImageView)current.findViewById(R.id.item_photo_view);
            iv.setImageDrawable(roundedBitmapDrawable);

            final ImageView changePhoto = (ImageView)current.findViewById(R.id.change_photo);
            changePhoto.setVisibility(View.VISIBLE);
            changePhoto.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!arr_lock[requestCode]){
                                v.setBackgroundResource(R.drawable.change_photo_item_active);
                                changePhoto.setImageResource(R.drawable.lock_photo);
                                arr_lock[requestCode] = true;
                            }
                            else if(arr_lock[requestCode]){
                                v.setBackgroundResource(R.drawable.change_photo_item_noactive);
                                changePhoto.setImageResource(R.drawable.unlock_photo);
                                arr_lock[requestCode] = false;
                            }
                        }
                    }
            );

            current.setOnClickListener(null);

            // set visibility next item
            if(requestCode < 5){
                View next = (View)customGrid.getChildAt(requestCode+1);
                next.findViewById(R.id.item_image_view).setVisibility(View.VISIBLE);
                next.findViewById(R.id.item_button_view).setVisibility(View.VISIBLE);
                next.findViewById(R.id.item_photo_view).setVisibility(View.VISIBLE);
                next.setClickable(true);
                next.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, requestCode+1);
                            }
                        }
                );
            }
            }
        }
    }
}
