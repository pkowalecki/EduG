package pl.kowalecki.edug.Adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.R;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private ArrayList<String> mExampleIdu;
    private ArrayList<String> mExamplePts;
//    private ArrayList<ListLeaderboards> mSpecLeaderboardsArrayList;
//    private ArrayList<ListLeaderboards> mLaboLeaderboardsArrayList;
//    private ArrayList<ListLeaderboards> mFastLeaderboardsArrayList;
//    private ArrayList<ListLeaderboards> mFullLeaderboardsArrayList;
    private String mGroupType;

    public static class LeaderboardViewHolder extends  RecyclerView.ViewHolder{

        public TextView mLeaderboardAgentName;
        public TextView mLeaderboardAgentPts;
        public BottomNavigationView bottomNavigationView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);


            mLeaderboardAgentName = itemView.findViewById(R.id.leaderboard_agent);
            mLeaderboardAgentPts = itemView.findViewById(R.id.leaderboard_agent_pts);
            bottomNavigationView = itemView.findViewById(R.id.top_navigation_rankings);
        }
    }

    public LeaderboardAdapter(ArrayList<String> exampleIdu, ArrayList<String> examplePts){
        this.mExampleIdu = exampleIdu;
        this.mExamplePts = examplePts;
//        mSpecLeaderboardsArrayList = mExampleList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_cardview, parent, false);
        LeaderboardViewHolder lvh = new LeaderboardViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        String currentIdu = mExampleIdu.get(position);
        String currentPts = mExamplePts.get(position);

            holder.mLeaderboardAgentName.setText("Agent " + currentIdu);
            holder.mLeaderboardAgentPts.setText(currentPts);

    }

    @Override
    public int getItemCount() {
        return mExampleIdu.size();
    }



}
