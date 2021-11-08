package pl.kowalecki.edug.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Model.Achievements.Achievements;
import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Attendances.ExtraAttendance;
import pl.kowalecki.edug.R;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {

    private static final String TAG = AchievementsAdapter.class.getSimpleName();
    private List<ExtraAchievement> achievementsArrayList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_achievements, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExtraAchievement achievements = achievementsArrayList.get(position);
                holder.idm.setText(achievements.getAchievements().getIdm());
                holder.codename.setText(achievements.getAchievements().getCodename());
                holder.points.setText(achievements.getAchievements().getPoints());

        }

    @Override
    public int getItemCount() {
        return achievementsArrayList.size();
    }

    public void setResults(List<ExtraAchievement> results){
        this.achievementsArrayList = results;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView idm;
        private final TextView codename;
        private final TextView points;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            idm = itemView.findViewById(R.id.achievements_idm);
            codename = itemView.findViewById(R.id.achievements_codename);
            points = itemView.findViewById(R.id.achievements_points);
        }
    }


}
