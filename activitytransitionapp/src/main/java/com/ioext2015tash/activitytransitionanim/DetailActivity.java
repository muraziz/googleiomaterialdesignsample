package com.ioext2015tash.activitytransitionanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ioext2015tash.activitytransitionanim.data.Album;
import com.ioext2015tash.activitytransitionanim.data.AlbumList;
import com.ioext2015tash.activitytransitionanim.interpolators.EaseOutCirc;
import com.ioext2015tash.activitytransitionanim.widget.SquareImageView;

/**
 * Created by aziz on 6/1/15.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String ALBUM_INDEX = "ALBUM_INDEX";

    private Toolbar mToolbar;
    private SquareImageView mImgAlbumCover;
    private int mCurrentAlbumIndex;
    private View mAlbumAuthorTitleContainer;
    private View mSongDetailsContainer;
    private View mSongDetailsContainerInner;
    private ImageView mFab;

    // coordinates of image in previous activity
    private int mLeft, mTop, mWidth;

    TextView mTxtAlbumAuthor, mTxtAlbumTitle, mTxtSongNumber, mTxtSongTitle, mTxtSongDuration;
    ImageView mImgVolume;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCurrentAlbumIndex = getIntent().getIntExtra(ALBUM_INDEX, 0);
        Album album = AlbumList.ITEMS[mCurrentAlbumIndex];

        mAlbumAuthorTitleContainer = findViewById(R.id.containerAlbumAuthorTitle);
        mSongDetailsContainer = findViewById(R.id.songDetailsContainer);
        mSongDetailsContainerInner = findViewById(R.id.songDetailsContainerInner);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFab = (ImageView) findViewById(R.id.fab);

        init(album);

        adjustFAB();

        preAnimInit();

        /* Step 3. Fade in bg */
        //fadeInBackground();

        /* Step 1. Image scaling */
        //scaleUpAlbumCover();
    }

    private void fadeInBackground() {
        // manipulation starts
        View windowBgAlternative = findViewById(R.id.windowBgAlternative);
        windowBgAlternative.setAlpha(0);

        windowBgAlternative.animate()
                .alpha(1)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600);
    }

    private void fadeOutBackground(int startDelay) {
        // manipulation starts
        View windowBgAlternative = findViewById(R.id.windowBgAlternative);
        windowBgAlternative.animate()
                .alpha(0)
                .setStartDelay(startDelay)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(800 - startDelay);
    }

    private void preAnimInit() {
        // scale fab down to 0

        /* Step 5. Scaling up FAB */
        /*mFab.setScaleX(0);
        mFab.setScaleY(0);*/

        /* Step 3. Fade in toolbar */
        //mToolbar.setAlpha(0);

        /* Step 2. Stretching text areas */
        // Make it invisible first
        /*mSongDetailsContainer.setVisibility(View.INVISIBLE);
        mAlbumAuthorTitleContainer.setVisibility(View.INVISIBLE);

        mTxtAlbumAuthor.setAlpha(0);
        mTxtAlbumTitle.setAlpha(0);
        mSongDetailsContainerInner.setAlpha(0);*/
    }

    private void scaleUpAlbumCover() {
        String packageName = getPackageName();
        Intent incomingIntent = getIntent();
        mLeft = incomingIntent.getIntExtra(packageName + ".left", 0);
        mTop = incomingIntent.getIntExtra(packageName + ".top", 0);
        mWidth = incomingIntent.getIntExtra(packageName + ".width", 0);

        mImgAlbumCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImgAlbumCover.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screenLocation = new int[2];
                mImgAlbumCover.getLocationOnScreen(screenLocation);

                int translationX = mLeft - screenLocation[0];
                int translationY = mTop  - screenLocation[1];
                float scaleFactor = mWidth / (float) mImgAlbumCover.getWidth();

                mImgAlbumCover.setTranslationX(translationX);
                mImgAlbumCover.setTranslationY(translationY);
                mImgAlbumCover.setScaleX(scaleFactor);
                mImgAlbumCover.setScaleY(scaleFactor);
                mImgAlbumCover.setPivotX(0);
                mImgAlbumCover.setPivotY(0);

                mImgAlbumCover.animate()
                        .translationX(0)
                        .translationY(0)
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(400)
                        .setStartDelay(100)
                        .setInterpolator(new EaseOutCirc())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                /* Step 2. Animate Bottom parts */
                                //stretchBottomPanels();

                                /* Step 4. Fade-in toolbar */
                                //fadeInToolbar();
                            }
                        });
                return true;
            }
        });
    }

    private void stretchBottomPanels() {
        mAlbumAuthorTitleContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mAlbumAuthorTitleContainer.getViewTreeObserver().removeOnPreDrawListener(this);

                mAlbumAuthorTitleContainer.setVisibility(View.VISIBLE);

                int albumTitleBottom = mAlbumAuthorTitleContainer.getBottom();
                int albumTitleTop = mAlbumAuthorTitleContainer.getTop();

                ObjectAnimator animAlbumTitle = ObjectAnimator.ofInt(mAlbumAuthorTitleContainer,
                        "bottom", albumTitleTop, albumTitleBottom);

                mSongDetailsContainer.setVisibility(View.VISIBLE);

                int songDetailsBottom = mSongDetailsContainer.getBottom();
                int songDetailsTop = mSongDetailsContainer.getTop();

                ObjectAnimator animSongDetailsBottom = ObjectAnimator.ofInt(mSongDetailsContainer,
                        "bottom", albumTitleTop, songDetailsBottom);

                ObjectAnimator animSongDetailsTop = ObjectAnimator.ofInt(mSongDetailsContainer,
                        "top", albumTitleTop, songDetailsTop);

                Animator textFadeInAnimator = getTextFadeInAnimator(300, 400);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animAlbumTitle, animSongDetailsTop, animSongDetailsBottom, textFadeInAnimator);
                animatorSet.setDuration(500);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.start();

                return false;
            }
        });

        /* Step 5. Scale up fab */
        //scaleUpFab(500);
    }

    private Animator getTextFadeInAnimator(int startDelay, int duration) {
        ObjectAnimator animTxtAlbumAuthor = ObjectAnimator.ofFloat(mTxtAlbumAuthor, View.ALPHA, 1);
        ObjectAnimator animTxtAlbumTitle = ObjectAnimator.ofFloat(mTxtAlbumTitle, View.ALPHA, 1);
        View songDetailsContainerInner = findViewById(R.id.songDetailsContainerInner);
        ObjectAnimator animSongDetails = ObjectAnimator.ofFloat(songDetailsContainerInner, View.ALPHA, 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animTxtAlbumAuthor, animTxtAlbumTitle, animSongDetails);
        animatorSet.setStartDelay(startDelay);
        animatorSet.setInterpolator(new EaseOutCirc());
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    private void adjustFAB() {
        mImgAlbumCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImgAlbumCover.getViewTreeObserver().removeOnPreDrawListener(this);
                int translationX = mImgAlbumCover.getRight() - mFab.getWidth();
                int translationY = mImgAlbumCover.getBottom() - mFab.getHeight() / 2;

                float rightPadding = dpToPx(14);
                // add right padding
                translationX -= rightPadding;
                mFab.setTranslationX(translationX);
                mFab.setTranslationY(translationY);
                return true;
            }
        });
    }

    private float dpToPx(int dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private void scaleUpFab(int startDelay) {
        mFab.animate()
                .setDuration(500)
                .setStartDelay(startDelay)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new EaseOutCirc());
    }

    private void fadeInToolbar() {
        mToolbar.animate().alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator());
    }

    private void fadeOutToolbar() {
        mToolbar.animate().alpha(0).setDuration(400).setInterpolator(new AccelerateInterpolator());
    }

    private void init(Album album) {
        int oneSide = getDisplayWidth();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = oneSide;
        options.outHeight = oneSide;
        Bitmap cover = BitmapFactory.decodeResource(getResources(), album.resourceIdLarge, options);

        mImgAlbumCover = (SquareImageView)findViewById(R.id.albumCover);
        mImgAlbumCover.setImageBitmap(cover);

        View albumAuthorTitleContainer = findViewById(R.id.containerAlbumAuthorTitle);
        albumAuthorTitleContainer.setBackgroundColor(album.paletteColor);

        mTxtAlbumAuthor = (TextView)findViewById(R.id.txtAlbumAuthor);
        mTxtAlbumAuthor.setText(album.singer);

        mTxtAlbumTitle = (TextView)findViewById(R.id.txtAlbumTitle);
        mTxtAlbumTitle.setText(album.album);

        mTxtSongNumber = (TextView)findViewById(R.id.txtSongNumber);
        mTxtSongNumber.setText("1");

        mTxtSongTitle = (TextView)findViewById(R.id.txtSongTitle);
        mTxtSongTitle.setText(album.songTitle);

        mTxtSongDuration = (TextView)findViewById(R.id.txtSongDuration);
        mTxtSongDuration.setText(album.songDuration);

        mImgVolume = (ImageView)findViewById(R.id.imgVolume);
        BitmapDrawable volumeIcon = (BitmapDrawable)getResources().getDrawable(R.drawable.perm_group_audio_settings);
        int accentColor = getResources().getColor(R.color.myAccentColor);
        volumeIcon.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP);
        mImgVolume.setImageDrawable(volumeIcon);
    }

    private int getDisplayWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return Math.min(width, height);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.menu_detailactivity, menu);
        MenuItem starItem = menu.findItem(R.id.action_star);
        setStarChecked(starItem, AlbumList.ITEMS[mCurrentAlbumIndex].favorite);
        return true;
    }

    private void setStarChecked(MenuItem menuItem, boolean checked) {
        Drawable starDrawable = getResources().getDrawable(checked ?
                R.drawable.ic_action_important : R.drawable.ic_action_not_important);
        menuItem.setIcon(starDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_star:
                item.setChecked(!item.isChecked());
                Album currentAlbum = AlbumList.ITEMS[mCurrentAlbumIndex];
                currentAlbum.favorite = !currentAlbum.favorite;
                setStarChecked(item, currentAlbum.favorite);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /* Step N. Reverse Image scaling */

        //triggerFadeOutTextAndScaleDownFab();
        //fadeOutToolbar();
        //fadeOutBackground(300);

        super.onBackPressed();
    }

    private void triggerFadeOutTextAndScaleDownFab() {

        ObjectAnimator fadeOutAlbumAuthor = ObjectAnimator.ofFloat(mAlbumAuthorTitleContainer,
                View.ALPHA, 0);
        ObjectAnimator fadeOutSongDetails = ObjectAnimator.ofFloat(mSongDetailsContainer,
                View.ALPHA, 0);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0);
        ObjectAnimator scaleDownFab = ObjectAnimator.ofPropertyValuesHolder(mFab, pvhX, pvhY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeOutAlbumAuthor, fadeOutSongDetails, scaleDownFab);
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                triggerReverseImageScaling();
            }
        });
        animatorSet.start();
    }

    private void triggerReverseImageScaling() {

        int[] screenLocation = new int[2];
        mImgAlbumCover.getLocationOnScreen(screenLocation);

        int translationX = mLeft - screenLocation[0];
        int translationY = mTop - screenLocation[1];
        float scale = mWidth / (float) mImgAlbumCover.getWidth();

        mImgAlbumCover.animate()
                .translationX(translationX)
                .translationY(translationY)
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });

    }
}
