package todo.codepath.gmac.gmaccodepathtodo;

import android.util.Log;

public class ToDoItem
{
    private static final String TAG = ToDoItem.class.getSimpleName();
    private String mDateTime;
    private String mTask;

    public ToDoItem(final String task, final String dateTime)
    {
        if (dateTime == null)
        {
            mDateTime = Utils.getCurrentDateTime();
        }

        mTask = task;
    }

    /**
     *
     * @return Returns current date/time if it is not set.
     */
    public String getDateTime()
    {
        if (mDateTime == null)
        {
            Log.w(TAG, "Setting default date time");
            mDateTime = Utils.getCurrentDateTime();
        }

        return mDateTime;
    }

    public String getTask()
    {
        return mTask;
    }

    public void setDateTime(final String dateTime)
    {
        mDateTime = dateTime;
    }

    /**
     * Updates task string
     * @param task task string
     */
    public void setTask(final String task)
    {
        mTask = task;
    }
};

