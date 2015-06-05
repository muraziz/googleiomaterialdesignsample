package com.ioext2015tash.animsamples;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class PropertyAnimation extends AppCompatActivity {

    ImageView mImg;

    boolean mRotateChanged = false;
    boolean mAlphaChanged = false;
    boolean mScaleChanged = false;
    boolean mTranslateChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);

        mImg = (ImageView)findViewById(R.id.imgViewAnim);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_property_animation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void triggerRotate(View v) {
        Animator animator;
        if (mRotateChanged) {
            animator = AnimatorInflater.loadAnimator(this, R.animator.rotate_reverse);
        } else {
            animator = AnimatorInflater.loadAnimator(this, R.animator.rotate);
        }
        animator.setTarget(mImg);
        animator.start();

        mRotateChanged = !mRotateChanged;
    }

    public void triggerAlpha(View v) {
        Animator animator;
        if (mAlphaChanged) {
            animator = AnimatorInflater.loadAnimator(this, R.animator.alpha_reverse);
        } else {
            animator = AnimatorInflater.loadAnimator(this, R.animator.alpha);
        }
        animator.setTarget(mImg);
        animator.start();

        mAlphaChanged = !mAlphaChanged;
    }

    public void triggerTranslate(View v) {
        Animator animator;
        if (mTranslateChanged) {
            animator = AnimatorInflater.loadAnimator(this, R.animator.translate_reverse);
        } else {
            animator = AnimatorInflater.loadAnimator(this, R.animator.translate);
        }
        animator.setTarget(mImg);
        animator.start();

        mTranslateChanged = !mTranslateChanged;
    }

    public void triggerScale(View v) {
        Animator animator;
        if (mScaleChanged) {
            animator = AnimatorInflater.loadAnimator(this, R.animator.scale_reverse);
        } else {
            animator = AnimatorInflater.loadAnimator(this, R.animator.scale);
        }
        animator.setTarget(mImg);
        animator.start();

        mScaleChanged = !mScaleChanged;
    }

    public void imageClicked(View v) {
        Toast.makeText(this, "IMAGE IS CLICKED!", Toast.LENGTH_SHORT).show();
    }
}
