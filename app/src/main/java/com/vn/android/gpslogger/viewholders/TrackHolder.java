package com.vn.android.gpslogger.viewholders;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.models.Track;

public class TrackHolder extends RecyclerView.ViewHolder {

  private TextView tvName;
  private TextView tvTime;
  private ImageButton buttonDel;
  private RelativeLayout itemListLayout;

  public TrackHolder(LayoutInflater inflater, ViewGroup parent) {
    super(inflater.inflate(R.layout.list_item_track, parent, false));
    itemListLayout = itemView.findViewById(R.id.list_item_track_layout);
    tvName = itemView.findViewById(R.id.tvName);
    tvTime = itemView.findViewById(R.id.tvTime);
    buttonDel = itemView.findViewById(R.id.buttonDel);
  }

  public void bind(Track track) {
    if (!track.getName().isEmpty()) {
      tvName.setText(track.getName());
    }
    else {
      tvName.setText(R.string.no_name);
    }
    tvTime.setText(track.getDate());
  }

  public RelativeLayout getItemListLayout() {
    return itemListLayout;
  }

  public ImageButton getButtonDel() {
    return buttonDel;
  }
}
