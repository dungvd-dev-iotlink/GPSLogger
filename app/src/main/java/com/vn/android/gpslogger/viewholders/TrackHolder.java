package com.vn.android.gpslogger.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.models.Track;

public class TrackHolder extends RecyclerView.ViewHolder {

  private Context context;
  private TextView tvName;
  private TextView tvTime;
  private TextView tvNumberPoint;
  private ImageButton buttonUpload;
  private RelativeLayout itemListLayout;

  public TrackHolder(Context context, LayoutInflater inflater, ViewGroup parent) {
    super(inflater.inflate(R.layout.list_item_track, parent, false));
    this.context = context;
    itemListLayout = itemView.findViewById(R.id.list_item_track_layout);
    tvName = itemView.findViewById(R.id.tvName);
    tvTime = itemView.findViewById(R.id.tvTime);
    tvNumberPoint = itemView.findViewById(R.id.tvNumberPoint);
    buttonUpload = itemView.findViewById(R.id.buttonUpload);
  }

  public void bind(Track track) {
    if (!track.getName().isEmpty()) {
      tvName.setText(track.getName());
    }
    else {
      tvName.setText(R.string.no_name);
    }
    tvTime.setText(track.getDate());
    tvNumberPoint.setText(context.getString(R.string.item_points, track.getPointList().size() + ""));
    if (track.isUploaded()) {
      buttonUpload.setVisibility(View.GONE);
    }
    else {
      buttonUpload.setVisibility(View.VISIBLE);
    }
  }

  public RelativeLayout getItemListLayout() {
    return itemListLayout;
  }

  public ImageButton getButtonUpload() {
    return buttonUpload;
  }
}
