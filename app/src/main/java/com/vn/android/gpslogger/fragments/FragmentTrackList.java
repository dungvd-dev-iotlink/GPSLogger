package com.vn.android.gpslogger.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.android.gpslogger.GPSApplication;
import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.adapters.TrackRecyclerViewAdapter;
import com.vn.android.gpslogger.database.DatabaseManager;
import com.vn.android.gpslogger.models.Track;

import java.util.List;

public class FragmentTrackList extends Fragment {

  private RecyclerView recyclerView;
  private TrackRecyclerViewAdapter adapter;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tracklist, container, false);
    recyclerView = view.findViewById(R.id.track_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateUI();
    return view;
  }

  public void updateUI() {
    List<Track> tracks = DatabaseManager.getInstance(getContext()).getAllTracks();
    if (tracks == null) {
      return;
    }
    if (adapter == null) {
      adapter = new TrackRecyclerViewAdapter(getContext(), tracks);
      recyclerView.setAdapter(adapter);
    }
    else {
      adapter.setTracks(tracks);
      adapter.notifyDataSetChanged();
    }
  }
}
