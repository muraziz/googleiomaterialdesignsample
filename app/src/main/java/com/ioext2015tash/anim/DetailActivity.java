package com.ioext2015tash.anim;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.ioext2015tash.anim.data.Album;
import com.ioext2015tash.anim.data.AlbumList;

/**
 * Created by aziz on 6/1/15.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String ALBUM_INDEX = "ALBUM_INDEX";

    private Toolbar mToolbar;
    private ImageView mImgAlbumCover;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int albumIndex = getIntent().getIntExtra(ALBUM_INDEX, 0);
        Album album = AlbumList.ITEMS[albumIndex];

        mToolbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init(album);
        adjustFAB();
    }

    private void adjustFAB() {
        final ImageView fab = (ImageView)findViewById(R.id.fab);

        mImgAlbumCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImgAlbumCover.getViewTreeObserver().removeOnPreDrawListener(this);
                int translationX = mImgAlbumCover.getRight() - fab.getWidth();
                int translationY = mImgAlbumCover.getBottom()-fab.getHeight()/2;

                // add right padding
                Resources r = getResources();
                float rightPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
                translationX -= rightPadding;
                fab.setTranslationX(translationX);
                fab.setTranslationY(translationY);
                return true;
            }
        });
    }

    private void init(Album album) {
        int oneSide = getDisplayWidth();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = oneSide;
        options.outHeight = oneSide;
        Bitmap cover = BitmapFactory.decodeResource(getResources(), album.resourceIdLarge, options);

        //Palette palette = Palette.generate(cover);
        mImgAlbumCover = (ImageView)findViewById(R.id.albumCover);
        mImgAlbumCover.setImageBitmap(cover);

        View albumAuthorTitleContainer = findViewById(R.id.containerAlbumAuthorTitle);
        albumAuthorTitleContainer.setBackgroundColor(album.paletteColor);
        //albumAuthorTitleContainer.setBackgroundColor(palette.getMutedSwatch().getRgb());

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
        imgVolume.setImageResource(R.drawable.perm_group_audio_settings);
    }

    private int getDisplayWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return Math.min(width, height);
    }
}
