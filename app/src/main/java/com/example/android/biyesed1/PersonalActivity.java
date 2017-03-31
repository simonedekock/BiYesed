package com.example.android.biyesed1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sdekock on 2017-03-30.
 */

public class PersonalActivity extends AppCompatActivity{

    private static final int REQUEST_WRITE_PERMISSION = 1;
    private static final long TRANSLATION_DURATION = 600;

    private TextView mTextMessage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CHOOSE_CONTACT = 2;

    public ImageView mImageView;
    public FrameLayout mLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentToHandleContactsRequestActivity = new Intent(PersonalActivity.this, ContactsRequestActivity.class);
                    startActivity(intentToHandleContactsRequestActivity);
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent intentToHandlePublicsActivity = new Intent(PersonalActivity.this, MainActivity.class);
                    startActivity(intentToHandlePublicsActivity);
                    return true;
            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         mLayout = (FrameLayout) findViewById(R.id.content);

        mLayout.setOnTouchListener(new OnSwipeTouchListener(PersonalActivity.this) {

            public void onSwipeTop() {
                Toast.makeText(PersonalActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(PersonalActivity.this, "right", Toast.LENGTH_SHORT).show();
                mLayout.animate()
                        .translationXBy(mLayout.getWidth())
                        .setDuration(TRANSLATION_DURATION)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLayout.setVisibility(View.INVISIBLE);

                            }
                        });
            }

            public void onSwipeLeft() {
                Toast.makeText(PersonalActivity.this, "left", Toast.LENGTH_SHORT).show();
                mLayout.animate()
                        .translationXBy(-mLayout.getWidth())
                        .setDuration(TRANSLATION_DURATION)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLayout.setVisibility(View.INVISIBLE);
                                Intent intentToHandleContactsActivity = new Intent(PersonalActivity.this, ContactsActivity.class);
                                startActivityForResult(intentToHandleContactsActivity, RESULT_CHOOSE_CONTACT);
                            }
                        });
            }

            public void onSwipeBottom() {
                Toast.makeText(PersonalActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        FloatingActionButton buttonLoadImage = (FloatingActionButton) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                requestPermission();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

           if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
               Uri selectedImage = data.getData();
               String[] filePathColumn = {MediaStore.Images.Media.DATA};

               Cursor cursor = getContentResolver().query(selectedImage,
                       filePathColumn, null, null, null);
               cursor.moveToFirst();

               int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
               String picturePath = cursor.getString(columnIndex);
               cursor.close();

               mImageView = (ImageView) findViewById(R.id.imageView);
               mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
               mLayout.setVisibility(View.VISIBLE);
           }

        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {
            String contactName = data.getStringExtra("Contact");
            Toast.makeText(PersonalActivity.this, "contact chosen: " + contactName, Toast.LENGTH_SHORT).show();
            Intent intentToReturnToPersonalActivityDefault = new Intent(PersonalActivity.this, PersonalActivity.class);
            startActivity(intentToReturnToPersonalActivityDefault);

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    public void openFilePicker(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}

