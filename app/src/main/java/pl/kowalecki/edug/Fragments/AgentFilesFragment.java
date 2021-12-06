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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
    TextView textView;
    TextView emptyFilesText;
    SessionManagement sessionManagement;
    ConstraintLayout constraintLayoutBackground;

    LifecycleOwner _lifecycleOwner;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.labo_mission_item_topbar:
                            initAdapter("Niezbędnik Agenta Laboratoryjnego", agentFilesViewModel.getLaboFiles());
                            break;
                        case R.id.spec_mission_item_topbar:
                            initAdapter("Niezbędnik Agenta Specjalnego", agentFilesViewModel.getSpecFiles());
                            break;
                        case R.id.other_mission_item_topbar:
                            initAdapter("Materiały dodatkowe", agentFilesViewModel.getAdditionalFiles());
                            break;

                    }
                    return true;
                }
            };

    private void initAdapter(String text, List<ListFile> files) {
        textView.setText(text);
                        if (files.size() != 0){
                            agentFilesAdapter = new AgentFilesAdapter();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(agentFilesAdapter);
                            agentFilesAdapter.setResults(files);
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyFilesText.setVisibility(View.GONE);
                            agentFilesAdapter.setOnItemClickListener(position3 -> {
                                Uri uri = Uri.parse(files.get(position3).getFileData().getLocation());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            });
                        }else{
                            recyclerView.setVisibility(View.GONE);
                            emptyFilesText.setVisibility(View.VISIBLE);
                        }
                    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agentFilesViewModel = new ViewModelProvider(this).get(AgentFilesViewModel.class);
        agentFilesViewModel.init();
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        searchAgentFiles(sGame);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_agent_files, container, false);

        _lifecycleOwner = getViewLifecycleOwner();
        constraintLayoutBackground = v.findViewById(R.id.files_text);
        emptyFilesText = v.findViewById(R.id.empty_files_text);
        textView = (TextView) v.findViewById(R.id.file_mission_text);

        recyclerView= v.findViewById(R.id.agent_files_recyclerView);
        recyclerView.setHasFixedSize(true);

        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_files);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.spec_mission_item);

        agentFilesViewModel.getFilesListLiveData().observe(_lifecycleOwner, new Observer<List<ListFile>>() {
                    @Override
                    public void onChanged(List<ListFile> listFiles) {
                        if (listFiles !=null){
                            initAdapter("Niezbędnik Agenta Laboratoryjnego", listFiles);
                            agentFilesViewModel.getFilesListLiveData().removeObservers(_lifecycleOwner);
                        }

                    }
                });

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
