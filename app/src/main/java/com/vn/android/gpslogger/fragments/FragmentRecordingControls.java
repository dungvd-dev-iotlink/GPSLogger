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
  TableLayout tableLayoutGeoPoints;
  TableLayout tableLayoutPlacemarks;

  private TextView tvGeoPoints;
  private TextView tvPlacemarks;
  private TextView tvGeoPointsLabel;
  private TextView tvPlacemarksLabel;

  public FragmentRecordingControls() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recording_controls, container, false);

    tableLayoutGeoPoints = (TableLayout) view.findViewById(R.id.tableLayoutGeoPoints);
    tableLayoutGeoPoints.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        ontoggleRecordGeoPoint(v);
      }
    });

    tableLayoutPlacemarks = (TableLayout) view.findViewById(R.id.tableLayoutPlacemarks);
    tableLayoutPlacemarks.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        onPlacemarkRequest(v);
      }
    });

    tvGeoPoints = view.findViewById(R.id.textViewGeoPoints);
    tvPlacemarks = view.findViewById(R.id.textViewPlacemarks);
    tvGeoPointsLabel = view.findViewById(R.id.textViewGeoPointsLabel);
    tvPlacemarksLabel = view.findViewById(R.id.textViewPlacemarksLabel);

    return view;
  }
}
