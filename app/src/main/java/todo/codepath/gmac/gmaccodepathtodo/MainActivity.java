package todo.codepath.gmac.gmaccodepathtodo;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ToDoListAdapter mToDoListAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_layout);
        init();
    }

    private void init()
    {
        Log.i(TAG, "init");
        lvItems = (ListView) findViewById(R.id.task_list_view);
        mToDoListAdapter = new ToDoListAdapter(this, R.layout.list_view_item);
        mToDoListAdapter.setListView(lvItems);
        lvItems.setAdapter(mToDoListAdapter);
        setupListViewListeners();
        //mToDoListAdapter.add("First Item");
        mToDoListAdapter.add("Second Item");

    }



    private void setupListViewListeners()
    {
        Log.i(TAG, "setupListViewListeners");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.i(TAG, "onItemLongClick");

                return false;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.i(TAG, "onItemClick");

            }
        });

        lvItems.setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent)
            {
                Log.i(TAG, "onDrag");
                return false;
            }
        });
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
}
