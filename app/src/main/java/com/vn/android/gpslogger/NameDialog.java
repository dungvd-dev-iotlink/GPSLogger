package com.vn.android.gpslogger;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NameDialog extends AppCompatDialogFragment {
  private EditText editTextName;
  private NameDialogListener listener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.name_dialog, null);

    editTextName = view.findViewById(R.id.editName);

    builder.setView(view)
        .setTitle(R.string.enter_track_name)
        .setPositiveButton(R.string.button_text, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            String name = editTextName.getText().toString();
            if (name.isEmpty()) {
              name = getString(R.string.no_name);
            }
            listener.applyName(name);
          }
        });
    return builder.create();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    listener = (NameDialogListener) context;
  }

  public interface NameDialogListener {
    void applyName(String name);
  }
}
