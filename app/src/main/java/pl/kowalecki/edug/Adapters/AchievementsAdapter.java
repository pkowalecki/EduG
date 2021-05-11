package pl.kowalecki.edug.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.kowalecki.edug.R;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementsViewHolder> {
    private ArrayList<String> mAchievementIdm;
    private ArrayList<String> mAchievementCodename;
    private ArrayList<String> mAchievementPoint;

    public static class AchievementsViewHolder extends RecyclerView.ViewHolder{
        public TextView mIdm;
        public TextView mCodename;
        public TextView mPoints;

        public AchievementsViewHolder(@NonNull View itemView){
            super(itemView);

            mIdm = itemView.findViewById(R.id.achievements_idm);
            mCodename = itemView.findViewById(R.id.achievements_codename);
            mPoints = itemView.findViewById(R.id.achievements_points);
        }
    }

    public AchievementsAdapter(ArrayList<String> achievementIdm, ArrayList<String> achievementCodename, ArrayList<String> achievementPoint){
        this.mAchievementIdm = achievementIdm;
        this.mAchievementCodename = achievementCodename;
        this.mAchievementPoint = achievementPoint;
    }

    @NonNull
    @Override
    public AchievementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_achievements, parent, false);
        AchievementsViewHolder evh = new AchievementsViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsViewHolder holder, int position) {
        String idm = mAchievementIdm.get(position);
        String codename = mAchievementCodename.get(position);
        String points = mAchievementPoint.get(position);


        holder.mIdm.setText(idm);
        holder.mCodename.setText(codename);
        holder.mPoints.setText(points);
    }

    @Override
    public int getItemCount() {
        return mAchievementIdm.size();
    }





}
