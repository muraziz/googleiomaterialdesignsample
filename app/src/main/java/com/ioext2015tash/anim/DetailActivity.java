package com.ioext2015tash.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ioext2015tash.anim.data.Album;
import com.ioext2015tash.anim.data.AlbumList;
import com.ioext2015tash.anim.widget.SquareImageView;

/**
 * Created by aziz on 6/1/15.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String ALBUM_INDEX = "ALBUM_INDEX";

    private Toolbar mToolbar;
    private SquareImageView mImgAlbumCover;
    private int mCurrentAlbumIndex;
    private View mWindowBgAlternative;
    private View mLLContainer;
    private View mAlbumAuthorTitleContainer;
    private View mSongDetailsContainer;
    private ImageView mFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCurrentAlbumIndex = getIntent().getIntExtra(ALBUM_INDEX, 0);
        Album album = AlbumList.ITEMS[mCurrentAlbumIndex];

        mLLContainer = findViewById(R.id.llContainer);
        mAlbumAuthorTitleContainer = findViewById(R.id.containerAlbumAuthorTitle);
        mSongDetailsContainer = findViewById(R.id.songDetailsContainer);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // manipulation starts
        mWindowBgAlternative = findViewById(R.id.windowBgAlternative);
        mWindowBgAlternative.setAlpha(0);

        mWindowBgAlternative.animate()
                .alpha(1)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(1000);



        mWindowBgAlternative.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*fadeInToolbar();
                stretchBottomPanels();*/
            }
        }, 1000);

        mFab = (ImageView) findViewById(R.id.fab);

        init(album);
        adjustFAB();

        preAnimInit();

        scaleUpAlbumCover();
    }

    private void preAnimInit() {
        // scale fab down to 0
        mFab.setScaleX(0);
        mFab.setScaleY(0);

        mToolbar.setAlpha(0);

        mSongDetailsContainer.setVisibility(View.INVISIBLE);
        mAlbumAuthorTitleContainer.setVisibility(View.INVISIBLE);
    }

    private void scaleUpAlbumCover() {
        String packageName = getPackageName();
        Intent incomingIntent = getIntent();
        final int left = incomingIntent.getIntExtra(packageName + ".left", 0);
        final int top = incomingIntent.getIntExtra(packageName + ".top", 0);
        final int width = incomingIntent.getIntExtra(packageName + ".width", 0);

        mImgAlbumCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImgAlbumCover.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screenLocation = new int[2];
                mImgAlbumCover.getLocationOnScreen(screenLocation);

                int translationX = left - screenLocation[0];
                int translationY = top - screenLocation[1];
                float scale = width / (float)mImgAlbumCover.getWidth();

                mImgAlbumCover.setTranslationX(translationX);
                mImgAlbumCover.setTranslationY(translationY);
                mImgAlbumCover.setScaleX(scale);
                mImgAlbumCover.setScaleY(scale);
                mImgAlbumCover.setPivotX(0);
                mImgAlbumCover.setPivotY(0);
                mImgAlbumCover.animate()
                        .translationX(0)
                        .translationY(0)
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(800)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                fadeInToolbar();
                                stretchBottomPanels();
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

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animAlbumTitle, animSongDetailsTop, animSongDetailsBottom);
                animatorSet.setDuration(1000);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.start();

                return false;
            }
        });

        mSongDetailsContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                scaleUpFab();
            }
        }, 800);
    }

    private void adjustFAB() {
        mImgAlbumCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImgAlbumCover.getViewTreeObserver().removeOnPreDrawListener(this);
                int translationX = mImgAlbumCover.getRight() - mFab.getWidth();
                int translationY = mImgAlbumCover.getBottom() - mFab.getHeight() / 2;

                // add right padding
                Resources r = getResources();
                float rightPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
                translationX -= rightPadding;
                mFab.setTranslationX(translationX);
                mFab.setTranslationY(translationY);
                return true;
            }
        });
    }

    private void scaleUpFab() {
        mFab.animate()
                .setDuration(600)
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new DecelerateInterpolator());
    }

    private void fadeInToolbar() {
        mToolbar.animate().alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator());
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

        TextView txtAlbumAuthor = (TextView)findViewById(R.id.txtAlbumAuthor);
        txtAlbumAuthor.setText(album.singer);

        TextView txtAlbumTitle = (TextView)findViewById(R.id.txtAlbumTitle);
        txtAlbumTitle.setText(album.album);

        TextView txtSongNumber = (TextView)findViewById(R.id.txtSongNumber);
        txtSongNumber.setText("1");

        TextView txtSongTitle = (TextView)findViewById(R.id.txtSongTitle);
        txtSongTitle.setText(album.songTitle);

        TextView txtSongDuration = (TextView)findViewById(R.id.txtSongDuration);
        txtSongDuration.setText(album.songDuration);

        ImageView imgVolume = (ImageView)findViewById(R.id.imgVolume);
        BitmapDrawable volumeIcon = (BitmapDrawable)getResources().getDrawable(R.drawable.perm_group_audio_settings);
        int accentColor = getResources().getColor(R.color.myAccentColor);
        volumeIcon.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP);
        imgVolume.setImageDrawable(volumeIcon);
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_star) {
            item.setChecked(!item.isChecked());
            Album currentAlbum = AlbumList.ITEMS[mCurrentAlbumIndex];
            currentAlbum.favorite = !currentAlbum.favorite;
            setStarChecked(item, currentAlbum.favorite);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
