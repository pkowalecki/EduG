package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    private static final String ARG_NUMBER = "argNumber";
    private final String TAG = AchievementsFragment.class.getSimpleName();
    SessionManagement sessionManagement;
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
    TextView textView, emptyAchievs;
    ConstraintLayout constraintLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String agentIdu;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.item_achievement_menu_special:
                            textView.setText("Misje Specjalne");
                            if (specAchievementsIdmArray.size() == 0) {
                                mRecyclerView.setVisibility(View.GONE);
                                emptyAchievs.setVisibility(View.VISIBLE);

                            } else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                emptyAchievs.setVisibility(View.GONE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(specAchievementsIdmArray, specAchievementsCodenameArray, specAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                            break;
                        case R.id.item_achievement_menu_labor:
                            textView.setText("Misje Laboratoryjne");
                            if (laboAchievementsIdmArray.size() == 0) {
                                emptyAchievs.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                emptyAchievs.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(laboAchievementsIdmArray, laboAchievementsCodenameArray, laboAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                            break;
                        case R.id.item_achievement_menu_instant:
                            textView.setText("Misje B??yskawiczne");
                            if (fastAchievementsIdmArray.size() == 0) {
                                emptyAchievs.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                emptyAchievs.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(fastAchievementsIdmArray, fastAchievementsCodenameArray, fastAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                            break;
                        case R.id.item_achievement_menu_last:
                            textView.setText("Misja Ostateczna");
                            if (lastAchievementsIdmArray.size() == 0) {
                                emptyAchievs.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                emptyAchievs.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new AchievementsAdapter(lastAchievementsIdmArray, lastAchievementsCodenameArray, lastAchievementsPointsArray);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            break;
                    }
                    return true;
                }
            };

    public static AchievementsFragment newInstance(String number) {
        AchievementsFragment fragment = new AchievementsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_achievements, container, false);
        constraintLayout = v.findViewById(R.id.fragment_achievs_constraint);
        emptyAchievs = v.findViewById(R.id.empty_achievements_text);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
        }
        mRecyclerView = v.findViewById(R.id.achievements_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.fragment_topbar_achievements);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        reciveAchievements(sGame, agentIdu);
        textView = (TextView) v.findViewById(R.id.achievement_group_text);
        checkMode();
        return v;
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()) {
            constraintLayout.setBackground(null);
            mRecyclerView.setPadding(0, 0, 0, 0);
        }
    }

    private void reciveAchievements(String sGame, String agentIdu) {
        Call<ListAchievements> call = userService.extraAchievements(sGame, agentIdu);
        Log.e(TAG, "Respo url: " + call.request().url().toString());
        call.enqueue(new Callback<ListAchievements>() {
            @Override
            public void onResponse(Call<ListAchievements> call, Response<ListAchievements> response) {
                ListAchievements res = response.body();

                for (int i = 0; i < res.getExtraAchievements().size(); i++) {
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("spec")) {
                        specAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        specAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        specAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("labo")) {
                        laboAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        laboAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        laboAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("fast")) {
                        fastAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        fastAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        fastAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                    if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("last")) {
                        lastAchievementsIdmArray.add(response.body().getExtraAchievements().get(i).getAchievements().getIdm());
                        lastAchievementsCodenameArray.add(response.body().getExtraAchievements().get(i).getAchievements().getCodename());
                        lastAchievementsPointsArray.add(response.body().getExtraAchievements().get(i).getAchievements().getPoints());
                    }
                }

                textView.setText("Misje Specjalne");
                if (specAchievementsIdmArray.size() == 0) {
                    emptyAchievs.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyAchievs.setVisibility(View.GONE);
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new AchievementsAdapter(specAchievementsIdmArray, specAchievementsCodenameArray, specAchievementsPointsArray);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ListAchievements> call, Throwable t) {

            }
        });
    }
}
