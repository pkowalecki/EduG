package pl.kowalecki.edug.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.R;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {

    private static final String TAG = BottomSheetAdapter.class.getSimpleName();
    private List<ListMission> listMissions = new ArrayList<>();
    private String mExampleMissionType;
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_cardview, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListMission model = listMissions.get(position);

        holder.mMissionNumber.setText(model.getMission().getIdm());
        holder.mMissionType.setText(mExampleMissionType);

    }

    @Override
    public int getItemCount() {
        return listMissions.size();
    }

    public void setResults(List<ListMission> results, String mExampleMissionType){
            this.listMissions = results;
            this.mExampleMissionType = mExampleMissionType;
            notifyDataSetChanged();
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ mListener = listener;}

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mMissionNumber;
        private final TextView mMissionType;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);

            mMissionNumber = itemView.findViewById(R.id.mission_number_bottomSheet);
            mMissionType = itemView.findViewById(R.id.mission_type_bottomSheet);

            itemView.setOnClickListener(v -> {
                if (listener !=null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }

    }

}
