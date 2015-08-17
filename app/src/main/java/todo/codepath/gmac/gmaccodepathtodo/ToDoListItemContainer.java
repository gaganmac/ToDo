package todo.codepath.gmac.gmaccodepathtodo;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ToDoListItemContainer
{
    private RelativeLayout mMainView;
    private RelativeLayout mDeleteView;
    private RelativeLayout mShareView;
        /* other views here */

    public static ToDoListItemContainer getToDoItemContainer(View workingView)
    {
        Object tag = workingView.getTag();
        final ToDoListItemContainer toDoListItemContainer;

        if (tag == null || !(tag instanceof ToDoListItemContainer))
        {
            toDoListItemContainer = new ToDoListItemContainer();
            toDoListItemContainer.mMainView = (RelativeLayout)workingView.findViewById(R.id.todo_object_mainview);
            toDoListItemContainer.mDeleteView = (RelativeLayout) workingView.findViewById(R.id.todo_delete_view);
            //toDoListItemContainer.mShareView = (RelativeLayout)workingView.findViewById(R.id.todo_done_view);
            /* initialize other views here */
            workingView.setTag(toDoListItemContainer);
        }
        else
        {
            toDoListItemContainer = (ToDoListItemContainer) tag;
        }

        return toDoListItemContainer;
    }

    public RelativeLayout getMainView()
    {
        return mMainView;
    }

    public RelativeLayout getDeleteView()
    {
        return mDeleteView;
    }

    public RelativeLayout getShareView()
    {
        return mShareView;
    }


}
