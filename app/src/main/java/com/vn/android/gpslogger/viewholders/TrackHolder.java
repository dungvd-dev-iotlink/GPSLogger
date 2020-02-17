package com.vn.android.gpslogger.viewholders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.models.Track;

public class TrackHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private TextView tvName;
  private TextView tvTime;
  private Track track;

  public TrackHolder(LayoutInflater inflater, ViewGroup parent) {
    super(inflater.inflate(R.layout.list_item_track, parent, false));
    tvName = itemView.findViewById(R.id.tvName);
    tvTime = itemView.findViewById(R.id.tvTime);
    itemView.setOnClickListener(this);
  }

  public void bind(Track track) {
    this.track = track;
    if (!track.getName().isEmpty()) {
      tvName.setText(track.getName());
    }
    else {
      tvName.setText(R.string.no_name);
    }
    tvTime.setText(track.getDate());
  }

  @Override
  public void onClick(View v) {
    Log.e("duydung", "Dang bam vao item thu: ");
  }
}
