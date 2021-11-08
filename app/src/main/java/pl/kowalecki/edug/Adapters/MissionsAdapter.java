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

import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.R;

public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.ViewHolder> {

    private static final String TAG = MissionsAdapter.class.getSimpleName();
    private List<ListMission> listMissions = new ArrayList<>();
    private OnItemClickListener mListener;
    private Boolean b;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.missions_cardview, parent, false);
        ViewHolder lvh = new ViewHolder(v, mListener);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListMission model = listMissions.get(position);
        if (Integer.parseInt(model.getMission().getIdm())>=100){
            holder.mMissionNameText.setText("Misja Błyskawiczna");
            holder.mMissionImage.setImageResource(R.drawable.ic_instant_mission_menu);
        }
        if (Integer.parseInt(model.getMission().getIdm())>=200){
            holder.mMissionNameText.setText("Misja Laboratoryjna");
            holder.mMissionImage.setImageResource(R.drawable.ic_labo_mission_menu);
        }
        if (Integer.parseInt(model.getMission().getIdm())>=300){
            holder.mMissionNameText.setText("Misja Specjalna");
            holder.mMissionImage.setImageResource(R.drawable.ic_spec_mission_menu);
        }
        holder.mMissionIdu.setText(model.getMission().getIdm());

        if (b){
            holder.mMissionImage.getDrawable().getCurrent().setColorFilter(Color.parseColor("#bfbdc0"), PorterDuff.Mode.MULTIPLY);
        }else{
            holder.mMissionImage.getDrawable().getCurrent().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        }

    }

    @Override
    public int getItemCount() {
        return listMissions.size();
    }

    public void setResults(List<ListMission> results, boolean b){
        this.listMissions = results;
        this.b = b;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mMissionIdu;
        private final TextView mMissionNameText;
        private final ImageView mMissionImage;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            mMissionIdu = itemView.findViewById(R.id.mission_idu);
            mMissionNameText = itemView.findViewById(R.id.mission_type_name);
            mMissionImage = itemView.findViewById(R.id.mission_image);

            itemView.setOnClickListener(v -> {
                if (listener !=null ){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }

    }
}

