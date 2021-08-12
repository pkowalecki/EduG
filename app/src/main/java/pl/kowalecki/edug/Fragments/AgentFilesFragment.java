package pl.kowalecki.edug.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import pl.kowalecki.edug.Adapters.AgentFilesAdapter;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentFilesFragment extends Fragment {


    SessionManagement sessionManagement;
    private final String TAG = AgentFilesFragment.class.getSimpleName();
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);

    final ArrayList<String> laboFileNameArray = new ArrayList<>();
    final ArrayList<String> laboUrlArray = new ArrayList<>();

    final ArrayList<String> specFileNameArray = new ArrayList<>();
    final ArrayList<String> specUrlArray = new ArrayList<>();

    final ArrayList<String> dodaFileNameArray = new ArrayList<>();
    final ArrayList<String> dodaUrlArray = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private AgentFilesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView textView;
    RelativeLayout filesText;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_agent_files, container, false);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        receiveListFiles(sGame);
        filesText = v.findViewById(R.id.files_text);
        mRecyclerView= v.findViewById(R.id.agent_files_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_files);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        textView = (TextView) v.findViewById(R.id.file_mission_text);
        checkMode();

        return v;
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()){
            filesText.setBackground(null);
            mRecyclerView.setPadding(0,0,0,0);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.labo_mission_item_topbar:
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new AgentFilesAdapter(laboFileNameArray, laboUrlArray);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            textView.setText("Niezbędnik Agenta Laboratoryjnego");

                            mAdapter.setOnItemClickListener(position -> {
                                Uri uri = Uri.parse(laboUrlArray.get(position));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            });
                            break;
                        case R.id.spec_mission_item_topbar:
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new AgentFilesAdapter(specFileNameArray, specUrlArray);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            textView.setText("Niezbędnik Agenta Specjalnego");

                            mAdapter.setOnItemClickListener(position -> {
                                Uri uri = Uri.parse(specUrlArray.get(position));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            });
                            break;
                        case R.id.other_mission_item_topbar:
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new AgentFilesAdapter(dodaFileNameArray, dodaUrlArray);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            textView.setText("Materiały dodatkowe");
                            mAdapter.setOnItemClickListener(position -> {
                                Uri uri = Uri.parse(dodaUrlArray.get(position));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            });
                            break;
                    }
                    return true;
                }
            };



    public void receiveListFiles(String sGame){
        Call <FilesList> call = service.filesDataArray(sGame);

        call.enqueue(new Callback<FilesList>() {
            @Override
            public void onResponse(Call<FilesList> call, final Response<FilesList> response) {

                FilesList res = response.body();


                for (int i = 0; i < res.getListFiles().toArray().length; i++) {


                    if (response.body().getListFiles().get(i).getFileData().getCategory().equals("labo")) {
                        laboFileNameArray.add(response.body().getListFiles().get(i).getFileData().getFilemane());
                        laboUrlArray.add(response.body().getListFiles().get(i).getFileData().getLocation());
                    }

                    if (response.body().getListFiles().get(i).getFileData().getCategory().equals("spec")) {
                        specFileNameArray.add(response.body().getListFiles().get(i).getFileData().getFilemane());
                        specUrlArray.add(response.body().getListFiles().get(i).getFileData().getLocation());
                    }

                    if (response.body().getListFiles().get(i).getFileData().getCategory().equals("doda")) {
                        dodaFileNameArray.add(response.body().getListFiles().get(i).getFileData().getFilemane());
                        dodaUrlArray.add(response.body().getListFiles().get(i).getFileData().getLocation());

                    }
                }

                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new AgentFilesAdapter(laboFileNameArray, laboUrlArray);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                textView.setText("Niezbędnik Agenta Laboratoryjnego");
                mAdapter.setOnItemClickListener(position -> {
                    Uri uri = Uri.parse(laboUrlArray.get(position));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });

            }

            @Override
            public void onFailure(Call<FilesList> call, Throwable t) {
                Log.e(TAG, "URL on failure: " + call.request().url().toString());
                Log.e(TAG, "on failure: " + t.getMessage());
            }

        });

    }


}
