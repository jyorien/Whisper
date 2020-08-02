package sg.edu.tp.whisper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private final boolean showSongTitle;
    private Context context;
    private ArrayList<Song> songList;
    private RecyclerViewClickListener listener;

    public HorizontalAdapter(Context context, ArrayList<Song> songList, boolean showSongTitle, RecyclerViewClickListener listener) {
        this.context = context;
        this.songList = songList;
        this.showSongTitle = showSongTitle;
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView title;
        private TextView artiste;

        public ViewHolder(@NonNull View itemView) {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_item,parent,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int resource = songList.get(position).getImageIcon();
        String title = songList.get(position).getTitle();
        String artiste = songList.get(position).getArtiste();
        holder.setData(resource, title, artiste);
        if(showSongTitle) {
            holder.setData(resource, title, artiste);
        }
        else {
            title = "";
            holder.setData(resource, title, artiste);
        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
