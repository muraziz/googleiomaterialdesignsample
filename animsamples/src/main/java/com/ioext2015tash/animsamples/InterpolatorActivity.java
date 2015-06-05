package com.ioext2015tash.animsamples;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class InterpolatorActivity extends AppCompatActivity {

    Spinner mInterpolatorSpinner;
    boolean mIsOut = false;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpolator);

        mInterpolatorSpinner = (Spinner) findViewById(R.id.interpolatorSpinner);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, mInterpolatorNames);
        mInterpolatorSpinner.setAdapter(spinnerAdapter);


        // Get the view that will be animated
        mView = findViewById(R.id.square);

        Button button = (Button) findViewById(R.id.animateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Interpolator selected in the spinner
                Interpolator interpolator = mInterpolators[mInterpolatorSpinner.getSelectedItemPosition()];

                // Start the animation with the selected options
                int translationY;
                if (mIsOut) {
                    translationY = 0;
                } else {
                    translationY = 600;
                }
                mView.animate()
                        .translationY(translationY)
                        .setInterpolator(interpolator)
                        .setDuration(800)
                        .start();


                // Toggle direction of animation (path)
                mIsOut = !mIsOut;
            }
        });

    }

    Interpolator[] mInterpolators = new Interpolator[] {
            new LinearInterpolator(),
            new AccelerateInterpolator(2.0f),
            new DecelerateInterpolator(2.0f),
            new AccelerateInterpolator()
    };

    String[] mInterpolatorNames = new String[] {
            "Linear Interpolator",
            "Accelerate Interpolator",
            "Decelerate Interpolator",
            "Accelerate Decelerate Interpolator"
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interpolator, menu);
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
}
