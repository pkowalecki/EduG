package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import pl.kowalecki.edug.Adapters.AchievementsAdapter;
import pl.kowalecki.edug.Model.Achievements.ListAchievements;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementsFragment extends Fragment {

    SessionManagement sessionManagement;
    private final String TAG = AchievementsFragment.class.getSimpleName();
    UserService userService = ServiceGenerator.getRetrofit().create(UserService.class);

    ArrayList<String> specAchievementsIdmArray = new ArrayList<>();
    ArrayList<String> specAchievementsCodenameArray = new ArrayList<>();
    ArrayList<String> specAchievementsPointsArray = new ArrayList<>();

    ArrayList<String> laboAchievementsIdmArray = new ArrayList<>();
    ArrayList<String> laboAchievementsCodenameArray = new ArrayList<>();
    ArrayList<String> laboAchievementsPointsArray = new ArrayList<>();

    ArrayList<String> fastAchievementsIdmArray = new ArrayList<>();
    ArrayList<String> fastAchievementsCodenameArray = new ArrayList<>();
    ArrayList<String> fastAchievementsPointsArray = new ArrayList<>();

    ArrayList<String> lastAchievementsIdmArray = new ArrayList<>();
    ArrayList<String> lastAchievementsCodenameArray = new ArrayList<>();
    ArrayList<String> lastAchievementsPointsArray = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout mEmptyLayout;
    TextView textView;
    private static final String ARG_NUMBER = "argNumber";
    private String agentIdu;



    public static AchievementsFragment newInstance(String number){
        AchievementsFragment fragment = new AchievementsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_achievements, container, false);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        if (getArguments() != null){
            agentIdu = getArguments().getString(ARG_NUMBER);
        }
        mRecyclerView = v.findViewById(R.id.achievements_recyclerview);
        mEmptyLayout = v.findViewById(R.id.empty_achievements_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.fragment_topbar_achievements);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        reciveAchievements(sGame, agentIdu);
        textView = (TextView) v.findViewById(R.id.achievement_group_text);
        return v;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.item_achievement_menu_special:
                            if (specAchievementsIdmArray.size() == 0){
                                mEmptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else{ mRecyclerView.setVisibility(View.VISIBLE);
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new AchievementsAdapter(specAchievementsIdmArray, specAchievementsCodenameArray, specAchievementsPointsArray);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            textView.setText("Misje Specjalne");
                            }
                            break;
                        case R.id.item_achievement_menu_labor:
                            if (laboAchievementsIdmArray.size() == 0){
                                mEmptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(laboAchievementsIdmArray, laboAchievementsCodenameArray, laboAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                textView.setText("Misje Laboratoryjne");
                            }
                            break;
                        case R.id.item_achievement_menu_instant:
                            if (fastAchievementsIdmArray.size() == 0){
                                mEmptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(fastAchievementsIdmArray, fastAchievementsCodenameArray, fastAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                textView.setText("Misje Błyskawiczne");
                            }
                            break;
                        case R.id.item_achievement_menu_last:
                            if (lastAchievementsIdmArray.size() == 0){
                                mEmptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(lastAchievementsIdmArray, lastAchievementsCodenameArray, lastAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                textView.setText("Misja Ostateczna");
                            }
                            break;
                    }
                    return true;
                }
            };


    private void reciveAchievements(String sGame, String agentIdu) {
        Call<ListAchievements> call = userService.extraAchievements(sGame, agentIdu);
        Log.e(TAG,"Respo url: " +  call.request().url().toString());
        call.enqueue(new Callback<ListAchievements>() {
            @Override
            public void onResponse(Call<ListAchievements> call, Response<ListAchievements> response) {
                ListAchievements res = response.body();

                for (int i = 0; i < res.getExtraAchievements().size(); i++){
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("spec")){
                        specAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        specAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        specAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("labo")){
                        laboAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        laboAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        laboAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("fast")){
                        fastAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        fastAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        fastAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("last")){
                        lastAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        lastAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        lastAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                }

                if (specAchievementsIdmArray.size() == 0){
                    mEmptyLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }else{
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new AchievementsAdapter(specAchievementsIdmArray, specAchievementsCodenameArray, specAchievementsPointsArray);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    textView.setText("Misje Specjalne");
                }

                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new AchievementsAdapter(specAchievementsIdmArray, specAchievementsCodenameArray, specAchievementsPointsArray);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                textView.setText("Misje Specjalne");
            }

            @Override
            public void onFailure(Call<ListAchievements> call, Throwable t) {

            }
        });
    }
}
