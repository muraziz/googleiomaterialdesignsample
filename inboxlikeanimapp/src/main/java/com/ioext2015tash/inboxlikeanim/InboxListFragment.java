package com.ioext2015tash.inboxlikeanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ioext2015tash.inboxlikeanim.data.MailItem;
import com.ioext2015tash.inboxlikeanim.data.MailData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(2.f);


    private InboxAdapter mInboxAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar_actionbar);

        mListView = (ListView) inflater.inflate(R.layout.inbox_list, null);
        ArrayList<MailItem> mailItems = new ArrayList<MailItem>(Arrays.asList(MailData.ITEMS));
        mInboxAdapter = new InboxAdapter(getActivity(), 0, mailItems);
        mListView.setAdapter(mInboxAdapter);
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
                Log.d("inboxlist", "onTouch() ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPrevTouchY != Float.NaN) {
                    float diff = event.getY() - mPrevTouchY;
                    moveToolbar(diff);
                    Log.d("inboxlist", "onTouch() ACTION_MOVE diff=" + diff);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("inboxlist", "onTouch() ACTION_UP");
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(10);
                float yVelocity = velocityTracker.getYVelocity();
                adjustToolbar(yVelocity);
                mPrevTouchY = Float.NaN;
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

        Log.d("inboxlist", "moveToolbar() diff="+diff
                +", transY="+toolbarTranslationY
                +", toolbarHeight="+toolbarHeight);

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
        int toolbarHeight = mToolbar.getHeight();
        float toolbarTranslationY = mToolbar.getTranslationY();
        Log.d("inboxlist", "adjustToolbar() velocity=" + velocity
                + ", toolbarTransY=" + toolbarTranslationY
                + ", toolbarHeight=" + toolbarHeight);
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
        public InboxAdapter(Context context, int resource, List<MailItem> objects) {
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
            convertView.setOnTouchListener(new InboxListItemTouchListener(convertView, position));
            return convertView;
        }
    }

    class InboxListItemTouchListener implements View.OnTouchListener {
        private float prevTouchX;
        private float prevTouchY;

        private float firstTouchX;
        private float firstTouchY;

        private float touchSlop;
        final int position;

        View layerFront;
        View layerDone;
        View layerSnooze;
        View host;
        VelocityTracker velocityTracker = VelocityTracker.obtain();

        //Interpolator

        InboxListItemTouchListener(View host, int position) {
            this.position = position;
            touchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();
            layerFront = host.findViewById(R.id.layer_front);
            layerDone = host.findViewById(R.id.layer_done);
            layerSnooze = host.findViewById(R.id.layer_snooze);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            velocityTracker.addMovement(event);

            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("inboxlist", "Horizontal move: ACTION_DOWN");
                    firstTouchX = event.getX();
                    firstTouchY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float diffX = event.getX() - prevTouchX;

                    if (Math.abs(firstTouchX - event.getX()) > touchSlop
                            && Math.abs(firstTouchY - event.getY()) < touchSlop) {
                        (view.getParent()).requestDisallowInterceptTouchEvent(true);
                    }

                    float modifiedTranslateX = layerFront.getTranslationX() + diffX;
                    if (modifiedTranslateX > 0) {
                        layerDone.setVisibility(View.VISIBLE);
                        layerSnooze.setVisibility(View.GONE);
                    } else {
                        layerDone.setVisibility(View.GONE);
                        layerSnooze.setVisibility(View.VISIBLE);
                    }

                    layerFront.setTranslationX(modifiedTranslateX);
                    Log.d("inboxlist", "Horizontal move: ACTION_MOVE diff=" + diffX
                                    + ", currentTransX=" + layerFront.getTranslationX()
                                    + ", prevTouchX=" + prevTouchX
                                    + ", event.getX()=" + event.getX());

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    (view.getParent()).requestDisallowInterceptTouchEvent(false);
                    velocityTracker.computeCurrentVelocity(10);
                    float xVelocity = velocityTracker.getXVelocity();

                    float layerFrontTransX = layerFront.getTranslationX();
                    float listWidth = view.getWidth();
                    if (Math.abs(xVelocity) < 15) {
                        if (layerFrontTransX < -listWidth/2) {
                            // animate out left
                            animateLeft(layerFrontTransX, listWidth);
                        } else if (layerFrontTransX < listWidth / 2) {
                            // animate back
                            layerFront.animate()
                                    .translationX(0)
                                    .setInterpolator(DECELERATE_INTERPOLATOR)
                                    .setDuration(300)
                                    .setListener(null);
                        } else {
                            // animate out right
                            animateRight(layerFrontTransX, listWidth);
                        }
                    } else {
                        if (xVelocity > 0) {
                            animateRight(layerFrontTransX, listWidth);
                        } else {
                            animateLeft(layerFrontTransX, listWidth);
                        }
                    }
                    Log.d("inboxlist", "Horizontal move: ACTION_UP/CANCEL");
                    break;
            }
            prevTouchX = event.getX();
            prevTouchY = event.getY();
            return true;
        }

        private void animateLeft(float layerFrontTransX, float listWidth) {
            layerFront.animate()
                    .translationX(-listWidth)
                    .setInterpolator(DECELERATE_INTERPOLATOR)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeItem(position, false);
                        }
                    });
        }

        private void animateRight(float layerFrontTransX, float listWidth) {
            layerFront.animate()
                    .translationX(listWidth)
                    .setInterpolator(DECELERATE_INTERPOLATOR)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeItem(position, true);
                        }
                    });
        }
    }

    private void removeItem(final int position, boolean isDone) {

        final int adjustedPosition = position + mListView.getHeaderViewsCount();
        final View child = mListView.getChildAt(adjustedPosition - mListView.getFirstVisiblePosition());
        Toast.makeText(getActivity(), "TASK is " + (isDone ? "DONE" : "SNOOZED"), Toast.LENGTH_SHORT).show();
        child.animate()
                .alpha(0)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetAllProperties(child);
                        deleteFromAdapter(position);
                    }
                });
    }

    private void resetAllProperties(View child) {
        View layerFront = child.findViewById(R.id.layer_front);
        View layerDone = child.findViewById(R.id.layer_done);
        View layerSnooze = child.findViewById(R.id.layer_snooze);
        layerFront.setTranslationX(0);
        layerDone.setVisibility(View.GONE);
        layerSnooze.setVisibility(View.GONE);
        child.setAlpha(1);
    }

    private void deleteFromAdapter(final int position) {
        mInboxAdapter.remove(mInboxAdapter.getItem(position));

        mListView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mListView.getViewTreeObserver().removeOnPreDrawListener(this);

                int childCount = mListView.getChildCount();
                int firstVisiblePosition = mListView.getFirstVisiblePosition();
                int headersCount = mListView.getHeaderViewsCount();
                for (int i=0; i<childCount; i++) {
                    if (i+firstVisiblePosition >= position + headersCount) {
                        View child = mListView.getChildAt(i);
                        child.setTranslationY(child.getHeight());
                        child.animate()
                                .translationY(0)
                                .setDuration(300)
                                .setInterpolator(DECELERATE_INTERPOLATOR)
                                .setListener(null);
                    }
                }
                return true;
            }
        });
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtBody;
        TextView txtSender;
    }
}
