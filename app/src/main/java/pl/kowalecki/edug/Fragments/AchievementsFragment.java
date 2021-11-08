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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import pl.kowalecki.edug.Adapters.AchievementsAdapter;
import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.AchievementsViewModel;

public class AchievementsFragment extends Fragment {

    private final String TAG = AchievementsFragment.class.getSimpleName();
    private String agentIdu;
    private AchievementsViewModel achievementsViewModel;
    private AchievementsAdapter achievementsAdapter;
    private RecyclerView recyclerView;
    TextView textView, emptyAchievs;
    private static final String ARG_NUMBER = "argNumber";
    SessionManagement sessionManagement;
    ConstraintLayout constraintLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.item_achievement_menu_special:
                    textView.setText("Misja Specjalna");
                    achievementsViewModel.getSpecAchievementsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAchievement>>() {
                        @Override
                        public void onChanged(List<ExtraAchievement> specListAchievements) {
                            if (specListAchievements != null){
                                achievementsAdapter.setResults(specListAchievements);
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyAchievs.setVisibility(View.GONE);
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                emptyAchievs.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    break;

                case R.id.item_achievement_menu_labor:
                    textView.setText("Misja Laboratoryjna");
                        achievementsViewModel.getLaboAchievementsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAchievement>>() {
                            @Override
                            public void onChanged(List<ExtraAchievement> laboListAchievements) {
                                if (laboListAchievements != null){
                                    achievementsAdapter.setResults(laboListAchievements);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    emptyAchievs.setVisibility(View.GONE);
                                }else{
                                    recyclerView.setVisibility(View.GONE);
                                    emptyAchievs.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    break;


                case R.id.item_achievement_menu_instant:
                    textView.setText("Misja Błyskawiczna");
                    achievementsViewModel.getFastAchievementsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAchievement>>() {
                        @Override
                        public void onChanged(List<ExtraAchievement> fastListAchievements) {
                            if (fastListAchievements != null){
                                achievementsAdapter.setResults(fastListAchievements);
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyAchievs.setVisibility(View.GONE);
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                emptyAchievs.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    break;
                case R.id.item_achievement_menu_last:
                    textView.setText("Misja Ostateczna");
                    achievementsViewModel.getLastAchievementsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAchievement>>() {
                        @Override
                        public void onChanged(List<ExtraAchievement> lastListAchievements) {
                            if (lastListAchievements != null){
                                achievementsAdapter.setResults(lastListAchievements);
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyAchievs.setVisibility(View.GONE);
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                emptyAchievs.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    break;

            }return true;
        }};




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
        achievementsAdapter = new AchievementsAdapter();
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.fragment_topbar_achievements);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        constraintLayout = v.findViewById(R.id.fragment_achievs_constraint);
        emptyAchievs = v.findViewById(R.id.empty_achievements_text);
        textView = (TextView) v.findViewById(R.id.achievement_group_text);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
        }
        recyclerView = v.findViewById(R.id.achievements_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(achievementsAdapter);

        textView.setText("Misja Specjalna");
        achievementsViewModel = ViewModelProviders.of(this).get(AchievementsViewModel.class);
        achievementsViewModel.init();
        achievementsViewModel.getSpecAchievementsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAchievement>>() {
            @Override
            public void onChanged(List<ExtraAchievement> listAchievements) {
                if (listAchievements != null){
                    achievementsAdapter.setResults(listAchievements);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyAchievs.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyAchievs.setVisibility(View.VISIBLE);
                }
            }
        });
        searchAchievs(sGame, agentIdu);
        checkMode();
        return v;
    }

    private void searchAchievs(String sGame, String agentIdu) {
        achievementsViewModel.getAchievements(sGame, agentIdu);
    }


    private void checkMode() {
        recyclerView.findViewById(R.id.achievements_recyclerview);
        if (sessionManagement.loadNightModeState()) {
            constraintLayout.setBackground(null);
            recyclerView.setPadding(0, 0, 0, 0);
        }
    }

}
