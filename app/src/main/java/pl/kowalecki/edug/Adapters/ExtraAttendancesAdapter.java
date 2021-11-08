package pl.kowalecki.edug.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Attendances.ExtraAttendance;
import pl.kowalecki.edug.R;

public class ExtraAttendancesAdapter extends RecyclerView.Adapter<ExtraAttendancesAdapter.ViewHolder> {

    private static final String TAG = ExtraAttendancesAdapter.class.getSimpleName();
    private List<ExtraAttendance> attendanceArrayList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_extraattendances, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExtraAttendance attendance = attendanceArrayList.get(position);
        holder.mClassDate.setText(attendance.getAttendance().getData());
        holder.mPoints.setText("3");

    }

    @Override
    public int getItemCount() {
        return attendanceArrayList.size();
    }

    public void setResults(List<ExtraAttendance> results){
        this.attendanceArrayList = results;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mClassDate;
        private final TextView mPoints;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            mClassDate = itemView.findViewById(R.id.extra_attendances_day);
            mPoints = itemView.findViewById(R.id.extra_attendances_pts);

        }
    }





}
