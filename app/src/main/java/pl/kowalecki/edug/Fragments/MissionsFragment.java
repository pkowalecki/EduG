package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import pl.kowalecki.edug.Cipher.MD5Cipher;
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
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsFragment extends Fragment {
    private final String TAG = MissionsFragment.class.getSimpleName();
    Missions missions = new Missions();
    List<MissionFast> missionFastList = new ArrayList<>();
    List<MissionSpec> missionSpecList = new ArrayList<>();
    List<MissionLabo> missionLaboList = new ArrayList<>();

    SessionManagement sessionManagement;
    UserService userService = ServiceGenerator.getRetrofit().create(UserService.class);
    TextView textView, emptyMissionsText;
    List<ListMission> listMissions = new ArrayList<>();
    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date;
    private String sSys, sLang, sGame, sLogin, sHash, sCrcSpec, sCrcLabo, sCrcFast, mMenu;
    Bundle bundle = new Bundle();
    MissionFast missionFast = new MissionFast();
    MissionSpec missionSpec = new MissionSpec();
    MissionLabo missionLabo = new MissionLabo();
    private ArrayList<String> allActive = new ArrayList<>();
    private ArrayList<String> allActiveType = new ArrayList<>();
    private ArrayList<String> laboActive = new ArrayList<>();
    private ArrayList<String> laboActiveType = new ArrayList<>();
    private ArrayList<String> specActive = new ArrayList<>();
    private ArrayList<String> specActiveType = new ArrayList<>();
    private ArrayList<String> fastActive = new ArrayList<>();
    private ArrayList<String> fastActiveType = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MissionsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final UserLogin userLogin = new UserLogin();
    ConstraintLayout constraintLayout;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_missions, container, false);
        constraintLayout = v.findViewById(R.id.fragment_missions_constraint);
        emptyMissionsText = v.findViewById(R.id.empty_missions_text);
        sessionManagement = new SessionManagement(getContext());
        date = simpleDateFormat.format(currentDate);
        mRecyclerView = v.findViewById(R.id.missions_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.top_navigation_missions);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        textView = (TextView) v.findViewById(R.id.mission_name_text);
        sGame = sessionManagement.getGame();
        callListMission(sGame);
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        checkMode();
        return v;

    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()){
            mRecyclerView.setPadding(0, 0, 0, 0);
            constraintLayout.setBackground(null);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.item_missions_menu_allround:
                            textView.setText("Dostępne Misje");
                            Log.e("size", allActive.size()+ "" );
                            if (allActive.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyMissionsText.setVisibility(View.VISIBLE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                emptyMissionsText.setVisibility(View.GONE);
                                mAdapter = new MissionsAdapter(allActive, sessionManagement.loadNightModeState());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);

                                mAdapter.setOnItemClickListener(position -> {
                                    mMenu = "all";
                                    if (allActiveType.get(position).equals("spec")) {
                                        String mCrcSpec = userLogin.getPassword() + sSys + sLang + sGame + allActive.get(position) + sLogin + sHash;
                                        sCrcSpec = MD5Cipher.md5(mCrcSpec);
                                        callSpecMission(sSys, sLang, sGame, allActive.get(position), sLogin, sHash, sCrcSpec, position, mMenu);
                                    }
                                    if (allActiveType.get(position).equals("labo")) {
                                        String mCrc = userLogin.getPassword() + sSys + sLang + sGame + allActive.get(position) + sLogin + sHash;
                                        sCrcLabo = MD5Cipher.md5(mCrc);
                                        callLaboMission(sSys, sLang, sGame, allActive.get(position), sLogin, sHash, sCrcLabo, position, mMenu);
                                    }
                                    if (allActiveType.get(position).equals("fast")) {
                                        String mCrc = userLogin.getPassword() + sSys + sLang + sGame + allActive.get(position) + sLogin + sHash;
                                        sCrcFast = MD5Cipher.md5(mCrc);
                                        callFastMission(sSys, sLang, sGame, allActive.get(position), sLogin, sHash, sCrcFast, position, mMenu);
                                    }
                                });
                            }
                            break;

                        case R.id.item_missions_menu_special:
                            textView.setText("Misje Specjalne");
                            if (specActive.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyMissionsText.setVisibility(View.VISIBLE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                emptyMissionsText.setVisibility(View.GONE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new MissionsAdapter(specActive, sessionManagement.loadNightModeState());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener(position -> {
                                    mMenu = "spec";
                                    String mCrcSpec = userLogin.getPassword() + sSys + sLang + sGame + specActive.get(position) + sLogin + sHash;
                                    sCrcSpec = MD5Cipher.md5(mCrcSpec);
                                    callSpecMission(sSys, sLang, sGame, specActive.get(position), sLogin, sHash, sCrcSpec, position, mMenu);
                                });
                            }
                            break;

                        case R.id.item_missions_menu_instant:
                            textView.setText("Misje Błyskawiczne");
                            if (fastActive.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyMissionsText.setVisibility(View.VISIBLE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                emptyMissionsText.setVisibility(View.GONE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new MissionsAdapter(fastActive, sessionManagement.loadNightModeState());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);


                                mAdapter.setOnItemClickListener(position -> {
                                    mMenu = "fast";
                                    Log.e(TAG, "Position" + position);
                                    String mCrc = userLogin.getPassword() + sSys + sLang + sGame + fastActive.get(position) + sLogin + sHash;
                                    sCrcFast = MD5Cipher.md5(mCrc);
                                    callFastMission(sSys, sLang, sGame, fastActive.get(position), sLogin, sHash, sCrcFast, position, mMenu);
                                    Log.e("Aint bottom sheet ", fastActive.get(position) + " " + position);
                                });
                            }
                            break;

                        case R.id.item_missions_menu_labor:
                            textView.setText("Misje Laboratoryjne");
                            if (laboActive.size() == 0){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyMissionsText.setVisibility(View.VISIBLE);
                            }else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                emptyMissionsText.setVisibility(View.GONE);
                                mLayoutManager = new LinearLayoutManager(getContext());
                                mAdapter = new MissionsAdapter(laboActive, sessionManagement.loadNightModeState());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setAdapter(mAdapter);


                                mAdapter.setOnItemClickListener(position -> {
                                    mMenu = "labo";
                                    Log.e(TAG, "Position" + position);
                                    String mCrc = userLogin.getPassword() + sSys + sLang + sGame + laboActive.get(position) + sLogin + sHash;
                                    sCrcLabo = MD5Cipher.md5(mCrc);

                                    callLaboMission(sSys, sLang, sGame, laboActive.get(position), sLogin, sHash, sCrcLabo, position, mMenu);
                                });
                            }
                            break;





                    }
                    return true;
                }

            };


    public void callListMission(String sGame) {
        Call<Missions> call = userService.listMissions(sGame);
        call.enqueue(new Callback<Missions>() {
            @Override
            public void onResponse(Call<Missions> call, Response<Missions> response) {
                Missions res = response.body();
                for (int i = 0; i < res.getListMissions().size(); i++) {
                    try {
                        listMissions.add(response.body().getListMissions().get(i));
                        missions.setListMissions(listMissions);
                        Date dateTimeNow = simpleDateFormat.parse(date);
                        Date dateTimeStart = simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStart());
                        Date dateTimeEnd = simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStop());

                        if (dateTimeNow.after(dateTimeStart)) {
                            if (dateTimeNow.before(dateTimeEnd)) {
                                allActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                allActiveType.add(response.body().getListMissions().get(i).getMission().getType());
                            }}

                        if (response.body().getListMissions().get(i).getMission().getType().equals("labo")) {
                            if (dateTimeNow.after(dateTimeStart)) {
                                if (dateTimeNow.before(dateTimeEnd)) {
                                    laboActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    laboActiveType.add(response.body().getListMissions().get(i).getMission().getIdm());
                                }}}
                        if (response.body().getListMissions().get(i).getMission().getType().equals("spec")) {
                            if (dateTimeNow.after(dateTimeStart)) {
                                if (dateTimeNow.before(dateTimeEnd)) {
                                    specActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    specActiveType.add(response.body().getListMissions().get(i).getMission().getIdm());
                                }}}
                        if (response.body().getListMissions().get(i).getMission().getType().equals("fast")) {
                            if (dateTimeNow.after(dateTimeStart)) {
                                if (dateTimeNow.before(dateTimeEnd)) {
                                    fastActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    fastActiveType.add(response.body().getListMissions().get(i).getMission().getIdm());
                                }}}
                    }catch (ParseException e) {
                        Log.e(TAG, "catch error");
                        e.printStackTrace();
                    }

                }

                textView.setText("Dostępne Misje");
                if (allActive.size() == 0){
                    mRecyclerView.setVisibility(View.GONE);
                    emptyMissionsText.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLayoutManager = new LinearLayoutManager(getContext());
                    emptyMissionsText.setVisibility(View.GONE);
                    mAdapter = new MissionsAdapter(allActive, sessionManagement.loadNightModeState());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(position -> {
                        mMenu = "all";
                        if (allActiveType.get(position).equals("spec")) {
                            String mCrcSpec = userLogin.getPassword() + sSys + sLang + sGame + allActive.get(position) + sLogin + sHash;
                            sCrcSpec = MD5Cipher.md5(mCrcSpec);
                            callSpecMission(sSys, sLang, sGame, allActive.get(position), sLogin, sHash, sCrcSpec, position, mMenu);
                        }
                        if (allActiveType.get(position).equals("labo")) {
                            String mCrc = userLogin.getPassword() + sSys + sLang + sGame + allActive.get(position) + sLogin + sHash;
                            sCrcLabo = MD5Cipher.md5(mCrc);
                            callLaboMission(sSys, sLang, sGame, allActive.get(position), sLogin, sHash, sCrcLabo, position, mMenu);
                        }
                        if (allActiveType.get(position).equals("fast")) {
                            String mCrc = userLogin.getPassword() + sSys + sLang + sGame + allActive.get(position) + sLogin + sHash;
                            sCrcFast = MD5Cipher.md5(mCrc);
                            callFastMission(sSys, sLang, sGame, allActive.get(position), sLogin, sHash, sCrcFast, position, mMenu);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<Missions> call, Throwable t) {
            }
        });
    }

    private void callFastMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc, Integer position, String mMenu) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Call<MissionFast> call = userService.getFastMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
        Log.e("FastMissionFragment","Respo url: " +  call.request().url().toString());
        call.enqueue(new Callback<MissionFast>() {
            @Override
            public void onResponse(Call<MissionFast> call, Response<MissionFast> response) {
//                    Date dateTimeEnd = simpleDateFormat.parse(response.body().getMissionFast().getFinishTime());
//                    Date dateTimeNow = simpleDateFormat.parse(date);
//                    Date dateTimeStart = simpleDateFormat.parse(response.body().getMissionFast().getMissionStart());


                MissionFast res = response.body();
                if (response.body().getMissionFast().getResult()){
                    missionFast.setMissionFast(response.body().getMissionFast());
                    bundle.putString("arg_codename", missionFast.getMissionFast().getCodename());
                    bundle.putString("arg_picture", missionFast.getMissionFast().getPicture());
                    bundle.putString("arg_introTime", missionFast.getMissionFast().getIntroTime());
                    bundle.putString("arg_introText", missionFast.getMissionFast().getIntroText());
                    bundle.putString("arg_missionStart", missionFast.getMissionFast().getMissionStart());
                    bundle.putString("arg_missionText", missionFast.getMissionFast().getMissionText());
                    bundle.putString("arg_finishTime", missionFast.getMissionFast().getFinishTime());
                    bundle.putString("arg_finishText", missionFast.getMissionFast().getFinishText());
                    if (mMenu.equals("all")){
                        bundle.putString("arg_missionNumber", allActive.get(position));
                    }if (mMenu.equals("fast"))
                    {
                        bundle.putString("arg_missionNumber", fastActive.get(position));
                    }

                    Fragment fragment = new FastMissionFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.parent_fragment_container, fragment).commit();


                }
                if (!response.body().getMissionFast().getResult()){
                    builder.setTitle("Misja błyskawiczna")
                            .setMessage(response.body().getMissionFast().getComment())
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
                missionFastList.add(response.body());

            }

            @Override
            public void onFailure(Call<MissionFast> call, Throwable t) {
            Log.e("TAGFAIL", t.getMessage());
            Log.e("TAGFAIL", String.valueOf(t.getCause()));
            }
        });

    }

    private void callSpecMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc, Integer position, String mMenu) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Call<MissionSpec> call = userService.getSpecMissionData(sSys, sLang, sGame,mMission, sLogin, sHash, sCrc);
        Log.e(TAG, "SpecRespo: " + call.request().url().toString());
        call.enqueue(new Callback<MissionSpec>() {
            @Override
            public void onResponse(Call<MissionSpec> call, Response<MissionSpec> response) {
                MissionSpec res = response.body();

                if (response.body().getMissionSpecModel().getResult()){

                    ArrayList<Answers1> answers1s = new ArrayList<>();
                    ArrayList<Answers2> answers2s = new ArrayList<>();
                    ArrayList<Answers3> answers3s = new ArrayList<>();
                    ArrayList<Answers4> answers4s = new ArrayList<>();


                    missionSpec.setMissionSpecModel(response.body().getMissionSpecModel());
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
                    if (mMenu.equals("all")){
                        bundle.putString("arg_missionNumber", allActive.get(position));
                    }
                    if (mMenu.equals("spec")){
                        bundle.putString("arg_missionNumber", specActive.get(position));
                    }
                    Fragment fragment = new SpecialMissionFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.parent_fragment_container, fragment).commit();


                }
                if (!response.body().getMissionSpecModel().getResult()){
                    builder.setTitle("Misja Specjalna")
                            .setMessage(response.body().getMissionSpecModel().getComment())
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
                missionSpecList.add(res);

            }

            @Override
            public void onFailure(Call<MissionSpec> call, Throwable t) {

            }
        });
    }

    private void callLaboMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc, Integer position, String mMenu) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Call<MissionLabo> call = userService.getLaboMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
        Log.e("FastMissionFragment","Respo url: " +  call.request().url().toString());
        call.enqueue(new Callback<MissionLabo>() {
            @Override
            public void onResponse(Call<MissionLabo> call, Response<MissionLabo> response) {

                MissionLabo res = response.body();
                if (response.body().getMissionLaboModel().getResult()){
                    missionLabo.setMissionLaboModel(response.body().getMissionLaboModel());

                    bundle.putString("arg_codename", missionLabo.getMissionLaboModel().getCodename());
                    bundle.putString("arg_missionStart", missionLabo.getMissionLaboModel().getMissionStart());
                    bundle.putString("arg_missionText", missionLabo.getMissionLaboModel().getMissionText());
                    bundle.putString("arg_missionFile", missionLabo.getMissionLaboModel().getMissionFile());
                    bundle.putString("arg_finishTime", missionLabo.getMissionLaboModel().getFinishTime());
                    bundle.putString("arg_finishText", missionLabo.getMissionLaboModel().getFinishText());
                    if (mMenu.equals("all")){
                        bundle.putString("arg_missionNumber", allActive.get(position));
                    }if (mMenu.equals("fast"))
                    {
                        bundle.putString("arg_missionNumber", fastActive.get(position));
                    }

                    Fragment fragment = new LaboMissionFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.parent_fragment_container, fragment).commit();


                }
                if (!response.body().getMissionLaboModel().getResult()){
                    builder.setTitle("Misja Laboratoryjna")
                            .setMessage(response.body().getMissionLaboModel().getComment())
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
                missionLaboList.add(response.body());

            }

            @Override
            public void onFailure(Call<MissionLabo> call, Throwable t) {
                Log.e("TAGFAIL", t.getMessage());
                Log.e("TAGFAIL", String.valueOf(t.getCause()));
            }
        });

    }


}