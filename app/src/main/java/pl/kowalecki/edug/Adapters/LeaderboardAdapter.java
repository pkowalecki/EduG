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
import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.R;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private static final String TAG = LeaderboardAdapter.class.getSimpleName();
    private List<ExtraLeaderboard> listLeaderboards = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExtraLeaderboard extraLeaderboard = listLeaderboards.get(position);

        holder.mLeaderboardAgentName.setText("Agent " + extraLeaderboard.getPosition().getIdu());
        holder.mLeaderboardAgentPts.setText(extraLeaderboard.getPosition().getPoints());

    }

    @Override
    public int getItemCount() {
        return listLeaderboards.size();
    }

    public void setResults(List<ExtraLeaderboard> results){
        this.listLeaderboards = results;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        private final TextView mLeaderboardAgentName;
        private final TextView mLeaderboardAgentPts;;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLeaderboardAgentName = itemView.findViewById(R.id.leaderboard_agent);
            mLeaderboardAgentPts = itemView.findViewById(R.id.leaderboard_agent_pts);
        }
    }

}
