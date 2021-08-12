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

public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.MissionsViewHolder> {

    private ArrayList<String> mListMissions;
    private OnItemClickListener mListener;
    private Boolean b;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){mListener = listener;}

    public static class MissionsViewHolder extends RecyclerView.ViewHolder {
        public TextView mMissionIdu;
        public TextView mMissionNameText;
        public ImageView mMissionImage;

        public MissionsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
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


    public MissionsAdapter(ArrayList<String> listMissions, boolean b) {
        this.mListMissions = listMissions;
        this.b=b;
    }

    @NonNull
    @Override
    public MissionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.missions_cardview, parent, false);
        MissionsViewHolder lvh = new MissionsViewHolder(v, mListener);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MissionsViewHolder holder, int position) {
        String currentIdu = mListMissions.get(position);
        if (Integer.parseInt(currentIdu)>=100){
            holder.mMissionNameText.setText("Misja Błyskawiczna");
            holder.mMissionImage.setImageResource(R.drawable.ic_instant_mission_menu);
        }
        if (Integer.parseInt(currentIdu)>=200){
            holder.mMissionNameText.setText("Misja Laboratoryjna");
            holder.mMissionImage.setImageResource(R.drawable.ic_labo_mission_menu);
        }
        if (Integer.parseInt(currentIdu)>=300){
            holder.mMissionNameText.setText("Misja Specjalna");
            holder.mMissionImage.setImageResource(R.drawable.ic_spec_mission_menu);
        }
        holder.mMissionIdu.setText(currentIdu);

        if (b){
            holder.mMissionImage.getDrawable().getCurrent().setColorFilter(Color.parseColor("#bfbdc0"), PorterDuff.Mode.MULTIPLY);
        }else{

            holder.mMissionImage.getDrawable().getCurrent().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        }




    }

    @Override
    public int getItemCount() {
        return mListMissions.size();
    }




}

