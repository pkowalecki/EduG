package pl.kowalecki.edug.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.kowalecki.edug.Adapters.BottomSheetAdapter;
import pl.kowalecki.edug.Adapters.MissionsAdapter;
import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Model.MissionLabo.MissionLabo;
import pl.kowalecki.edug.Model.MissionSpec.Answers1;
import pl.kowalecki.edug.Model.MissionSpec.Answers2;
import pl.kowalecki.edug.Model.MissionSpec.Answers3;
import pl.kowalecki.edug.Model.MissionSpec.Answers4;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.Model.Missions.Missions;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.ViewModel.AgentFilesViewModel;
import pl.kowalecki.edug.ViewModel.MissionFastViewModel;
import pl.kowalecki.edug.ViewModel.MissionLaboViewModel;
import pl.kowalecki.edug.ViewModel.MissionsSpecViewModel;
import pl.kowalecki.edug.ViewModel.MissionsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsFragment extends Fragment {

    private final static String TAG = MissionsFragment.class.getSimpleName();
    private MissionsAdapter missionsAdapter;
    private RecyclerView recyclerView;
    private TextView emptyMissionsText, textView;
    SessionManagement sessionManagement;
    ConstraintLayout constraintLayout;
    private String sSys, sLang, sGame, sLogin, sHash, sCrcSpec, sCrcLabo, sCrcFast, mMenu;
    Bundle bundle = new Bundle();
    private final UserLogin userLogin = new UserLogin();
    private MissionsViewModel missionsViewModel;
    private MissionLaboViewModel missionLaboViewModel;
    private MissionsSpecViewModel missionSpecViewModel;
    private MissionFastViewModel missionFastViewModel;


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.item_missions_menu_allround:
                            initMissions("Dostępne Misje", missionsViewModel.getAllActiveMissionListLiveData());
                            break;

                        case R.id.item_missions_menu_special:
                            initMissions("Misje Specjalne", missionsViewModel.getSpecMissionListLiveData());
                            break;

                        case R.id.item_missions_menu_labor:
                            initMissions("Misje Laboratoryjne", missionsViewModel.getLaboMissionListLiveData());
                            break;

                        case R.id.item_missions_menu_instant:
                            initMissions("Misje Błyskawiczne", missionsViewModel.getFastMissionListLiveData());
                            break;

                    }
                    return true;
            }};


    private void initMissions(String text, LiveData<List<ListMission>> missionsList) {
        textView.setText(text);
        missionsList.observe(getViewLifecycleOwner(), new Observer<List<ListMission>>() {
            @Override
            public void onChanged(List<ListMission> allListMissions) {
                if (allListMissions != null){
                    missionsAdapter = new MissionsAdapter();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(missionsAdapter);
                    missionsAdapter.setResults(allListMissions, sessionManagement.loadNightModeState());
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyMissionsText.setVisibility(View.GONE);

                    missionsAdapter.setOnItemClickListener(position -> {
                        if (allListMissions.get(position).getMission().getType().equals("spec")){
                            Log.e(TAG, allListMissions.get(position).getMission().getIdm());
                            String mCrcSpec = userLogin.getPassword() + sSys + sLang + sGame + allListMissions.get(position).getMission().getIdm() + sLogin + sHash;
                            sCrcSpec = MD5Cipher.md5(mCrcSpec);
                            missionSpecViewModel.getSpecMission(sSys, sLang, sGame, allListMissions.get(position).getMission().getIdm(), sLogin, sHash, sCrcSpec);
                            missionSpecViewModel.getMissionSpecLiveData().observe(getViewLifecycleOwner(), new Observer<MissionSpec>() {
                                @Override
                                public void onChanged(MissionSpec missionSpec) {
                                        if (missionSpec != null){
                                            if (missionSpec.getMissionSpecModel().getResult()){
                                        ArrayList<Answers1> answers1s = new ArrayList<>();
                                        ArrayList<Answers2> answers2s = new ArrayList<>();
                                        ArrayList<Answers3> answers3s = new ArrayList<>();
                                        ArrayList<Answers4> answers4s = new ArrayList<>();

                                        answers1s.add(missionSpec.getMissionSpecModel().getAnswers1());
                                        answers2s.add(missionSpec.getMissionSpecModel().getAnswers2());
                                        answers3s.add(missionSpec.getMissionSpecModel().getAnswers3());
                                        answers4s.add(missionSpec.getMissionSpecModel().getAnswers4());

                                        bundle.putString("arg_codename", missionSpec.getMissionSpecModel().getCodename());
                                        bundle.putString("arg_missionStart", missionSpec.getMissionSpecModel().getMissionStart());
                                        bundle.putString("arg_introText", missionSpec.getMissionSpecModel().getIntroText());
                                        bundle.putString("arg_question1", missionSpec.getMissionSpecModel().getQuestion1());
                                        bundle.putParcelableArrayList("arg_answers1", answers1s);
                                        bundle.putParcelableArrayList("arg_answers2", answers2s);
                                        bundle.putParcelableArrayList("arg_answers3", answers3s);
                                        bundle.putParcelableArrayList("arg_answers4", answers4s);
                                        bundle.putString("arg_question2", missionSpec.getMissionSpecModel().getQuestion2());
                                        bundle.putString("arg_question3", missionSpec.getMissionSpecModel().getQuestion3());
                                        bundle.putString("arg_question4", missionSpec.getMissionSpecModel().getQuestion4());
                                        bundle.putString("arg_finishTime", missionSpec.getMissionSpecModel().getFinishTime());
                                        bundle.putString("arg_finishText", missionSpec.getMissionSpecModel().getFinishText());
                                        Fragment fragment = new SpecialMissionFragment();
                                        fragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.parent_fragment_container, fragment).commit();
                                            }else{
                                                showDialog("Misja Specjalna", missionSpec.getMissionSpecModel().getComment());
                                            }
                                    }else{
                                            Log.e(TAG, "Eror");
                                        }

                                }

                            });
                        }

                        if (allListMissions.get(position).getMission().getType().equals("labo")) {
                            Log.e(TAG, allListMissions.get(position).getMission().getIdm());
                            String mCrc = userLogin.getPassword() + sSys + sLang + sGame + allListMissions.get(position).getMission().getIdm() + sLogin + sHash;
                            sCrcLabo = MD5Cipher.md5(mCrc);

                            missionLaboViewModel.getLaboMission(sSys, sLang, sGame, allListMissions.get(position).getMission().getIdm(), sLogin, sHash, sCrcLabo);
                            missionLaboViewModel.getLaboMissionLiveData().observe(getViewLifecycleOwner(), new Observer<MissionLabo>() {
                                @Override
                                public void onChanged(MissionLabo missionLabo) {
                                    if (missionLabo != null){
                                        if (missionLabo.getMissionLaboModel().getResult()) {
                                            bundle.putString("arg_codename", missionLabo.getMissionLaboModel().getCodename());
                                            bundle.putString("arg_missionStart", missionLabo.getMissionLaboModel().getMissionStart());
                                            bundle.putString("arg_missionText", missionLabo.getMissionLaboModel().getMissionText());
                                            bundle.putString("arg_missionFile", missionLabo.getMissionLaboModel().getMissionFile());
                                            bundle.putString("arg_finishTime", missionLabo.getMissionLaboModel().getFinishTime());
                                            bundle.putString("arg_finishText", missionLabo.getMissionLaboModel().getFinishText());
                                            Fragment fragment = new LaboMissionFragment();
                                            fragment.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.parent_fragment_container, fragment).commit();
                                        }else{
                                            showDialog("Misja Laboratoryjna", missionLabo.getMissionLaboModel().getComment());
                                        }

                                    }
                                }
                            });
                        }
                        if (allListMissions.get(position).getMission().getType().equals("fast")) {
                            Log.e(TAG, allListMissions.get(position).getMission().getIdm());
                            String mCrc = userLogin.getPassword() + sSys + sLang + sGame + allListMissions.get(position).getMission().getIdm() + sLogin + sHash;
                            sCrcFast = MD5Cipher.md5(mCrc);
                            missionFastViewModel.getFastMission(sSys, sLang, sGame, allListMissions.get(position).getMission().getIdm(), sLogin, sHash, sCrcFast);
                            missionFastViewModel.getMissionFastLiveData().observe(getViewLifecycleOwner(), new Observer<MissionFast>() {
                                @Override
                                public void onChanged(MissionFast missionFast) {
                                    if (missionFast != null){
                                        if (missionFast.getMissionFast().getResult()){
                                            bundle.putString("arg_codename", missionFast.getMissionFast().getCodename());
                                            bundle.putString("arg_picture", missionFast.getMissionFast().getPicture());
                                            bundle.putString("arg_introTime", missionFast.getMissionFast().getIntroTime());
                                            bundle.putString("arg_introText", missionFast.getMissionFast().getIntroText());
                                            bundle.putString("arg_missionStart", missionFast.getMissionFast().getMissionStart());
                                            bundle.putString("arg_missionText", missionFast.getMissionFast().getMissionText());
                                            bundle.putString("arg_finishTime", missionFast.getMissionFast().getFinishTime());
                                            bundle.putString("arg_finishText", missionFast.getMissionFast().getFinishText());

                                            Fragment fragment = new FastMissionFragment();
                                            fragment.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.parent_fragment_container, fragment).commit();
                                        }else{
                                            showDialog("Misja Błyskawiczna", missionFast.getMissionFast().getComment());
                                        }

                                    }
                                }
                            });
                        }
                    });
                }else{
                    recyclerView.setVisibility(View.GONE);
                    emptyMissionsText.setVisibility(View.VISIBLE);
                }
            }
        });
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_missions, container, false);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_missions);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        constraintLayout = v.findViewById(R.id.fragment_missions_constraint);
        emptyMissionsText = v.findViewById(R.id.empty_missions_text);
        sessionManagement = new SessionManagement(getContext());
        textView = (TextView) v.findViewById(R.id.mission_name_text);
        sGame = sessionManagement.getGame();
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();

        recyclerView = v.findViewById(R.id.missions_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        textView.setText("Dostępne Misje");
        missionsViewModel = ViewModelProviders.of(this).get(MissionsViewModel.class);
        missionsViewModel.init();
        missionLaboViewModel = ViewModelProviders.of(this).get(MissionLaboViewModel.class);
        missionLaboViewModel.init();
        missionSpecViewModel = ViewModelProviders.of(this).get(MissionsSpecViewModel.class);
        missionSpecViewModel.init();
        missionFastViewModel = ViewModelProviders.of(this).get(MissionFastViewModel.class);
        missionFastViewModel.init();

        initMissions("Dostępne Misje", missionsViewModel.getAllActiveMissionListLiveData());
        searchAllMissions(sGame);
        checkMode();
        return v;

    }

    private void searchAllMissions(String sGame) {
        missionsViewModel.getAllMissions(sGame);
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()){
            recyclerView.setPadding(0, 0, 0, 0);
            constraintLayout.setBackground(null);
        }
    }

    private void showDialog(String title, String comment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(comment)
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                //TODO W JAKIŚ SPOSÓB TO ROZWIĄZAĆ
                                Fragment fragment = new MissionsFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, fragment)
                                        .commit();
                            }
                        });
                        builder.show();
    }
}