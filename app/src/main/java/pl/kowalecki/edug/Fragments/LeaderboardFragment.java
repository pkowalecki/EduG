package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Adapters.LeaderboardAdapter;
import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.ViewModel.LeaderboardsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {
    SessionManagement sessionManagement;
    private final String TAG = LeaderboardFragment.class.getSimpleName();
    private LeaderboardsViewModel leaderboardsViewModel;
    private LeaderboardAdapter leaderboardAdapter;
    private RecyclerView recyclerView;
    TextView textView, emptyText;
    ConstraintLayout constraintLayoutBackground;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.item_ranking_menu_special:
                            initRanking("Misja Specjalna", leaderboardsViewModel.getSpecLeaderboardsLiveData());
                            break;
                        case R.id.item_ranking_menu_labor:
                            initRanking("Misja Laboratoryjna", leaderboardsViewModel.getLaboLeaderboardsLiveData());
                            break;
                        case R.id.item_ranking_menu_instant:
                            initRanking("Misja Błyskawiczna", leaderboardsViewModel.getFastLeaderboardsLiveData());
                            break;
                        case R.id.item_ranking_menu_allround:
                            initRanking("Wszechagent", leaderboardsViewModel.getAllLeaderboardsLiveData());
                            break;
                    }
                    return true;
                }

            };

    private void initRanking(String text, LiveData<List<ExtraLeaderboard>> listLeaderboardsLiveData) {
        textView.setText(text);
        listLeaderboardsLiveData.observe(getViewLifecycleOwner(), new Observer<List<ExtraLeaderboard>>() {
            @Override
            public void onChanged(List<ExtraLeaderboard> listLeaderboards) {
                if (listLeaderboards != null){
                    leaderboardAdapter = new LeaderboardAdapter();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(leaderboardAdapter);
                    leaderboardAdapter.setResults(listLeaderboards);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_rankings, container, false);
        sessionManagement = new SessionManagement(getContext());
        emptyText = v.findViewById(R.id.empty_list);
        constraintLayoutBackground = v.findViewById(R.id.fragment_ranking_constraint);
        String sGame = sessionManagement.getGame();
        recyclerView = v.findViewById(R.id.rankings_recycler_view);
        recyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_rankings);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        textView = (TextView) v.findViewById(R.id.mission_name);
        leaderboardsViewModel = ViewModelProviders.of(this).get(LeaderboardsViewModel.class);
        leaderboardsViewModel.init();
        initRanking("Misja Specjalna",leaderboardsViewModel.getSpecLeaderboardsLiveData());
        searchAllRankings(sGame);
        checkMode();
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



//    public void receiveLeaderboards(String sGame){
//        Call<ListLeaderboards> call = service.extraLeaderboards(sGame);
//        call.enqueue(new Callback<ListLeaderboards>() {
//            @Override
//            public void onResponse(Call<ListLeaderboards> call, Response<ListLeaderboards> response) {
//                ListLeaderboards res = response.body();
//
//                for (int i = 0; i < res.getExtraLeaderboards().size(); i++){
//
//                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("spec")){
//                        specLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
//                        specLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());
//                    }
//                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("labo")){
//                        laboLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
//                        laboLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());;
//                    }
//                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("fast")){
//                        fastLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
//                        fastLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());;
//                    }
//
//                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("full")){
//                        fullLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
//                        fullLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());;
//                    }
//
//                }
//
//                textView.setText("Misja Specjalna");
//                if (specLeaderboardsArrayListIdu.size() == 0){
//                    mRecyclerView.setVisibility(View.GONE);
//                    emptyText.setVisibility(View.VISIBLE);
//                }else{
//                    emptyText.setVisibility(View.GONE);
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    mLayoutManager = new LinearLayoutManager(getContext());
//                    mAdapter = new LeaderboardAdapter(specLeaderboardsArrayListIdu, specLeaderboardsArrayListPts);
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ListLeaderboards> call, Throwable t) {
//
//            }
//        });
//    }

}
