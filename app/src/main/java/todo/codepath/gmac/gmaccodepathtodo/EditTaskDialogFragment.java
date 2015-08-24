package todo.codepath.gmac.gmaccodepathtodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class EditTaskDialogFragment extends DialogFragment implements TextView.OnEditorActionListener
{
    private EditText mEditText;
    private EditTaskDialogListener mDialogListener;


    public interface EditTaskDialogListener
    {
        public void onDialogPositiveClick(final DialogFragment dialog, final Object text);
        void onFinishEditDialog(String inputText);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mDialogListener = (EditTaskDialogListener) activity;
        }
        catch (ClassCastException e)
        {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditTextDialogListener");
        }
    }

    public EditTaskDialogFragment()
    {
    }

    public static EditTaskDialogFragment newInstance(final Context context, final String currentEntry)
    {
        final EditTaskDialogFragment dialogFragment = new EditTaskDialogFragment();
        final Bundle args = new Bundle();
        args.putString(context.getString(R.string.current_entry_key), currentEntry);
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
        final String entry = args.getString(getString(R.string.current_entry_key), null);
        mEditText.setText(entry);
        builder.setTitle("Edit Task ")
                .setView(dialogView)
                .setPositiveButton(context.getString(R.string.save), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        mDialogListener.onDialogPositiveClick(EditTaskDialogFragment.this, mEditText.getText().toString());
                    }
                }).create();

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
            listener.onFinishEditDialog(mEditText.getText().toString());
            dismiss();
            return true;
        }

        return false;
    }
}
