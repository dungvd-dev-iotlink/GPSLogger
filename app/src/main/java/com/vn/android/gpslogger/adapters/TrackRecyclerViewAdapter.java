package com.vn.android.gpslogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vn.android.gpslogger.models.Track;
import com.vn.android.gpslogger.viewholders.TrackHolder;

import java.util.List;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackHolder> {
  private List<Track> tracks;
  private Context context;

  public TrackRecyclerViewAdapter(Context context, List<Track> tracks) {
    this.tracks = tracks;
    this.context = context;
  }

  @NonNull
  @Override
  public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    return new TrackHolder(layoutInflater, parent);
  }

  @Override
  public void onBindViewHolder(@NonNull TrackHolder holder, int position) {
    Track track = tracks.get(position);
    holder.bind(track);
  }

  @Override
  public int getItemCount() {
    return tracks.size();
  }

  public void setTracks(List<Track> tracks) {
    this.tracks = tracks;
  }
}
