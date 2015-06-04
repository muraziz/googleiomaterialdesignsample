package com.ioext2015tash.inboxlikeanim;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ioext2015tash.inboxlikeanim.data.MailItem;
import com.ioext2015tash.inboxlikeanim.data.MailData;

/**
 * Created by aziz on 6/4/15.
 */
public class InboxListFragment extends Fragment implements AbsListView.OnScrollListener, View.OnTouchListener {

    private ListView mListView;
    private Toolbar mToolbar;
    private float mPrevTouchY;
    private boolean mTouchScrollInProgress = false;
    private final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private static final int TOOLBAR_TRANSLATION_DURATION = 600;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar_actionbar);

        mListView = (ListView) inflater.inflate(R.layout.inbox_list, null);
        mListView.setAdapter(new InboxAdapter(getActivity(), 0, MailData.ITEMS));
        mListView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mListView.getViewTreeObserver().removeOnPreDrawListener(this);

                int toolbarHeight = mToolbar.getHeight();

                View headerView = new View(getActivity());
                headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, toolbarHeight));

                mListView.addHeaderView(headerView);

                return false;
            }
        });

        mListView.setOnTouchListener(this);

        mListView.setOnScrollListener(this);
        return mListView;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        mVelocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Log.d("inboxlist", "onTouch() ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                float diff = event.getY() - mPrevTouchY;
                moveToolbar(diff);
                //Log.d("inboxlist", "onTouch() ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                //Log.d("inboxlist", "onTouch() ACTION_UP");
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(10);
                float yVelocity = velocityTracker.getYVelocity();
                adjustToolbar(yVelocity);
                break;
        }
        mPrevTouchY = event.getY();
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            mTouchScrollInProgress = true;
        } else {
            mTouchScrollInProgress = false;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    private void moveToolbar(float diff) {
        if (!mTouchScrollInProgress) return;

        int toolbarHeight = mToolbar.getHeight();
        float toolbarTranslationY = mToolbar.getTranslationY();
        float translationY;
        if (diff + toolbarTranslationY > 0) {
            // already fully visible, no more dragging possible
            translationY = 0;
        } else if (diff + toolbarTranslationY < -toolbarHeight) {
            // already fully invisible, no more pulling up feasible
            translationY = -toolbarHeight;
        } else {
            translationY = toolbarTranslationY + diff;
        }
        mToolbar.setTranslationY(translationY);
    }

    private void adjustToolbar(float velocity) {
        Log.d("inboxlist", "adjustToolbar() velocity="+velocity);
        int toolbarHeight = mToolbar.getHeight();
        float toolbarTranslationY = mToolbar.getTranslationY();
        if (Math.abs(velocity) < 15) {
            if (toolbarTranslationY > -toolbarHeight/2) {
                // more than half of toolbar visible, show it fully
                showToolbarWithAnim(toolbarHeight, toolbarTranslationY);
            } else {
                // less than half of toolbar is visible, hide it
                hideToolbarWithAnim(toolbarHeight, toolbarTranslationY);
            }
        } else {
            // touch velocity is big enough to neglect whether more or less than half of toolbar is visible
            if (velocity > 0) {
                showToolbarWithAnim(toolbarHeight, toolbarTranslationY);
            } else {
                hideToolbarWithAnim(toolbarHeight, toolbarTranslationY);
            }
        }
    }

    private void showToolbarWithAnim(int toolbarHeight, float toolbarTranslationY) {
        int duration = (int) (TOOLBAR_TRANSLATION_DURATION * (toolbarHeight - Math.abs(toolbarTranslationY)) / toolbarHeight);
        mToolbar.animate()
                .translationY(0)
                .setDuration(duration)
                .start();

    }

    private void hideToolbarWithAnim(int toolbarHeight, float toolbarTranslationY) {
        int duration = (int) (TOOLBAR_TRANSLATION_DURATION * Math.abs(toolbarTranslationY) / toolbarHeight);
        mToolbar.animate()
                .translationY(-toolbarHeight)
                .setDuration(duration)
                .start();
    }

    private class InboxAdapter extends ArrayAdapter<MailItem> {

        LayoutInflater mInflater;
        public InboxAdapter(Context context, int resource, MailItem[] objects) {
            super(context, resource, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.imgIcon = (ImageView)convertView.findViewById(R.id.letterIcon);
                holder.txtTitle = (TextView)convertView.findViewById(R.id.msgTitle);
                holder.txtBody = (TextView)convertView.findViewById(R.id.msgBody);
                holder.txtSender = (TextView)convertView.findViewById(R.id.msgSender);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            MailItem item = getItem(position);
            holder.imgIcon.setImageResource(item.iconResId);
            holder.txtTitle.setText(item.msgTitle);
            holder.txtBody.setText(item.msgBody);
            holder.txtSender.setText(item.msgSender);
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtBody;
        TextView txtSender;
    }
}
