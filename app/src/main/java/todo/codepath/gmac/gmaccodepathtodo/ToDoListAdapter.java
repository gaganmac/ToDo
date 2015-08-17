package todo.codepath.gmac.gmaccodepathtodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToDoListAdapter extends ArrayAdapter<String>
{
    private static final String TAG = ToDoListAdapter.class.getSimpleName();
    private int mLayoutResource;
    private LayoutInflater mInflater;
    private ListView listView;

    public ToDoListAdapter(Context context, int resource)
    {
        super(context, resource);
        mLayoutResource = resource;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {

        View workingView;
        if (convertView == null)
        {
            workingView = mInflater.inflate(mLayoutResource, null);
        }
        else
        {
            workingView = convertView;
        }
        final ToDoListItemContainer toDoListItemContainer = ToDoListItemContainer.getToDoItemContainer(workingView);
        final String entry = getItem(position);
        final TextView todoItemView = (TextView) toDoListItemContainer.getMainView().findViewById(R.id.todo_row);
        todoItemView.setText(entry);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toDoListItemContainer.getMainView().getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        toDoListItemContainer.getMainView().setLayoutParams(params);
        workingView.setOnTouchListener(new SwipeDetector(this, listView, toDoListItemContainer, position));

        return workingView;
    }

    public void setListView(ListView view)
    {
        listView = view;
    }

    public void swipeRemove(final int position)
    {
        remove(getItem(position));
        notifyDataSetChanged();
    }

}