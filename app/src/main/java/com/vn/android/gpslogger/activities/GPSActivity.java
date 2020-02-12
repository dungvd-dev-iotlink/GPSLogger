package com.vn.android.gpslogger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.vn.android.gpslogger.fragments.FragmentGPSFix;
import com.vn.android.gpslogger.fragments.FragmentTrack;
import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.adapters.ViewPagerAdapter;
import com.vn.android.gpslogger.fragments.FragmentTracklist;

public class GPSActivity extends AppCompatActivity {
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private View bottomSheet;
  private BottomSheetBehavior mBottomSheetBehavior;
  Toast toastClickAgain;

  private int activeTab = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gps);

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
        updateBottomSheetPosition();
      }
    });

    bottomSheet = findViewById(R.id.bottomSheet);

    mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    mBottomSheetBehavior.setHideable(false);
    toastClickAgain = Toast.makeText(this, getString(R.string.toast_track_finished_click_again), Toast.LENGTH_SHORT);
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
      //Log.w("myApp", "[#] GPSActivity.java - mBottomSheetBehavior.setPeekHeight(" + bottomSheet.getHeight() +");");
      mBottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
    } else {
      mBottomSheetBehavior.setPeekHeight(1);
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED) ;
    }
  }
}