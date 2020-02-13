package com.vn.android.gpslogger.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProviders;

import com.vn.android.gpslogger.GPSApplication;
import com.vn.android.gpslogger.LocationSubscriber;
import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.activities.GPSActivity;
import com.vn.android.gpslogger.location.LocationListener;
import com.vn.android.gpslogger.models.GPSViewModel;

import java.text.DecimalFormat;

public class FragmentGPSFix extends Fragment implements LocationListener {
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

  private TableLayout tlCoordinates;
  private TableLayout tlAltitude;
  private TableLayout tlSpeed;
  private TableLayout tlBearing;
  private TableLayout tlAccuracy;
  private TableLayout tlTime;

  private LinearLayout LLTimeSatellites;

  private GPSApplication gpsApplication;
  private GPSViewModel gpsViewModel;
  private GPSActivity activity;

  public FragmentGPSFix() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    activity = (GPSActivity) context;
    gpsViewModel = ViewModelProviders.of(activity).get(GPSViewModel.class);
    subscribeViewModels();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    gpsApplication = GPSApplication.getInstance();
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

    // TableLayouts
    tlCoordinates       = view.findViewById(R.id.tableLayoutCoordinates) ;
    tlAltitude          = view.findViewById(R.id.tableLayoutAltitude);
    tlSpeed             = view.findViewById(R.id.tableLayoutSpeed);
    tlBearing           = view.findViewById(R.id.tableLayoutBearing);
    tlAccuracy          = view.findViewById(R.id.tableLayoutAccuracy);
    tlTime              = view.findViewById(R.id.tableLayoutTime);

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
    Location location = gpsApplication.getLocation();
    if (location != null) {
      DecimalFormat decimalFormatLong = new DecimalFormat("0.000000");
      DecimalFormat decimalFormatShort = new DecimalFormat("0.000");

      tvLatitude.setText(decimalFormatLong.format(location.getLatitude()));
      tvLongitude.setText(decimalFormatLong.format(location.getLongitude()));
      tvLatitudeUM.setText(decimalFormatLong.format(location.getLatitude()));
      tvLongitudeUM.setText(decimalFormatLong.format(location.getLongitude()));
      tvAltitude.setText(decimalFormatShort.format(location.getAltitude()));
      tvAltitudeUM.setText(decimalFormatShort.format(location.getAltitude()));
      tvSpeed.setText(decimalFormatShort.format(location.getSpeed()));
      tvSpeedUM.setText(decimalFormatShort.format(location.getSpeed()));
      tvBearing.setText(decimalFormatShort.format(location.getBearing()));
      tvAccuracy.setText(decimalFormatShort.format(location.getAccuracy()));
      tvAccuracyUM.setText(decimalFormatShort.format(location.getAccuracy()));
      tvTime.setText(location.getTime() + "");

      // Colorize the Altitude textview depending on the altitude EGM Correction
//      isValidAltitude = EGMAltitudeCorrection && (location.getAltitudeEGM96Correction() != NOT_AVAILABLE);
//      tvAltitude.setTextColor(isValidAltitude ? getResources().getColor(R.color.textColorPrimary) : getResources().getColor(R.color.textColorSecondary));
//      tvAltitudeUM.setTextColor(isValidAltitude ? getResources().getColor(R.color.textColorPrimary) : getResources().getColor(R.color.textColorSecondary));

      tvGPSFixStatus.setVisibility(View.GONE);

      tvDirectionUM.setVisibility(/*prefDirections == 0 ? View.GONE :*/ View.VISIBLE);
      tlTime.setVisibility(View.VISIBLE);

      tlCoordinates.setVisibility( View.VISIBLE);
      tlAltitude.setVisibility(View.VISIBLE);
      tlSpeed.setVisibility(View.VISIBLE);
      tlBearing.setVisibility(View.VISIBLE);
      tlAccuracy.setVisibility(View.VISIBLE);
      tlTime.setVisibility(View.VISIBLE);

      flGPSFix.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
          flGPSFix.removeOnLayoutChangeListener(this);

          int ViewHeight = tlTime.getMeasuredHeight() + (int) (6 * getResources().getDisplayMetrics().density);
          int LayoutHeight = flGPSFix.getHeight() - (int) (6 * getResources().getDisplayMetrics().density);
          boolean isTimeAndSatellitesVisible;
          if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isTimeAndSatellitesVisible = LayoutHeight >= 6 * ViewHeight;
          } else {
            isTimeAndSatellitesVisible = LayoutHeight >= 4 * ViewHeight;
          }
          LLTimeSatellites.setVisibility(isTimeAndSatellitesVisible ? View.VISIBLE : View.GONE);
        }
      });

    } else {
      tlCoordinates.setVisibility(View.INVISIBLE);
      tlAltitude.setVisibility(View.INVISIBLE);
      tlSpeed.setVisibility(View.INVISIBLE);
      tlBearing.setVisibility(View.INVISIBLE);
      tlAccuracy.setVisibility(View.INVISIBLE);
      tlTime.setVisibility(View.INVISIBLE);

      tvGPSFixStatus.setVisibility(View.VISIBLE);
    }
    Log.e("duydung", "update Location: " + location);
  }

  private void subscribeViewModels() {
    new LocationSubscriber(activity.getLifeOwner(), gpsViewModel, this).subscribe();
  }

  @Override
  public void onUpdatedLocation(Location location) {
    update();
  }
}
