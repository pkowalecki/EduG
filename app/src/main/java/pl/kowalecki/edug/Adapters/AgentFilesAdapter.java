package pl.kowalecki.edug.Adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import pl.kowalecki.edug.R;

public class AgentFilesAdapter extends RecyclerView.Adapter<AgentFilesAdapter.AgentfileViewHolder> {

    private ArrayList<String> mExampleFilename;
    private ArrayList<String> mExampleUrl;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class AgentfileViewHolder extends RecyclerView.ViewHolder{

        public TextView mFilename;

        public AgentfileViewHolder(@NonNull View itemView, OnItemClickListener listener){
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

    public AgentFilesAdapter(ArrayList<String> exampleFilename, ArrayList<String> exampleUrl){
        this.mExampleFilename = exampleFilename;
        this.mExampleUrl = exampleUrl;
    }

    @NonNull
    @Override
    public AgentfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_files_cardview, parent, false);
        AgentfileViewHolder avh = new AgentfileViewHolder(v, mListener);
        return  avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AgentfileViewHolder holder, int position) {
        String currentFilename = mExampleFilename.get(position);
        holder.mFilename.setText(currentFilename);
    }

    @Override
    public int getItemCount() {
        return mExampleFilename.size();
    }



}
