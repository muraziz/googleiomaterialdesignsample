package com.ioext2015tash.anim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ioext2015tash.anim.data.Album;
import com.ioext2015tash.anim.data.AlbumList;
import com.ioext2015tash.anim.widget.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by aziz on 5/31/15.
 */
public class AlbumListFragment extends Fragment {

    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGridView = (GridView) inflater.inflate(R.layout.album_list_fragment, null);
        mGridView.setAdapter(new AlbumAdapter(getActivity(), 0, AlbumList.ITEMS));
        return mGridView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void showDetails(int albumIndex) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.ALBUM_INDEX, albumIndex);
        getActivity().startActivity(intent);
    }

    private class AlbumAdapter extends ArrayAdapter<Album> {
        LayoutInflater mInflater;
        Context mContext;
        public AlbumAdapter(Context context, int resource, Album[] objects) {
            super(context, resource, objects);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.album_list_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.cover = (SquareImageView)convertView.findViewById(R.id.imgCover);
                holder.txtAlbum = (TextView)convertView.findViewById(R.id.txtAlbum);
                holder.txtSinger = (TextView)convertView.findViewById(R.id.txtSinger);
                holder.favorite = (CheckBox)convertView.findViewById(R.id.favorite);
                holder.txtContainer = (RelativeLayout)convertView.findViewById(R.id.txtContainer);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            final Album currentAlbum = getItem(position);
            /*Target target = new Target() {
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Palette palette = Palette.generate(bitmap);
                    holder.txtContainer.setBackgroundColor(palette.getMutedSwatch().getRgb());
                    holder.cover.setImageBitmap(bitmap);
                    currentAlbum.paletteColor = palette.getMutedSwatch().getRgb();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            Picasso.with(mContext).load(currentAlbum.resourceIdSmall).into(target);*/
            Picasso.with(mContext).load(currentAlbum.resourceIdSmall).into(holder.cover, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable)holder.cover.getDrawable()).getBitmap();
                    Palette palette = Palette.generate(bitmap);
                    int paletteColor = palette.getMutedSwatch().getRgb();
                    holder.txtContainer.setBackgroundColor(paletteColor);
                    currentAlbum.paletteColor = paletteColor;

                    Log.d("palette", "Picasso.onSuccess() position="+position+", color="+paletteColor);
                }

                @Override
                public void onError() {

                }
            });
            holder.txtAlbum.setText(currentAlbum.album);
            holder.txtSinger.setText(currentAlbum.singer);
            holder.favorite.setChecked(currentAlbum.favorite);
            final int albumIndex = position;
            holder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDetails(albumIndex);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        SquareImageView cover;
        TextView txtSinger, txtAlbum;
        CheckBox favorite;
        RelativeLayout txtContainer;
    }
}
