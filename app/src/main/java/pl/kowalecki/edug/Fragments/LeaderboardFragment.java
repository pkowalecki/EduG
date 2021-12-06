package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import pl.kowalecki.edug.Adapters.LeaderboardAdapter;
import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.LeaderboardsViewModel;

public class LeaderboardFragment extends Fragment{

    private final String TAG = LeaderboardFragment.class.getSimpleName();
    private SessionManagement sessionManagement;
    private LeaderboardsViewModel leaderboardsViewModel;
    private LeaderboardAdapter leaderboardAdapter;

    private RecyclerView recyclerView;
    private TextView textView;
    private TextView emptyText;
    private ConstraintLayout constraintLayoutBackground;

    LifecycleOwner _lifeCycleOwner;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.item_ranking_menu_special:
                            initRanking("Agent Specjalny", leaderboardsViewModel.getSpecLeaderboards());
                            break;
                        case R.id.item_ranking_menu_labor:
                            initRanking("Agent Laboratoryjny", leaderboardsViewModel.getLaboLeaderboards());
                            break;
                        case R.id.item_ranking_menu_instant:
                            initRanking("Agent Błyskawiczny", leaderboardsViewModel.getFastLeaderboards());
                            break;
                        case R.id.item_ranking_menu_allround:
                            initRanking("Agent Wszechstronny", leaderboardsViewModel.getTopLeaderboards());
                            break;
                    }
                    return true;
                }
            };


    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        leaderboardsViewModel = new ViewModelProvider(this).get(LeaderboardsViewModel.class);
        searchAllRankings(sGame);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_rankings, container, false);

        _lifeCycleOwner = getViewLifecycleOwner();
        emptyText = v.findViewById(R.id.empty_list);
        textView = v.findViewById(R.id.mission_name);
        constraintLayoutBackground = v.findViewById(R.id.fragment_ranking_constraint);

        recyclerView = v.findViewById(R.id.rankings_recycler_view);
        recyclerView.setHasFixedSize(true);

        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_rankings);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.item_ranking_menu_special);

        checkMode();

        leaderboardsViewModel.getSpecLeaderboardsMutable().observe(_lifeCycleOwner, new Observer<List<ExtraLeaderboard>>() {
            @Override
            public void onChanged(List<ExtraLeaderboard> leaderboards) {
                if (leaderboards != null){
                    initRanking("Agent Specjalny", leaderboards);
                }
                leaderboardsViewModel.getSpecLeaderboardsMutable().removeObservers(_lifeCycleOwner);
            }
        });

        return v;
    }


    private void searchAllRankings(String sGame) {
        leaderboardsViewModel.getAllLeaderboards(sGame);
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()){
            recyclerView.setPadding(0,0,0,0);
            constraintLayoutBackground.setBackground(null);
        }
    }

    private void initRanking(String text, List<ExtraLeaderboard> leaderboards) {
        textView.setText(text);
        if (leaderboards.size() != 0){
            leaderboardAdapter = new LeaderboardAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(leaderboardAdapter);
            leaderboardAdapter.setResults(leaderboards);
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }
    }

