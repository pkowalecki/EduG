package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import pl.kowalecki.edug.Adapters.LeaderboardAdapter;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {
    SessionManagement sessionManagement;
    private final String TAG = LeaderboardFragment.class.getSimpleName();
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);

    ArrayList<String> specLeaderboardsArrayListIdu = new ArrayList<>();
    ArrayList<String> specLeaderboardsArrayListPts = new ArrayList<>();

    ArrayList<String> laboLeaderboardsArrayListIdu = new ArrayList<>();
    ArrayList<String> laboLeaderboardsArrayListPts = new ArrayList<>();

    ArrayList<String> fastLeaderboardsArrayListIdu = new ArrayList<>();
    ArrayList<String> fastLeaderboardsArrayListPts = new ArrayList<>();

    ArrayList<String> fullLeaderboardsArrayListIdu = new ArrayList<>();
    ArrayList<String> fullLeaderboardsArrayListPts = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView textView;
    ConstraintLayout constraintLayoutBackground;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_rankings, container, false);
        sessionManagement = new SessionManagement(getContext());
        constraintLayoutBackground = v.findViewById(R.id.fragment_ranking_constraint);
        String sGame = sessionManagement.getGame();
        receiveLeaderboards(sGame);
        mRecyclerView = v.findViewById(R.id.rankings_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_rankings);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        textView = (TextView) v.findViewById(R.id.mission_name);
        checkMode();
        return v;
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()){
            mRecyclerView.setPadding(0,0,0,0);
            constraintLayoutBackground.setBackground(null);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.item_ranking_menu_special:
                            textView.setText("Misja Specjalna");
                            if (specLeaderboardsArrayListIdu.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                            }else{
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new LeaderboardAdapter(specLeaderboardsArrayListIdu, specLeaderboardsArrayListPts);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                            }

                            break;
                        case R.id.item_ranking_menu_labor:
                            textView.setText("Misja Laboratoryjna");
                            if (laboLeaderboardsArrayListIdu.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new LeaderboardAdapter(laboLeaderboardsArrayListIdu, laboLeaderboardsArrayListPts);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            break;
                        case R.id.item_ranking_menu_instant:
                            textView.setText("Misja Błyskawiczna");
                            if (fastLeaderboardsArrayListIdu.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                            }else{
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new LeaderboardAdapter(fastLeaderboardsArrayListIdu, fastLeaderboardsArrayListPts);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                            }


                            break;
                        case R.id.item_ranking_menu_allround:
                            textView.setText("Wszechagent");
                            if (fullLeaderboardsArrayListIdu.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new LeaderboardAdapter(fullLeaderboardsArrayListIdu, fullLeaderboardsArrayListPts);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            break;
                    }
                    return true;
                }

            };

    public void receiveLeaderboards(String sGame){
        Call<ListLeaderboards> call = service.extraLeaderboards(sGame);
        call.enqueue(new Callback<ListLeaderboards>() {
            @Override
            public void onResponse(Call<ListLeaderboards> call, Response<ListLeaderboards> response) {
                ListLeaderboards res = response.body();

                for (int i = 0; i < res.getExtraLeaderboards().size(); i++){

                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("spec")){
                        specLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
                        specLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());
                    }
                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("labo")){
                        laboLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
                        laboLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());;
                    }
                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("fast")){
                        fastLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
                        fastLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());;
                    }

                    if (res.getExtraLeaderboards().get(i).getPosition().getType().equals("full")){
                        fullLeaderboardsArrayListIdu.add(res.getExtraLeaderboards().get(i).getPosition().getIdu());
                        fullLeaderboardsArrayListPts.add(res.getExtraLeaderboards().get(i).getPosition().getPoints());;
                    }

                }

                textView.setText("Misja Specjalna");
                if (specLeaderboardsArrayListIdu.size() == 0){
                    mRecyclerView.setVisibility(View.GONE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new LeaderboardAdapter(specLeaderboardsArrayListIdu, specLeaderboardsArrayListPts);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<ListLeaderboards> call, Throwable t) {

            }
        });
    }

}
