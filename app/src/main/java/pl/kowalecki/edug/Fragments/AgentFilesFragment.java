package pl.kowalecki.edug.Fragments;

import android.content.Intent;
import android.net.Uri;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import pl.kowalecki.edug.Adapters.AgentFilesAdapter;
import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.ViewModel.AchievementsViewModel;
import pl.kowalecki.edug.ViewModel.AgentFilesViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentFilesFragment extends Fragment {

    private final String TAG = AgentFilesFragment.class.getSimpleName();
    private AgentFilesViewModel agentFilesViewModel;
    private AgentFilesAdapter agentFilesAdapter;
    private RecyclerView recyclerView;
    TextView textView, emptyFiles;
    SessionManagement sessionManagement;
    ConstraintLayout constraintLayoutBackground;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.labo_mission_item_topbar:
                            initAdapter("Niezbędnik Agenta Laboratoryjnego", agentFilesViewModel.getLaboFilesLiveData());
                            break;
                        case R.id.spec_mission_item_topbar:
                            initAdapter("Niezbędnik Agenta Specjalnego", agentFilesViewModel.getSpecFilesLiveData());
                            break;
                        case R.id.other_mission_item_topbar:
                            initAdapter("Materiały dodatkowe", agentFilesViewModel.getAdditionalFilesLiveData());
                            break;

                    }
                    return true;
                }
            };

    private void initAdapter(String text, LiveData liveData) {
        textView.setText(text);
            liveData.observe(getViewLifecycleOwner(), new Observer<List<ListFile>>() {
                @Override
                public void onChanged(List<ListFile> listFiles) {
                        if (listFiles != null){
                            agentFilesAdapter = new AgentFilesAdapter();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(agentFilesAdapter);
                            agentFilesAdapter.setResults(listFiles);
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyFiles.setVisibility(View.GONE);
                            agentFilesAdapter.setOnItemClickListener(position3 -> {
                                Uri uri = Uri.parse(listFiles.get(position3).getFileData().getLocation());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            });
                        }else{
                            recyclerView.setVisibility(View.GONE);
                            emptyFiles.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_agent_files, container, false);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_files);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        constraintLayoutBackground = v.findViewById(R.id.files_text);
        emptyFiles = v.findViewById(R.id.empty_files_text);
        sessionManagement = new SessionManagement(getContext());
        textView = (TextView) v.findViewById(R.id.file_mission_text);
        String sGame = sessionManagement.getGame();
        recyclerView= v.findViewById(R.id.agent_files_recyclerView);
        recyclerView.setHasFixedSize(true);
        agentFilesViewModel = ViewModelProviders.of(this).get(AgentFilesViewModel.class);
        agentFilesViewModel.init();
        initAdapter("Niezbędnik Agenta Laboratoryjnego", agentFilesViewModel.getLaboFilesLiveData());
        searchAgentFiles(sGame);
        checkMode();

        return v;
    }

    private void searchAgentFiles(String sGame) {
        agentFilesViewModel.getFiles(sGame);
    }

    private void checkMode() {
        recyclerView.findViewById(R.id.agent_files_recyclerView);
        if (sessionManagement.loadNightModeState()){
            constraintLayoutBackground.setBackground(null);
            recyclerView.setPadding(0,0,0,0);
        }
    }
}
