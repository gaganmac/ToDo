package todo.codepath.gmac.gmaccodepathtodo;

public class ToDoItem
{
    private String mDateTime;
    private String mTask;

    public ToDoItem(final String task, final String dateTime)
    {
        mDateTime = dateTime;
        mTask = task;
    }

    public String getDateTime()
    {
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

