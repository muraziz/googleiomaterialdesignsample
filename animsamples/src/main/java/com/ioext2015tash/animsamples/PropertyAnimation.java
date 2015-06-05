package com.ioext2015tash.animsamples;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ioext2015tash.animsamples.util.ArgbEvaluator;


public class PropertyAnimation extends AppCompatActivity {

    ImageView mImg;
    TextView mTxt;

    boolean mRotateChanged = false;
    boolean mAlphaChanged = false;
    boolean mScaleChanged = false;
    boolean mTranslateChanged = false;

    boolean mTextColorChanged = false;

    int startColor = Color.BLUE;
    int endColor = Color.rgb(183, 139, 21);

    final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);

        mImg = (ImageView)findViewById(R.id.imgViewAnim);
        mTxt = (TextView)findViewById(R.id.txt);
        mTxt.setTextColor(startColor);
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

    public void animateColor(View v) {
        Animator animator;
        if (mTextColorChanged) {
            animator = ObjectAnimator.ofObject(mTxt, "textColor", argbEvaluator, endColor, startColor);
        } else {
            animator = ObjectAnimator.ofObject(mTxt, "textColor", argbEvaluator, startColor, endColor);
        }
        animator.setDuration(1000);
        animator.start();

        mTextColorChanged = !mTextColorChanged;
    }

    public void animateText(View v) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 7000);
        animator.setDuration(7000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int adjustedValue = 7000 - (int)valueAnimator.getAnimatedValue();
                int seconds = adjustedValue / 1000;
                int millis = adjustedValue % 1000;
                mTxt.setText(" -- "+seconds+":"+millis+" --");
            }
        });
        animator.start();
    }
}
