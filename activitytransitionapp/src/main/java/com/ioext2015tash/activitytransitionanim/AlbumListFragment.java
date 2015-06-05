package com.ioext2015tash.activitytransitionanim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ioext2015tash.activitytransitionanim.data.Album;
import com.ioext2015tash.activitytransitionanim.data.AlbumList;
import com.ioext2015tash.activitytransitionanim.widget.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

    private void showDetails(View selectedView, int albumIndex) {
        int[] screenLocation = new int[2];
        selectedView.getLocationOnScreen(screenLocation);
        String packageName = getActivity().getPackageName();

        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.ALBUM_INDEX, albumIndex);

        /* Step 1. Image scaling */
        /*intent.putExtra(packageName + ".left", screenLocation[0]);
        intent.putExtra(packageName + ".top", screenLocation[1]);
        intent.putExtra(packageName + ".width", selectedView.getWidth());*/

        getActivity().startActivity(intent);

        // Step 1. Image scaling
        //getActivity().overridePendingTransition(0, 0);
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
            Picasso.with(mContext).load(currentAlbum.resourceIdSmall).into(holder.cover, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable)holder.cover.getDrawable()).getBitmap();
                    Palette palette = Palette.generate(bitmap);
                    int paletteColor = palette.getMutedSwatch().getRgb();
                    holder.txtContainer.setBackgroundColor(paletteColor);
                    currentAlbum.paletteColor = paletteColor;
                }

                @Override
                public void onError() {
                }
            });
            holder.txtAlbum.setText(currentAlbum.album);
            holder.txtSinger.setText(currentAlbum.singer);
            holder.favorite.setChecked(currentAlbum.favorite);
            holder.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    currentAlbum.favorite = checked;
                }
            });
            final int albumIndex = position;
            holder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDetails(view, albumIndex);
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
