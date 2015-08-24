package todo.codepath.gmac.gmaccodepathtodo;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements EditTaskDialogFragment.EditTaskDialogListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ToDoListAdapter mToDoListAdapter;
    private final ArrayList<String> mToDoData = new ArrayList<>();
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
        final EditText newItem = (EditText) findViewById(R.id.newToDoItem);
        final String itemText = newItem.getText().toString();
        if (itemText != null && itemText.length() > 0)
        {
            mToDoListAdapter.add(itemText);
        }
        // Clears the edit-text and brings back hint.
        newItem.setText("");
        hideKeyboard();
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


    public void showAlertDialog(final String currentEntry, final int position)
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
        args.putString(getString(R.string.current_entry_key), currentEntry);
        // Create and show the dialog.
        EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance(this, currentEntry);
        dialogFragment.setArguments(args);
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, final Object text)
    {
        final String updatedText = (String) text;

        if (updatedText != null && updatedText.length() > 0)
        {
                mToDoData.set(mPosition, updatedText);
                mToDoListAdapter.notifyDataSetChanged();
        }

        hideKeyboard();
    }

    @Override
    public void onFinishEditDialog(String inputText)
    {
        if (inputText != null && inputText.length() > 0)
        {
                mToDoData.set(mPosition, inputText);
                mToDoListAdapter.notifyDataSetChanged();
        }

        hideKeyboard();
    }
}
