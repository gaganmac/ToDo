package todo.codepath.gmac.gmaccodepathtodo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DateTimeView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ToDoListAdapter extends ArrayAdapter<ToDoItem>
{
    private static final String TAG = ToDoListAdapter.class.getSimpleName();
    private ListView mListView;
    private Activity mParentActivity;

    public ToDoListAdapter(Context context, ArrayList<ToDoItem> todoList)
    {
        super(context, 0, todoList);
        mParentActivity = (Activity) context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View workingView;
        if (convertView == null)
        {
            workingView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        }
        else
        {
            workingView = convertView;
        }
        final ToDoListItemContainer toDoListItemContainer = ToDoListItemContainer.getToDoItemContainer(workingView);
        final TextView todoItemView = (TextView) toDoListItemContainer.getMainView().findViewById(R.id.todo_row_text);
        final DateTimeView dateTimeView = (DateTimeView) toDoListItemContainer.getMainView().findViewById(R.id.todo_row_date);
        todoItemView.setText(getItem(position).getTask());
        dateTimeView.setText(getItem(position).getDateTime());


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toDoListItemContainer.getMainView().getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        toDoListItemContainer.getMainView().setLayoutParams(params);
        workingView.setOnTouchListener(new SwipeDetector(getContext(), this, mListView, toDoListItemContainer, position));

        workingView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((MainActivity) mParentActivity).showAlertDialog(getItem(position), position);
            }
        });

        return workingView;
    }

    public void setListView(ListView view)
    {
        mListView = view;
    }

    public void swipeRemove(final int position)
    {
        remove(getItem(position));
        notifyDataSetChanged();
    }
}