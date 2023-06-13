package si.uni_lj.fe.tnuv.groupsound2_1;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistsAdapter extends ArrayAdapter<String> {
    
    private ArrayList<String> playlistItems;
    private MyplaylistsActivity activity;




    public PlaylistsAdapter(MyplaylistsActivity activity, ArrayList<String> playlistItems) {
        super(activity, 0, playlistItems);
        this.activity = activity;
        this.playlistItems = playlistItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_row, parent, false);
            holder = new ViewHolder();
            holder.playlistNameTextView = convertView.findViewById(R.id.tvPlaylistName);
            holder.btnSettings = convertView.findViewById(R.id.btnSettings);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String playlistName = getItem(position);
        holder.playlistNameTextView.setText(playlistName);

        // Set click listener for btnSettings
        holder.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showPlaylistOptions(playlistName);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView playlistNameTextView;
        ImageButton btnSettings;
    }

}