package sg.edu.tp.whisper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.Viewholder> {
    private RecyclerViewClickListener listener;
    private ArrayList<Song> songList;

    TracksAdapter(ArrayList<Song> songList, RecyclerViewClickListener listener) {
        this.songList = songList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracks_item_layout, viewGroup,false );
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        int resource = songList.get(position).getImageIcon();
        String title = songList.get(position).getTitle();
        String artiste = songList.get(position).getArtiste();
        viewholder.setData(resource, title, artiste);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView artiste;

        Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.songTitle);
            artiste = itemView.findViewById(R.id.artisteName);
            itemView.setOnClickListener(this);
        }

        private void setData(int imageResource, String songTitle, String artisteName ) {
            imageView.setImageResource(imageResource);
            title.setText(songTitle);
            artiste.setText(artisteName);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    public void filterList(ArrayList<Song> songList){
        this.songList = songList;
        notifyDataSetChanged();
    }

}
