package todo.codepath.gmac.gmaccodepathtodo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.gesture.Gesture;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SwipeDetector implements View.OnTouchListener
{
    private static final String TAG = SwipeDetector.class.getSimpleName();
    private static final int MIN_DISTANCE = 200;
    private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
    private boolean motionInterceptDisallowed = false;
    private float mDownX;
    private float mUpX;
    private ToDoListItemContainer mToDoListItemContainer;
    private ListView mListView;
    private ToDoListAdapter mAdapter;
    private int mPosition;
    private Context mContext;

    public SwipeDetector(Context context, ToDoListAdapter adapter, ListView listView, ToDoListItemContainer h, int pos)
    {
        mToDoListItemContainer = h;
        mListView = listView;
        mAdapter = adapter;
        mPosition = pos;
        mContext = context;

    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                mDownX = event.getX();
                return true; // allow other events like Click to be processed
            }

            case MotionEvent.ACTION_MOVE:
            {
                mUpX = event.getX();
                final float deltaX = mDownX - mUpX;

                if (deltaX < -MIN_LOCK_DISTANCE && mListView != null && !motionInterceptDisallowed)
                {
                    mListView.requestDisallowInterceptTouchEvent(true);
                    motionInterceptDisallowed = true;
                }

                if (deltaX < -MIN_LOCK_DISTANCE)
                {
                    mToDoListItemContainer.getDeleteView().setVisibility(View.GONE);
                }
                else if (deltaX > MIN_LOCK_DISTANCE)
                {
                    // if first swiped left and then swiped right
                    mToDoListItemContainer.getDeleteView().setVisibility(View.VISIBLE);
                }

                swipe(-(int) deltaX);
                return true;
            }

            case MotionEvent.ACTION_UP:
                mUpX = event.getX();
                float deltaX = mUpX - mDownX;
                if (deltaX < -MIN_LOCK_DISTANCE && Math.abs(deltaX) > MIN_DISTANCE)
                {
                    Log.i(TAG, "Swipe left action up");
                    // left or right
                    mAdapter.swipeRemove(mPosition);
                }
                else
                {
                    Log.i(TAG, "NO Swipe action up");
                    swipe(0);
                    v.performClick();
                }

                if (mListView != null)
                {
                    mListView.requestDisallowInterceptTouchEvent(false);
                    motionInterceptDisallowed = false;
                }

                mToDoListItemContainer.getDeleteView().setVisibility(View.GONE);
                return true;

            case MotionEvent.ACTION_CANCEL:
                mToDoListItemContainer.getDeleteView().setVisibility(View.GONE);
                return false;
        }

        return true;
    }

    private void swipe(int distance)
    {
        View animationView = mToDoListItemContainer.getMainView();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
        params.rightMargin = -distance;
        params.leftMargin = distance;
        animationView.setLayoutParams(params);
    }


}

