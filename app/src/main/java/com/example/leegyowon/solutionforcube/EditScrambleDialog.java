package com.example.leegyowon.solutionforcube;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EditScrambleDialog extends DialogFragment {

    public static final String SCRAMBLE_TAG = "scramble_tag";
    EditScrambleDialogListener editScrambleDialogListener;
    String scramble = "";

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        scramble = args.getString(SCRAMBLE_TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String currentScramble = getArguments().getString(SCRAMBLE_TAG);
        final View view = inflater.inflate(R.layout.edit_scramble_dialog_body, null);
        ((EditText) view.findViewById(R.id.scramble_editor)).setText(currentScramble);

        builder.setView(view)
                .setTitle("Edit Scramble")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        editScrambleDialogListener.onDialogPositiveClick(EditScrambleDialog.this,
                                ((EditText) view.findViewById(R.id.scramble_editor)).getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editScrambleDialogListener.onDialogNegativeClick(EditScrambleDialog.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            editScrambleDialogListener = (EditScrambleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EditScrambleDialogListener");
        }
    }

    public interface EditScrambleDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String scramble);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
