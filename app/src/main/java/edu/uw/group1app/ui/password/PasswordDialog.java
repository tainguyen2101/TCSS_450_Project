package edu.uw.group1app.ui.password;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PasswordDialog extends AppCompatDialogFragment {

    private String myMessage;

    private String myTitle;

    public PasswordDialog(String theTitle, String theMessage) {
        myTitle = theTitle;
        myMessage = theMessage;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(myTitle)
                .setMessage(myMessage)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
