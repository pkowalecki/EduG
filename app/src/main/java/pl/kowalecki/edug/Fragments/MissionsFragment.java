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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.kowalecki.edug.Adapters.MissionsAdapter;
import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.Model.Missions.Missions;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsFragment extends Fragment {
    private final String TAG = MissionsFragment.class.getSimpleName();
    Missions missions = new Missions();
    SessionManagement sessionManagement;
    UserService userService = ServiceGenerator.getRetrofit().create(UserService.class);
    TextView textView;
    List<ListMission> listMissions = new ArrayList<>();
    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.item_missions_menu_special:

                            try {
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new MissionsAdapter(listMissions, "spec", "Misja Specjalna", date);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                textView.setText("Misja Specjalna");
                                break;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        case R.id.item_missions_menu_instant:

                            try {
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new MissionsAdapter(listMissions, "fast", "Misja Błyskawiczna", date);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                textView.setText("Misja Błyskawiczna");
                                break;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        case R.id.item_missions_menu_labor:

                            try {
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new MissionsAdapter(listMissions, "labo", "Misja Laboratoryjna", date);
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                textView.setText("Misja Laboratoryjna");
                                break;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                    }
                    return true;
                }

            };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_missions, container, false);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        date = simpleDateFormat.format(currentDate);
        mRecyclerView = v.findViewById(R.id.missions_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_missions);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        textView = (TextView) v.findViewById(R.id.mission_name_text);
        callListMission(sGame);
        return v;
    }

    private void callListMission(String sGame) {

        Call<Missions> call = userService.listMissions(sGame);
        Log.e(TAG, "Mission url:" + call.request().url().toString());
        call.enqueue(new Callback<Missions>() {
            @Override
            public void onResponse(Call<Missions> call, Response<Missions> response) {
                Missions res = response.body();
                for (int i = 0; i < res.getListMissions().size(); i++) {

                    listMissions.add(response.body().getListMissions().get(i));
                    missions.setListMissions(listMissions);
                }

                try {
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new MissionsAdapter(listMissions, "spec", "Misja Specjalna", date);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    textView.setText("Misja Specjalna");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "" + missions.getListMissions().size());
            }

            @Override
            public void onFailure(Call<Missions> call, Throwable t) {
            }
        });
    }


}