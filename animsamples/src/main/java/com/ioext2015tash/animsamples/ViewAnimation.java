package com.ioext2015tash.animsamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class ViewAnimation extends AppCompatActivity {

    ImageView mImg;
    boolean mFillAfter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);

        mImg = (ImageView)findViewById(R.id.imgViewAnim);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_animation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fill_enabled) {
            item.setChecked(!item.isChecked());
            mFillAfter = !mFillAfter;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void triggerRotate(View v) {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        rotate.setFillEnabled(mFillAfter);
        rotate.setFillAfter(mFillAfter);

        mImg.startAnimation(rotate);
    }

    public void triggerAlpha(View v) {
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        alpha.setFillEnabled(mFillAfter);
        alpha.setFillAfter(mFillAfter);

        mImg.startAnimation(alpha);
    }

    public void triggerTranslate(View v) {
        Animation translate = AnimationUtils.loadAnimation(this, R.anim.translate);

        translate.setFillEnabled(mFillAfter);
        translate.setFillAfter(mFillAfter);

        mImg.startAnimation(translate);
    }

    public void triggerScale(View v) {
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale);

        scale.setFillEnabled(mFillAfter);
        scale.setFillAfter(mFillAfter);

        mImg.startAnimation(scale);
    }

    public void imageClicked(View v) {
        Toast.makeText(this, "IMAGE IS CLICKED!", Toast.LENGTH_SHORT).show();
    }
}
