package com.vn.android.gpslogger.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vn.android.gpslogger.R;
import com.vn.android.gpslogger.database.DatabaseManager;
import com.vn.android.gpslogger.models.Track;
import com.vn.android.gpslogger.viewholders.TrackHolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackHolder> {
  private static final String JSON_CACHE_DIR = "json_cache";
  private static final String JSON_POSTFIX = "json";
  private List<Track> tracks;
  private Context context;
  private File jsonFile;

  // instance for fire base storage and StorageReference
  FirebaseStorage storage;
  StorageReference storageReference;

  public TrackRecyclerViewAdapter(Context context, List<Track> tracks) {
    this.tracks = tracks;
    this.context = context;
    // get the fire base storage reference
    storage = FirebaseStorage.getInstance();
    storageReference = storage.getReference();
  }

  @NonNull
  @Override
  public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    return new TrackHolder(layoutInflater, parent);
  }

  @Override
  public void onBindViewHolder(@NonNull TrackHolder holder, final int position) {
    final Track track = tracks.get(position);
    holder.bind(track);
    holder.getItemListLayout().setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        TextView noticeContent = dialogView.findViewById(R.id.noticeContent);
        noticeContent.setText(R.string.notice_upload_content);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });

        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
            String json = track.toJSon();
            saveJSonFileToCache(track.getName(), json);
            uploadJsonToFireBase(track.getName(), position);
          }
        });
        return false;
      }
    });

    holder.getButtonDel().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        TextView noticeContent = dialogView.findViewById(R.id.noticeContent);
        noticeContent.setText(R.string.notice_delete_content);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });

        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
            deleteItem(position);
          }
        });
      }
    });
  }

  @Override
  public int getItemCount() {
    return tracks.size();
  }

  public void setTracks(List<Track> tracks) {
    this.tracks = tracks;
  }

  private void deleteItem(int position) {
    DatabaseManager.getInstance(context).deleteTrack(tracks.get(position).getId());
    tracks.remove(position);
    notifyDataSetChanged();
  }

  private void saveJSonFileToCache(String name, String json) {
    File dir = new File(context.getCacheDir(), JSON_CACHE_DIR);
    if (!dir.exists()) {
      dir.mkdir();
    }
    jsonFile = new File(dir, name + "." + JSON_POSTFIX);
    if (jsonFile.exists()) {
      jsonFile.delete();
    }

    try {
      if (!jsonFile.createNewFile()) {
        Log.e("Error", "Can not create file JSON !");
      }
      PrintWriter printWriter = new PrintWriter(jsonFile);
      BufferedWriter bufferedWriter = new BufferedWriter(printWriter);
      bufferedWriter.write(json);
      bufferedWriter.close();
      printWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void uploadJsonToFireBase(String name, final int position) {
    if (jsonFile != null) {
      final ProgressDialog progressDialog = new ProgressDialog(context);
      progressDialog.setTitle(context.getString(R.string.uploading));
      progressDialog.show();

      // Defining the child of storageReference
      StorageReference ref = storageReference.child(JSON_POSTFIX + "/" + name + "." + JSON_POSTFIX);

      // adding listeners on upload or failure of image
      UploadTask updateTask = ref.putFile(Uri.fromFile(jsonFile));
      updateTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          progressDialog.dismiss();
          Toast.makeText(context, context.getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
        }
      });

      updateTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
          progressDialog.dismiss();
          deleteItem(position);
          jsonFile.delete();
          Toast.makeText(context, context.getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
        }
      });
      updateTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
          double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
          progressDialog.setMessage(context.getString(R.string.upload_progress, ((int) progress) + "") + " %");
        }
      });
    }
  }
}
