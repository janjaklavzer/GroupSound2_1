package si.uni_lj.fe.tnuv.groupsound2_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<String> {

    private ArrayList<String> songItems;
    private PlaylistActivity activity;




    public SongAdapter(PlaylistActivity activity, ArrayList<String> songItems) {
        super(activity, 0, songItems);
        this.activity = activity;
        this.songItems = songItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_row, parent, false);
            holder = new ViewHolder();
            holder.songNameTextView = convertView.findViewById(R.id.tvSongName);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String songName = getItem(position);

        holder.songNameTextView.setText(songName);

        // Set click listener for btnSettings
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                activity.deleteSong(songName);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView songNameTextView;
        ImageButton btnDelete;
    }

}