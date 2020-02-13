package com.vn.android.gpslogger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.vn.android.gpslogger.GPSApplication;
import com.vn.android.gpslogger.fragments.FragmentGPSFix;
import com.vn.android.gpslogger.fragments.FragmentTrack;
import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.adapters.ViewPagerAdapter;
import com.vn.android.gpslogger.fragments.FragmentTracklist;
import com.vn.android.gpslogger.location.LocationListener;
import com.vn.android.gpslogger.models.GPSViewModel;

import java.util.ArrayList;
import java.util.List;

public class GPSActivity extends AppCompatActivity implements LifecycleOwner {
  private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

  private final GPSApplication gpsApp = GPSApplication.getInstance();

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private View bottomSheet;
  private BottomSheetBehavior mBottomSheetBehavior;
  Toast toastClickAgain;
  private LifecycleRegistry lifecycleRegistry;
  private GPSViewModel gpsViewModel;

  private int activeTab = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gps);

    gpsViewModel = ViewModelProviders.of(this).get(GPSViewModel.class);

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
    toastClickAgain = Toast.makeText(this, getString(R.string.toast_track_finished_click_again), Toast.LENGTH_SHORT);

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
    adapter.addFragment(new FragmentTrack(), getString(R.string.tab_track));
    adapter.addFragment(new FragmentTracklist(), getString(R.string.tab_tracklist));
    viewPager.setAdapter(adapter);
  }

  private void updateBottomSheetPosition() {
    activeTab = tabLayout.getSelectedTabPosition();
    if (activeTab != 2) {
      mBottomSheetBehavior.setPeekHeight(1);
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      mBottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
    } else {
      mBottomSheetBehavior.setPeekHeight(1);
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED) ;
    }
  }

  public boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      return true;    // Permission Granted
    } else {
      boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
          this, Manifest.permission.ACCESS_FINE_LOCATION);
      if (showRationale || !gpsApp.isLocationPermissionChecked()) {
        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(
            this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]) , REQUEST_ID_MULTIPLE_PERMISSIONS);
      }
      return false;
    }
  }

  public LifecycleOwner getLifeOwner() {
    return this;
  }
}