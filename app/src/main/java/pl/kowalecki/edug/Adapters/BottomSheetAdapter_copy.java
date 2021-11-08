package pl.kowalecki.edug.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.kowalecki.edug.R;

public class BottomSheetAdapter_copy extends RecyclerView.Adapter<BottomSheetAdapter_copy.BottomSheetViewHolder> {

    private ArrayList<String> mExampleMissionNumber;
    private String mExampleMissionType;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ mListener = listener;}

    public static class BottomSheetViewHolder extends RecyclerView.ViewHolder{

        public TextView mMissionNumber;
        public TextView mMissionType;

        public BottomSheetViewHolder(@NonNull View itemView, OnItemClickListener listener){
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

    public BottomSheetAdapter_copy(ArrayList<String> exampleMissionNumber, String exampleMissionType ){
        this.mExampleMissionNumber = exampleMissionNumber;
        this.mExampleMissionType = exampleMissionType;
    }

    @NonNull
    @Override
    public BottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_cardview, parent, false);
        BottomSheetViewHolder avh = new BottomSheetViewHolder(v, mListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetViewHolder holder, int position) {

        String currentMissionNumber = mExampleMissionNumber.get(position);
        String currentMissionType = mExampleMissionType;

        holder.mMissionNumber.setText(currentMissionNumber);
        holder.mMissionType.setText(currentMissionType);

    }

    @Override
    public int getItemCount() {
        return mExampleMissionNumber.size();
    }
}
