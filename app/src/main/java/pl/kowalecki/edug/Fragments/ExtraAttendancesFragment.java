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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

import pl.kowalecki.edug.Adapters.ExtraAttendancesAdapter;
import pl.kowalecki.edug.Model.Attendances.ListAttendances;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtraAttendancesFragment extends Fragment {

    SessionManagement sessionManagement;
    private final String TAG = ExtraAttendancesFragment.class.getSimpleName();
    UserService userService = ServiceGenerator.getRetrofit().create(UserService.class);

     ArrayList<String> attendancesLectureDate = new ArrayList<>();

     ArrayList<String> attendancesLaboDate = new ArrayList<>();

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView textView;
    private static final String ARG_NUMBER = "argNumber";
    private String agentIdu;

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
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
        }
        mRecycleView = (RecyclerView) v.findViewById(R.id.extra_attendances_list_recyclerview);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setAdapter(mAdapter);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.fragment_topbar_attendances);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        textView = (TextView) v.findViewById(R.id.extra_attendance_group_text) ;
        reveiveAttendances(sGame, agentIdu);
        return v;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.menu_spec_attendances:
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new ExtraAttendancesAdapter(attendancesLectureDate);
                            mRecycleView.setLayoutManager(mLayoutManager);
                            mRecycleView.setAdapter(mAdapter);
                            textView.setText("Misje Specjalne");
                            break;
                        case R.id.menu_labo_attendances:
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new ExtraAttendancesAdapter(attendancesLaboDate);
                            mRecycleView.setLayoutManager(mLayoutManager);
                            mRecycleView.setAdapter(mAdapter);
                            textView.setText("Misje Laboratoryjne ");
                            break;
                    }
                    return true;
                }
            };

    public void reveiveAttendances(String sGame, String agentIdu){
        Call<ListAttendances> call = userService.extraAttendances(sGame, agentIdu);
        Log.e(TAG,"Respo url: " +  call.request().url().toString());
        call.enqueue(new Callback<ListAttendances>() {
            @Override
            public void onResponse(Call<ListAttendances> call, Response<ListAttendances> response) {
                ListAttendances res = response.body();

                for (int i = 0; i < res.getExtraAttendances().size(); i++){
                    if (res.getExtraAttendances().get(i).getAttendance().getType().equals("W")){
                        attendancesLectureDate.add(res.getExtraAttendances().get(i).getAttendance().getData());
                    }
                    if (res.getExtraAttendances().get(i).getAttendance().getType().equals("L")){
                        attendancesLaboDate.add(res.getExtraAttendances().get(i).getAttendance().getData());
                    }
                }

                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new ExtraAttendancesAdapter(attendancesLaboDate);
                mRecycleView.setLayoutManager(mLayoutManager);
                mRecycleView.setAdapter(mAdapter);
                textView.setText("Misje Laboratoryjne");
            }




            @Override
            public void onFailure(Call<ListAttendances> call, Throwable t) {

            }
        });

    }


}
