package todo.codepath.gmac.gmaccodepathtodo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements EditTaskDialogFragment.EditTaskDialogListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ToDoListAdapter mToDoListAdapter;
    private final ArrayList<ToDoItem> mToDoData = new ArrayList<>();
    private ListView lvItems;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_layout);
        init();
    }

    private void init()
    {
        lvItems = (ListView) findViewById(R.id.task_list_view);
        mToDoListAdapter = new ToDoListAdapter(this, mToDoData);
        mToDoListAdapter.setListView(lvItems);
        lvItems.setAdapter(mToDoListAdapter);
    }


    public void onAddItem(final View v)
    {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        final Bundle args = new Bundle();
        args.putString(getString(R.string.hint_key), getString(R.string.hint_text));
        args.putString(getString(R.string.dialog_title_key), getString(R.string.dialog_add_title));
        args.putInt(getString(R.string.dialog_reason_key), Utils.DialogReason.ADD.ordinal());
        // Create and show the dialog.
        EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance(args);
        dialogFragment.setArguments(args);
        dialogFragment.show(ft, "dialog");

        /*final EditText newItem = (EditText) findViewById(R.id.newToDoItem);
        final String itemText = newItem.getText().toString();
        if (itemText != null && itemText.length() > 0)
        {
            mToDoListAdapter.add(itemText);
        }
        // Clears the edit-text and brings back hint.
        newItem.setText("");
        hideKeyboard();*/
    }

    private void hideKeyboard()
    {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void showAlertDialog(final ToDoItem currentEntry, final int position)
    {
        mPosition = position;
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        final Bundle args = new Bundle();
        args.putString(getString(R.string.current_entry_task_key), currentEntry.getTask());
        args.putString(getString(R.string.current_entry_date_time_key), currentEntry.getDateTime());
        args.putString(getString(R.string.dialog_title_key), getString(R.string.dialog_edit_title));
        args.putInt(getString(R.string.dialog_reason_key), Utils.DialogReason.EDIT.ordinal());
        // Create and show the dialog.
        EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance(args);
        dialogFragment.setArguments(args);
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void onFinishEditDialog(final ToDoItem toDoItem)
    {
        final String task = toDoItem.getTask();
        if (task != null && task.length() > 0)
        {
            mToDoData.get(mPosition).setTask(task);
            mToDoListAdapter.notifyDataSetChanged();
        }

        hideKeyboard();
    }

    @Override
    public void onFinishAddDialog(final ToDoItem toDoItem)
    {
        if (toDoItem != null && toDoItem.getTask()!= null && toDoItem.getTask().length() > 0)
        {
            mToDoData.add(toDoItem);
            mToDoListAdapter.notifyDataSetChanged();
        }

        hideKeyboard();
    }
}
