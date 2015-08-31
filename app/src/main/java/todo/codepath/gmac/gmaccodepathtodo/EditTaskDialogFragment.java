package todo.codepath.gmac.gmaccodepathtodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DateTimeView;
import android.widget.EditText;
import android.widget.TextView;

public class EditTaskDialogFragment extends DialogFragment implements TextView.OnEditorActionListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ToDoItem mToDoItem = new ToDoItem("", "");
    private EditText mEditText;
    private DateTimeView mDateTimeView;
    private int mDialogReason;


    public interface EditTaskDialogListener
    {
        void onFinishEditDialog(final ToDoItem toDoItem);
        void onFinishAddDialog(final ToDoItem toDoItem);
    }

    public EditTaskDialogFragment()
    {
    }

    public static EditTaskDialogFragment newInstance(final Bundle args)
    {
        final EditTaskDialogFragment dialogFragment = new EditTaskDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogView = View.inflate(getActivity(), R.layout.fragment_edit_task, null);
        mEditText = (EditText) dialogView.findViewById(R.id.edit_task_view);
        mEditText.setOnEditorActionListener(this);

        final Bundle args = getArguments();
        final String entry = args.getString(getString(R.string.current_entry_task_key), "");
        final String title = args.getString(getString(R.string.dialog_title_key));
        if (title == null)
        {
            Log.e(TAG, "Dialog needs a title!");
            return null;
        }

        mDialogReason = args.getInt(getString(R.string.dialog_reason_key), -1);
        mEditText.setText(entry);
        if (entry!= null && entry.equals(""))
        {
            mEditText.setHint(args.getString(getString(R.string.hint_key), getString(R.string.hint_text)));
        }

        builder.setTitle(title)
                .setView(dialogView)
                .setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        if (mToDoItem == null)
                        {
                            mToDoItem = new ToDoItem(null, null);
                        }

                        final EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
                        if (mDialogReason == Utils.DialogReason.ADD.ordinal())
                        {
                            mToDoItem.setTask(mEditText.getText().toString());
                            // mToDoItem.setDateTime();
                            listener.onFinishAddDialog(mToDoItem);
                        }
                        else if (mDialogReason == Utils.DialogReason.EDIT.ordinal())
                        {
                            mToDoItem.setTask(mEditText.getText().toString());
                            listener.onFinishEditDialog(mToDoItem);
                        }
                    }
                });


        mEditText.requestFocus();
        // Show Keyboard when editText in dialog is in focus
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
    }
    // Fires whenever the text-field has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (EditorInfo.IME_ACTION_DONE == actionId)
        {
            // Return input text to activity
            EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
            if (mDialogReason == Utils.DialogReason.ADD.ordinal())
            {
                mToDoItem.setTask(mEditText.getText().toString());
               // mToDoItem.setDateTime();
                listener.onFinishAddDialog(mToDoItem);
            }
            else if (mDialogReason == Utils.DialogReason.EDIT.ordinal())
            {
                mToDoItem.setTask(mEditText.getText().toString());
                listener.onFinishEditDialog(mToDoItem);
            }
            dismiss();
            return true;
        }

        return false;
    }
}
