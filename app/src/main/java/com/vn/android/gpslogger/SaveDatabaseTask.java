package com.vn.android.gpslogger;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.vn.android.gpslogger.activities.GPSActivity;
import com.vn.android.gpslogger.fragments.FragmentTrackList;

public class SaveDatabaseTask extends AsyncTask<String, Void, Boolean> {

  private GPSActivity activity;

  public SaveDatabaseTask(GPSActivity activity) {
    this.activity = activity;
  }

  @Override
  protected Boolean doInBackground(String... strings) {
    GPSApplication.getInstance().saveTrack(strings[0]);
    return true;
  }

  @Override
  protected void onPostExecute(Boolean aBoolean) {
    Toast.makeText(activity, "Saved !", Toast.LENGTH_SHORT).show();
    ViewPager viewPager = activity.getViewPager();
    FragmentTrackList fragmentTrackList = (FragmentTrackList) viewPager.getAdapter().instantiateItem(viewPager, 1);
    fragmentTrackList.updateUI();
  }
}
