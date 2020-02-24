package com.vn.android.gpslogger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.vn.android.gpslogger.GPSApplication;
import com.vn.android.gpslogger.NameDialog;
import com.vn.android.gpslogger.SaveDatabaseTask;
import com.vn.android.gpslogger.fragments.FragmentGPSFix;
import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.adapters.ViewPagerAdapter;
import com.vn.android.gpslogger.fragments.FragmentTrackList;

import java.util.List;

public class GPSActivity extends AppCompatActivity implements LifecycleOwner, NameDialog.NameDialogListener {

  private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

  private final GPSApplication gpsApp = GPSApplication.getInstance();

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private View bottomSheet;
  private BottomSheetBehavior mBottomSheetBehavior;
  private LifecycleRegistry lifecycleRegistry;

  private int activeTab = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gps);

    Uri data = getIntent().getData();
    if (data != null) {
      String scheme = data.getScheme();
      String host = data.getHost();
      List<String> params = data.getPathSegments();
      if (params.size() > 0) {
        String first = params.get(0);
        String second = params.get(1);
        String third = params.get(2);
        Log.e("duydung", "scheme : " + scheme);
        Log.e("duydung", "host : " + host);
        Log.e("duydung", "first : " + first);
        Log.e("duydung", "second : " + second);
        Log.e("duydung", "third : " + third);
      }
    }
    lifecycleRegistry = new LifecycleRegistry(this);
    lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);

    viewPager = findViewById(R.id.viewPager);
    viewPager.setOffscreenPageLimit(3);
    setupViewPager(viewPager);

    tabLayout =  findViewById(R.id.tabLayout);
    tabLayout.setTabMode(TabLayout.MODE_FIXED);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        activeTab = tab.getPosition();
        gpsApp.setGpsActivity_activeTab(activeTab);
        updateBottomSheetPosition();
      }
    });

    bottomSheet = findViewById(R.id.bottomSheet);

    mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    mBottomSheetBehavior.setHideable(false);
    gpsApp.setGpsActivity(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
    activeTab = tabLayout.getSelectedTabPosition();
    gpsApp.setGpsActivity_activeTab(activeTab);
  }

  @Override
  protected void onResume() {
    super.onResume();
    lifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    gpsApp.onResume();
    // Check for Location runtime Permissions (for Android 23+)
    if (!gpsApp.isLocationPermissionChecked()) {
      checkLocationPermission();
      gpsApp.setLocationPermissionChecked(true);
    }
  }

  @Override
  protected void onDestroy() {
    lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    gpsApp.onDestroy();
    super.onDestroy();
  }

  @NonNull
  @Override
  public Lifecycle getLifecycle() {
    return lifecycleRegistry;
  }

  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new FragmentGPSFix(), getString(R.string.tab_gpsfix));
    adapter.addFragment(new FragmentTrackList(), getString(R.string.tab_tracklist));
    viewPager.setAdapter(adapter);
  }

  private void updateBottomSheetPosition() {
    activeTab = tabLayout.getSelectedTabPosition();
    if (activeTab != 1) {
      mBottomSheetBehavior.setPeekHeight(1);
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      mBottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
    } else {
      mBottomSheetBehavior.setPeekHeight(1);
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED) ;
    }
  }

  private boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      return true;    // Permission Granted
    } else {
      boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
          this, Manifest.permission.ACCESS_FINE_LOCATION);
      if (showRationale || !gpsApp.isLocationPermissionChecked()) {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS);
      }
      return false;
    }
  }

  public void hideBottomSheet() {
    if (bottomSheet.getVisibility() == View.VISIBLE) {
      bottomSheet.setVisibility(View.INVISIBLE);
    }
  }

  public void showBottomSheet() {
    if (bottomSheet.getVisibility() == View.INVISIBLE) {
      bottomSheet.setVisibility(View.VISIBLE);
    }
  }

  public void showDialogInputName() {
    NameDialog dialog = new NameDialog();
    dialog.setCancelable(false);
    dialog.show(getSupportFragmentManager(), "dialog");
  }

  public ViewPager getViewPager() {
    return viewPager;
  }

  public LifecycleOwner getLifeOwner() {
    return this;
  }

  @Override
  public void applyName(String name) {
    new SaveDatabaseTask(this).execute(name);
  }
}