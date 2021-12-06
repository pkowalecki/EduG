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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

    private TextView textView;
    private TextView emptyAchievsText;

    private static final String ARG_NUMBER = "argNumber";
    SessionManagement sessionManagement;
    ConstraintLayout constraintLayout;
    LifecycleOwner _lifecycleOwner;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.item_achievement_menu_special:
                    initMenuView("Misja Specjalna", achievementsViewModel.getSpecAchievementsList());
                    break;

                case R.id.item_achievement_menu_labor:
                    initMenuView("Misja Laboratoryjna", achievementsViewModel.getLaboAchievementsList());
                    break;

                case R.id.item_achievement_menu_instant:
                    initMenuView("Misja Błyskawiczna", achievementsViewModel.getFastAchievementsList());
                    break;

                case R.id.item_achievement_menu_last:
                    initMenuView("Misja Ostateczna", achievementsViewModel.getLastAchievementsList());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        achievementsViewModel = new ViewModelProvider(this).get(AchievementsViewModel.class);
        sessionManagement = new SessionManagement(getContext());
        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
        }
        String sGame = sessionManagement.getGame();
        searchAchievs(sGame, agentIdu);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_achievements, container, false);

        _lifecycleOwner = getViewLifecycleOwner();
        constraintLayout = v.findViewById(R.id.fragment_achievs_constraint);
        emptyAchievsText = v.findViewById(R.id.empty_achievements_text);
        textView = (TextView) v.findViewById(R.id.achievement_group_text);

        recyclerView = v.findViewById(R.id.achievements_recyclerview);
        recyclerView.setHasFixedSize(true);

        BottomNavigationView bottomNavigationView = v.findViewById(R.id.fragment_topbar_achievements);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.item_achievement_menu_special);

        achievementsViewModel.getSpecAchievementListLiveData().observe(_lifecycleOwner, new Observer<List<ExtraAchievement>>() {
            @Override
            public void onChanged(List<ExtraAchievement> achievements) {
                if (achievements != null){
                    initMenuView("Misja Specjalna", achievements);
                }
                achievementsViewModel.getSpecAchievementListLiveData().removeObservers(_lifecycleOwner);
            }
        });

        checkMode();

        return v;
    }

    private void searchAchievs(String sGame, String agentIdu) {
        achievementsViewModel.getAchievements(sGame, agentIdu);
    }

    private void initMenuView(String achievementType, List<ExtraAchievement> achievements) {
        textView.setText(achievementType);
        if (achievements.size() != 0){
            achievementsAdapter = new AchievementsAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(achievementsAdapter);
            achievementsAdapter.setResults(achievements);
            recyclerView.setVisibility(View.VISIBLE);
            emptyAchievsText.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            emptyAchievsText.setVisibility(View.VISIBLE);
        }
    }


    private void checkMode() {
        recyclerView.findViewById(R.id.achievements_recyclerview);
        if (sessionManagement.loadNightModeState()) {
            constraintLayout.setBackground(null);
            recyclerView.setPadding(0, 0, 0, 0);
        }
    }

}
