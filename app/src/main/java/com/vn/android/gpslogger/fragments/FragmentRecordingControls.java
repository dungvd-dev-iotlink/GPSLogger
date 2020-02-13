package com.vn.android.gpslogger.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vn.android.gpslogger.R;

public class FragmentRecordingControls extends Fragment {
  private TableLayout tableLayoutRecording;
  private TextView tvRecording;

  public FragmentRecordingControls() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recording_controls, container, false);

    tableLayoutRecording = view.findViewById(R.id.tableLayoutRecording);
    tableLayoutRecording.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onToggleRecord(v);
      }
    });

    tvRecording = view.findViewById(R.id.tvRecording);

    return view;
  }

  private void onToggleRecord(View v) {
    if (tvRecording.getText().equals(getString(R.string.startRecord))) {
      tvRecording.setText(R.string.stopRecord);
    }
    else {
      tvRecording.setText(R.string.startRecord);
    }
  }
}
