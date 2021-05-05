package pl.kowalecki.edug.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Activities.HomeActivity;
import pl.kowalecki.edug.ApiUtils;
import pl.kowalecki.edug.HttpHandler;
import pl.kowalecki.edug.Model.Files.FileData;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Model.Games.ListGames;
import pl.kowalecki.edug.Model.User.UserAccount;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.ReceiveData;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartFilesFragment extends Fragment {

    TextView laboTextView, dodaTextView, specTextView;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String TAG = StartFilesFragment.class.getSimpleName();
    SessionManagement sessionManagement;
    ReceiveData receiveData = new ReceiveData();
    String sGame;
    FilesList filesList = new FilesList();
    ListFile listFile = new ListFile();
    FileData fileData = new FileData();
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
    BottomNavigationView bottomNavigationView;
    ScrollView nestedScrollView;
    ListView laboListView, specListView, dodaListView;
    final ArrayList<String> laboArray = new ArrayList<>();
    final ArrayList<String> laboUrl = new ArrayList<>();
    final ArrayList<String> specArray = new ArrayList<>();
    final ArrayList<String> specUrl = new ArrayList<>();
    final ArrayList<String> dodaArray = new ArrayList<>();
    final ArrayList<String> dodaUrl = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sessionManagement = new SessionManagement(getContext());

        View v =  inflater.inflate(R.layout.test_fragment_start_files, container, false);
        laboListView = v.findViewById(R.id.labo_list_view);
        specListView = v.findViewById(R.id.spec_list_view);
        dodaListView = v.findViewById(R.id.doda_list_view);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_files);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        sGame = sessionManagement.getGame();
        receiveListFromServer(sGame);

        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.labo_mission_item_topbar:
                            laboListView.setVisibility(View.VISIBLE);
                            specListView.setVisibility(View.GONE);
                            dodaListView.setVisibility(View.GONE);
                            break;
                        case R.id.spec_mission_item_topbar:
                            laboListView.setVisibility(View.GONE);
                            specListView.setVisibility(View.VISIBLE);
                            dodaListView.setVisibility(View.GONE);
                            break;
                        case R.id.other_mission_item_topbar:
                            laboListView.setVisibility(View.GONE);
                            specListView.setVisibility(View.GONE);
                            dodaListView.setVisibility(View.VISIBLE);
                            break;
                    }
                    return true;
                }
            };



    public void receiveListFromServer(String sGame){
        Call <FilesList> call = service.filesDataArray(sGame);

        call.enqueue(new Callback<FilesList>() {
            @Override
            public void onResponse(Call<FilesList> call, final Response<FilesList> response) {

                FilesList res;
                String category;
                String filename;
                String location;
                List<ListFile> aaaa = new ArrayList<>();
                res = response.body();


                for (int i = 0; i < res.getListFiles().toArray().length; i++) {

                    category = response.body().getListFiles().get(i).getFileData().getCategory();
                    filename = response.body().getListFiles().get(i).getFileData().getFilemane();
                    location = response.body().getListFiles().get(i).getFileData().getLocation();

                    fileData.setCategory(category);
                    fileData.setFilemane(filename);
                    fileData.setLocation(location);


                    listFile.setFileData(fileData);
//                    Log.e(TAG, "NEXTONE" + listFile.getFileData().getFilemane());
                    aaaa.add(listFile);

                    if (response.body().getListFiles().get(i).getFileData().getCategory().equals("labo")){
                        laboArray.add(response.body().getListFiles().get(i).getFileData().getFilemane());
                        laboUrl.add(response.body().getListFiles().get(i).getFileData().getLocation());
                    }

                    if (response.body().getListFiles().get(i).getFileData().getCategory().equals("spec")){
                        specArray.add(response.body().getListFiles().get(i).getFileData().getFilemane());
                        specUrl.add(response.body().getListFiles().get(i).getFileData().getLocation());
                    }

                    if (response.body().getListFiles().get(i).getFileData().getCategory().equals("doda")){
                        dodaArray.add(response.body().getListFiles().get(i).getFileData().getFilemane());
                        dodaUrl.add(response.body().getListFiles().get(i).getFileData().getLocation());

                    }

                    filesList.setListFiles(aaaa);
                }Log.e(TAG, "LaboArray: " + laboArray);
                Log.e(TAG, "SpecArray: " + specArray);
                Log.e(TAG, "DodaArray: " + dodaArray);
                Log.e(TAG, "FULL RESPONSE"+ res);

                final ArrayAdapter laboAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, laboArray);
                final ArrayAdapter specAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, specArray);
                final ArrayAdapter dodaAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, dodaArray);
                laboListView.setAdapter(laboAdapter);
                specListView.setAdapter(specAdapter);
                dodaListView.setAdapter(dodaAdapter);

                laboListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Uri uri = Uri.parse(laboUrl.get(position));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                specListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Uri uri = Uri.parse(specUrl.get(position));
                        Log.e(TAG, "position spec:" + position);
                        Log.e(TAG, "id  spec:" + id);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                dodaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Uri uri = Uri.parse(dodaUrl.get(position));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
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
