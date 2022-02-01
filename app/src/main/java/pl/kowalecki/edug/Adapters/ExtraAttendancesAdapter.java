package pl.kowalecki.edug.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.kowalecki.edug.R;

public class ExtraAttendancesAdapter extends RecyclerView.Adapter<ExtraAttendancesAdapter.ExtraAttendancesViewHolder> {
    private ArrayList<String> mAttendancesDate;

    public static class ExtraAttendancesViewHolder extends RecyclerView.ViewHolder{
        public TextView mClassDate;
        public TextView mPoints;

        public ExtraAttendancesViewHolder(@NonNull View itemView){
            super(itemView);

            mClassDate = itemView.findViewById(R.id.extra_attendances_day);
            mPoints = itemView.findViewById(R.id.extra_attendances_pts);
        }
    }

    public ExtraAttendancesAdapter(ArrayList<String> attendancesDate){
        this.mAttendancesDate = attendancesDate;
    }

    @NonNull
    @Override
    public ExtraAttendancesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_extraattendances, parent, false);
        ExtraAttendancesViewHolder evh = new ExtraAttendancesViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraAttendancesViewHolder holder, int position) {
        String currentDate = mAttendancesDate.get(position);
        String points = mAttendancesDate.get(position);
        holder.mClassDate.setText(currentDate);
        holder.mPoints.setText("2");
    }

    @Override
    public int getItemCount() {
        return mAttendancesDate.size();
    }





}
