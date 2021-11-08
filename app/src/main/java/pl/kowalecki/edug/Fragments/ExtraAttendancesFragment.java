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

import pl.kowalecki.edug.Adapters.ExtraAttendancesAdapter;
import pl.kowalecki.edug.Model.Attendances.ExtraAttendance;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.AttendancesViewModel;

public class ExtraAttendancesFragment extends Fragment {

    private final String TAG = ExtraAttendancesFragment.class.getSimpleName();
    private String agentIdu;
    private AttendancesViewModel attendancesViewModel;
    private ExtraAttendancesAdapter extraAttendancesAdapter;
    private RecyclerView recyclerView;
    TextView textView, emptyText;
    private static final String ARG_NUMBER = "argNumber";
    SessionManagement sessionManagement;
    ConstraintLayout constraintLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.menu_spec_attendances:
                            textView.setText("Misje Specjalne");
                            attendancesViewModel.getSpecAttendancesLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAttendance>>() {
                                @Override
                                public void onChanged(List<ExtraAttendance> specExtraAttendances) {
                                    if (specExtraAttendances != null){
                                        extraAttendancesAdapter.setResults(specExtraAttendances);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        emptyText.setVisibility(View.GONE);
                                    }else{
                                        recyclerView.setVisibility(View.GONE);
                                        emptyText.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            break;
                        case R.id.menu_labo_attendances:
                            textView.setText("Misje Laboratoryjne ");
                            attendancesViewModel.getLaboAttendancesLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAttendance>>() {
                                @Override
                                public void onChanged(List<ExtraAttendance> laboExtraAttendances) {
                                    if (laboExtraAttendances != null) {
                                        extraAttendancesAdapter.setResults(laboExtraAttendances);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        emptyText.setVisibility(View.GONE);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        emptyText.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }return true;
            }};

    public static ExtraAttendancesFragment newInstance(String number){
        ExtraAttendancesFragment fragment = new ExtraAttendancesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_extra_attendances, container, false);
        extraAttendancesAdapter = new ExtraAttendancesAdapter();
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.fragment_topbar_attendances);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        constraintLayout = v.findViewById(R.id.attendances_text);
        sessionManagement = new SessionManagement(getContext());
        emptyText = v.findViewById(R.id.empty_data);
        textView = (TextView) v.findViewById(R.id.extra_attendance_group_text);
        String sGame = sessionManagement.getGame();
        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
        }
        recyclerView = v.findViewById(R.id.extra_attendances_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(extraAttendancesAdapter);
        textView.setText("Misja Laboratoryjna");
        attendancesViewModel = ViewModelProviders.of(this).get(AttendancesViewModel.class);
        attendancesViewModel.init();
        attendancesViewModel.getLaboAttendancesLiveData().observe(getViewLifecycleOwner(), new Observer<List<ExtraAttendance>>() {
            @Override
            public void onChanged(List<ExtraAttendance> listAchievements) {
                if (listAchievements != null){
                    extraAttendancesAdapter.setResults(listAchievements);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
        });
        searchAchievs(sGame, agentIdu);
        checkMode();
        return v;
    }

    private void searchAchievs(String sGame, String agentIdu) {
        attendancesViewModel.getAttendances(sGame, agentIdu);
    }

    private void checkMode() {
        recyclerView.findViewById(R.id.extra_attendances_list_recyclerview);
        if (sessionManagement.loadNightModeState()){
            recyclerView.setPadding(0,0,0,0);
            constraintLayout.setBackground(null);

        }
    }

}
