package com.vn.android.gpslogger.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vn.android.gpslogger.GPSApplication;
import com.vn.android.gpslogger.R;

public class FragmentGPSFix extends Fragment {
  private FrameLayout flGPSFix;

  private TextView tvLatitude;
  private TextView tvLongitude;
  private TextView tvLatitudeUM;
  private TextView tvLongitudeUM;
  private TextView tvAltitude;
  private TextView tvAltitudeUM;
  private TextView tvSpeed;
  private TextView tvSpeedUM;
  private TextView tvBearing;
  private TextView tvAccuracy;
  private TextView tvAccuracyUM;
  private TextView tvGPSFixStatus;
  private TextView tvDirectionUM;
  private TextView tvTime;
  private TextView tvSatellites;

  private TableLayout tlCoordinates;
  private TableLayout tlAltitude;
  private TableLayout tlSpeed;
  private TableLayout tlBearing;
  private TableLayout tlAccuracy;
  private TableLayout tlTime;
  private TableLayout tlSatellites;

  private LinearLayout LLTimeSatellites;

  final GPSApplication gpsApplication = GPSApplication.getInstance();

  public FragmentGPSFix() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_gpsfix, container, false);

    // FrameLayouts
    flGPSFix = view.findViewById(R.id.fragmentGpsFixFrameLayout);

    // TextViews
    tvLatitude          = view.findViewById(R.id.textViewLatitude);
    tvLongitude         = view.findViewById(R.id.textViewLongitude);
    tvLatitudeUM        = view.findViewById(R.id.textViewLatitudeUM);
    tvLongitudeUM       = view.findViewById(R.id.textViewLongitudeUM);
    tvAltitude          = view.findViewById(R.id.textViewAltitude);
    tvAltitudeUM        = view.findViewById(R.id.textViewAltitudeUM);
    tvSpeed             = view.findViewById(R.id.textViewSpeed);
    tvSpeedUM           = view.findViewById(R.id.textViewSpeedUM);
    tvBearing           = view.findViewById(R.id.textViewBearing);
    tvAccuracy          = view.findViewById(R.id.textViewAccuracy);
    tvAccuracyUM        = view.findViewById(R.id.textViewAccuracyUM);
    tvGPSFixStatus      = view.findViewById(R.id.textViewGPSFixStatus);
    tvDirectionUM       = view.findViewById(R.id.textViewBearingUM);
    tvTime              = view.findViewById(R.id.textViewTime);
    tvSatellites        = view.findViewById(R.id.textViewSatellites);

    // TableLayouts
    tlCoordinates       = view.findViewById(R.id.tableLayoutCoordinates) ;
    tlAltitude          = view.findViewById(R.id.tableLayoutAltitude);
    tlSpeed             = view.findViewById(R.id.tableLayoutSpeed);
    tlBearing           = view.findViewById(R.id.tableLayoutBearing);
    tlAccuracy          = view.findViewById(R.id.tableLayoutAccuracy);
    tlTime              = view.findViewById(R.id.tableLayoutTime);
    tlSatellites        = view.findViewById(R.id.tableLayoutSatellites);

    // LinearLayouts
    LLTimeSatellites    = view.findViewById(R.id.linearLayoutTimeSatellites);

    tvGPSFixStatus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        if (GPSStatus == GPS_DISABLED) {
//          if (GPSApplication.getInstance().getLocationSettingsFlag()) {
//            // This is the second click
//            GPSApplication.getInstance().setLocationSettingsFlag(false);
//            // Go to Settings screen
//            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            if (callGPSSettingIntent != null) startActivityForResult(callGPSSettingIntent, 0);
//          } else {
//            GPSApplication.getInstance().setLocationSettingsFlag(true); // Start the timer
//          }
//        }
      }
    });
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    update();
  }

  private void update() {

  }
}
