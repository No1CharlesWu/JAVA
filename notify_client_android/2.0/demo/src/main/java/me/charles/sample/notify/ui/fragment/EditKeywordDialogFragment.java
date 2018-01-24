package me.charles.sample.notify.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import me.charles.fragmentation.SupportFragment;
import me.charles.sample.R;
import me.charles.sample.notify.ui.fragment.third.PersonalSettingFragment;

/**
 * Created by charles on 23/01/2018.
 */

public class EditKeywordDialogFragment extends DialogFragment {
    private EditText mEditText;
    private static SupportFragment mfragment;

    public interface KeywordInputListener
    {
        void onKeywordInputComplete(int keyword, SupportFragment fragment);
    }

    public EditKeywordDialogFragment() {}

    public static EditKeywordDialogFragment newInstance(SupportFragment fragment) {
        EditKeywordDialogFragment frag = new EditKeywordDialogFragment();
        mfragment = fragment;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        mEditText = (EditText) view.findViewById(R.id.id_ed_keyword);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                try{
                                    int keyword = Integer.valueOf(mEditText.getText().toString());
                                    if (keyword == 6666) {
                                        Toast.makeText(getContext(), "口令正确！",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getContext(), "口令错误！",Toast.LENGTH_SHORT).show();
                                    }
                                    KeywordInputListener listener = (KeywordInputListener) getTargetFragment();
                                    listener.onKeywordInputComplete(keyword, mfragment);
                                }
                                catch (Exception e)
                                {
                                    KeywordInputListener listener = (KeywordInputListener) getTargetFragment();
                                    listener.onKeywordInputComplete(0, mfragment);
                                    Toast.makeText(getContext(), e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel", null);
        return builder.create();
    }
}
