package pl.kowalecki.edug.Adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.R;

public class AgentFilesAdapter extends RecyclerView.Adapter<AgentFilesAdapter.ViewHolder> {

    private static final String TAG = AgentFilesAdapter.class.getSimpleName();
    private List<ListFile> filesArrayList = new ArrayList<>();
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_files_cardview, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListFile filesList = filesArrayList.get(position);

            Log.e(TAG, String.valueOf(filesArrayList.get(position)));


        holder.mFilename.setText(filesList.getFileData().getFilemane());

    }

    @Override
    public int getItemCount() {
        return filesArrayList.size();
    }

    public void setResults(List<ListFile> results){
        this.filesArrayList = results;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mFilename;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);

            mFilename = itemView.findViewById(R.id.agent_filename);
            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }

    }

}
