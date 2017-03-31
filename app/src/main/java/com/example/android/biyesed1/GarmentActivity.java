package com.example.android.biyesed1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by sdekock on 2017-03-30.
 */

public class GarmentActivity extends AppCompatActivity{

    private static final int REQUEST_WRITE_PERMISSION = 1;
    private static final long TRANSLATION_DURATION = 600;




    public ImageView mImageView;
    public ConstraintLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garment_layout);
        mLayout = (ConstraintLayout) findViewById(R.id.garment_layout);

        mLayout.setOnTouchListener(new OnSwipeTouchListener(GarmentActivity.this) {

            public void onSwipeTop() {
                Toast.makeText(GarmentActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(GarmentActivity.this, "right", Toast.LENGTH_SHORT).show();
                mLayout.animate()
                        .translationXBy(mLayout.getWidth())
                        .setDuration(TRANSLATION_DURATION)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLayout.setVisibility(View.INVISIBLE);
                                Intent data = new Intent();
                                data.putExtra("Feedback","Like");
                                setResult(RESULT_OK,data);
                                finish();
                            }

                        });
            }

            public void onSwipeLeft() {
                Toast.makeText(GarmentActivity.this, "left", Toast.LENGTH_SHORT).show();
                mLayout.animate()
                        .translationXBy(-mLayout.getWidth())
                        .setDuration(TRANSLATION_DURATION)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLayout.setVisibility(View.INVISIBLE);
                                Intent data = new Intent();
                                data.putExtra("Feedback","Dislike");
                                setResult(RESULT_OK,data);
                                finish();
                            }
                        });
            }

            public void onSwipeBottom() {
                Toast.makeText(GarmentActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }





}

