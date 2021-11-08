package pl.kowalecki.edug.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Adapters.BottomSheetAdapter;
import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Fragments.AchievementsFragment;
import pl.kowalecki.edug.Fragments.AgentFilesFragment;
import pl.kowalecki.edug.Fragments.BadgesFragment;
import pl.kowalecki.edug.Fragments.ExtraAttendancesFragment;
import pl.kowalecki.edug.Fragments.FastMissionFragment;
import pl.kowalecki.edug.Fragments.LaboMissionFragment;
import pl.kowalecki.edug.Fragments.LeaderboardFragment;
import pl.kowalecki.edug.Fragments.MissionsFragment;
import pl.kowalecki.edug.Fragments.SpecialMissionFragment;
import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Model.MissionLabo.MissionLabo;
import pl.kowalecki.edug.Model.MissionSpec.Answers1;
import pl.kowalecki.edug.Model.MissionSpec.Answers2;
import pl.kowalecki.edug.Model.MissionSpec.Answers3;
import pl.kowalecki.edug.Model.MissionSpec.Answers4;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.Model.Missions.Missions;
import pl.kowalecki.edug.Model.User.UserAccount;
import pl.kowalecki.edug.Model.User.UserData;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.NotificationsApp.AlertReceiverAfter;
import pl.kowalecki.edug.NotificationsApp.AlertReceiverBefore;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.MissionFastViewModel;
import pl.kowalecki.edug.ViewModel.MissionLaboViewModel;
import pl.kowalecki.edug.ViewModel.MissionsSpecViewModel;
import pl.kowalecki.edug.ViewModel.MissionsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private final String TAG = HomeActivity.class.getSimpleName();
    private final UserLogin userLogin = new UserLogin();
    private final UserAccount userAccount = new UserAccount();
    private UserData userData = new UserData();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    private final Date currentDate = Calendar.getInstance().getTime();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Bundle bundle = new Bundle();
    private final TreeMap<Date, String> notificationMissionsStart = new TreeMap<>();
    private final TreeMap<Date, String> notificationMissionsFinish = new TreeMap<>();
    private SessionManagement sessionManagement;
    private String sSys, sLang, sGame, sLogin, sHash, sCrc;
    private AHBottomNavigation bottomNavigation;
    private TextView userName;
    private TextView userInfo;
    private TextView specText;
    private TextView laboText;
    private TextView instantText;
    private String date;
    private BottomSheetDialog bottomSheetDialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String sCrcSpec, sCrcFast, sCrcLabo;
    private NavigationView navigationView;
    private ExecutorService executorService;
    private MissionsViewModel missionsViewModel;
    private MissionFastViewModel missionFastViewModel;
    private MissionsSpecViewModel missionSpecViewModel;
    private MissionLaboViewModel missionLaboViewModel;
    private BottomSheetAdapter missionsAdapter;
    private RecyclerView recyclerView;
    MissionSpec missionSpec1 = new MissionSpec();
    MissionFast missionFast1 = new MissionFast();
    MissionLabo missionLabo1 = new MissionLabo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(this);
        if (sessionManagement.loadNightModeState()) {
            setTheme(R.style.DarkTheme_AppTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);

        missionsViewModel = ViewModelProviders.of(this).get(MissionsViewModel.class);
        missionsViewModel.init();
        missionsViewModel.getAllActiveMissionListLiveData().observe(this, new Observer<List<ListMission>>() {
            @Override
            public void onChanged(List<ListMission> listMissions) {
                laboText.setText(String.valueOf(missionsViewModel.getLaboMissionList().size()));
                instantText.setText(String.valueOf(missionsViewModel.getFastMissionList().size()));
                specText.setText(String.valueOf(missionsViewModel.getSpecMissionList().size()));
            }
        });

        missionSpecViewModel = ViewModelProviders.of(this).get(MissionsSpecViewModel.class);
        missionSpecViewModel.init();
        missionSpecViewModel.getMissionSpecLiveData().observe(this, new Observer<MissionSpec>() {
            @Override
            public void onChanged(MissionSpec missionSpec) {
                if (missionSpec !=null) {
                    missionSpec1.setMissionSpecModel(missionSpec.getMissionSpecModel());
                    if (missionSpec.getMissionSpecModel().getResult()){startSpecMission(missionSpec);}
                    else{
                        showDialogFinished("Misja Specjalna", missionSpec.getMissionSpecModel().getComment());
                    }

                }
            }
        });
        missionFastViewModel = ViewModelProviders.of(this).get(MissionFastViewModel.class);
        missionFastViewModel.init();
        missionFastViewModel.getMissionFastLiveData().observe(this, new Observer<MissionFast>() {
            @Override
            public void onChanged(MissionFast missionFastModel) {
                if (missionFastModel != null){
                    missionFast1.setMissionFast(missionFastModel.getMissionFast());
                    if (missionFastModel.getMissionFast().getResult()){startFastMission(missionFastModel);}
                    else{
                        showDialogFinished("Misja Błyskawiczna", missionFastModel.getMissionFast().getComment());
                    }
                }
            }
        });
        missionLaboViewModel = ViewModelProviders.of(this).get(MissionLaboViewModel.class);
        missionLaboViewModel.init();
        missionLaboViewModel.getLaboMissionLiveData().observe(this, new Observer<MissionLabo>() {
            @Override
            public void onChanged(MissionLabo missionLabo) {
                if (missionLabo != null){
                    missionLabo1.setMissionLaboModel(missionLabo.getMissionLaboModel());
                    if (missionLabo.getMissionLaboModel().getResult()){
                        startLaboMission(missionLabo);
                    }else{
                        showDialogFinished("Misja Laboratoryjna", missionLabo.getMissionLaboModel().getComment());
                    }
                }
            }
        });
        checkMissionsNotifications(sGame);
        setContentView(R.layout.activity_home);
        navigationView = findViewById(R.id.nav_view);
        sessionManagement = new SessionManagement(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        specText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.spec_mission_item)));
        laboText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.labor_mission_item)));
        instantText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.instant_mission_item)));
        TextView hazardText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.hazard_mission_item)));
        TextView lastText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.last_mission_item)));
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navbar_open_pl, R.string.navbar_close_pl);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        bottomNavigationMenu(5); // liczba elementów, które znajdują się w menu dolnym

        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        sCrc = sessionManagement.getCRC();
        callService(sSys, sLang, sGame, sLogin, sHash, sCrc);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.image_gravatar);
        String avatarLogin = MD5Cipher.md5(sLogin);
        Picasso.get().load("https://gravatar.com/avatar/" + avatarLogin + "?d=wavatar").into(imageView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();
            navigationView.setCheckedItem(R.id.main_menu);
        }
        date = simpleDateFormat.format(currentDate);
        searchAllMissions(sGame);
        initializeCountDrawer();

        //Usuwanie kanałów powiadomień
//        notificationManagerCompat.deleteNotificationChannel("channel200");
//        Log.e(TAG, "" + notificationManagerCompat.getNotificationChannels());

    }


    private void startSpecMission(MissionSpec missionSpecModelToFragment) {
        ArrayList<Answers1> answers1s = new ArrayList<>();
        ArrayList<Answers2> answers2s = new ArrayList<>();
        ArrayList<Answers3> answers3s = new ArrayList<>();
        ArrayList<Answers4> answers4s = new ArrayList<>();

        answers1s.add(missionSpecModelToFragment.getMissionSpecModel().getAnswers1());
        answers2s.add(missionSpecModelToFragment.getMissionSpecModel().getAnswers2());
        answers3s.add(missionSpecModelToFragment.getMissionSpecModel().getAnswers3());
        answers4s.add(missionSpecModelToFragment.getMissionSpecModel().getAnswers4());

        bundle.putString("arg_codename", missionSpecModelToFragment.getMissionSpecModel().getCodename());
        bundle.putString("arg_missionStart", missionSpecModelToFragment.getMissionSpecModel().getMissionStart());
        bundle.putString("arg_introText", missionSpecModelToFragment.getMissionSpecModel().getIntroText());
        bundle.putString("arg_question1", missionSpecModelToFragment.getMissionSpecModel().getQuestion1());
        bundle.putParcelableArrayList("arg_answers1", answers1s);
        bundle.putParcelableArrayList("arg_answers2", answers2s);
        bundle.putParcelableArrayList("arg_answers3", answers3s);
        bundle.putParcelableArrayList("arg_answers4", answers4s);
        bundle.putString("arg_question2", missionSpecModelToFragment.getMissionSpecModel().getQuestion2());
        bundle.putString("arg_question3", missionSpecModelToFragment.getMissionSpecModel().getQuestion3());
        bundle.putString("arg_question4", missionSpecModelToFragment.getMissionSpecModel().getQuestion4());
        bundle.putString("arg_finishTime", missionSpecModelToFragment.getMissionSpecModel().getFinishTime());
        bundle.putString("arg_finishText", missionSpecModelToFragment.getMissionSpecModel().getFinishText());
        Fragment fragment = new SpecialMissionFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_fragment_container, fragment).commit();
    }

    private void startFastMission(MissionFast missionFastModelToFragment){

        bundle.putString("arg_codename", missionFastModelToFragment.getMissionFast().getCodename());
        bundle.putString("arg_picture", missionFastModelToFragment.getMissionFast().getPicture());
        bundle.putString("arg_introTime", missionFastModelToFragment.getMissionFast().getIntroTime());
        bundle.putString("arg_introText", missionFastModelToFragment.getMissionFast().getIntroText());
        bundle.putString("arg_missionStart", missionFastModelToFragment.getMissionFast().getMissionStart());
        bundle.putString("arg_missionText", missionFastModelToFragment.getMissionFast().getMissionText());
        bundle.putString("arg_finishTime", missionFastModelToFragment.getMissionFast().getFinishTime());
        bundle.putString("arg_finishText", missionFastModelToFragment.getMissionFast().getFinishText());

        Fragment fragment = new FastMissionFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_fragment_container, fragment).commit();

    }

    private void startLaboMission(MissionLabo missionLaboModelToFragment){
        bundle.putString("arg_codename", missionLaboModelToFragment.getMissionLaboModel().getCodename());
        bundle.putString("arg_missionStart", missionLaboModelToFragment.getMissionLaboModel().getMissionStart());
        bundle.putString("arg_missionText", missionLaboModelToFragment.getMissionLaboModel().getMissionText());
        bundle.putString("arg_missionFile", missionLaboModelToFragment.getMissionLaboModel().getMissionFile());
        bundle.putString("arg_finishTime", missionLaboModelToFragment.getMissionLaboModel().getFinishTime());
        bundle.putString("arg_finishText", missionLaboModelToFragment.getMissionLaboModel().getFinishText());

        Fragment fragment = new LaboMissionFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_fragment_container, fragment).commit();

    }

    private void searchAllMissions(String sGame) {
        missionsViewModel.getAllMissions(sGame);
    }


    @Override
    protected void onResume() {
        checkNetworkState();
        checkMissionsNotifications(sGame);
        super.onResume();
    }

    private boolean checkNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check network state
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //Chcek status
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            //Init dialog about lack of internet
            new AlertDialog.Builder(this).setCancelable(true)
                    .setTitle("Brak internetu")
                    .setMessage("Brak dostępu do internetu")
                    .setPositiveButton("Spróbuj ponownie", (dialog, which) -> recreate())
                    .setCancelable(false)
                    .show();
            return false;
        }
        return true;
    }

    public void onClickImageButton(View v) {
        showBottomSheetDialog();
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialogHeader = new BottomSheetDialog(this);
        bottomSheetDialogHeader.setContentView(R.layout.bottom_sheet_header);
        LinearLayout logoutBottomSheet = bottomSheetDialogHeader.findViewById(R.id.logout_bottom_sheet);
        LinearLayout badgesStyle1 = bottomSheetDialogHeader.findViewById(R.id.badges_style_1);
        LinearLayout appTheme = bottomSheetDialogHeader.findViewById(R.id.app_theme);
        logoutBottomSheet.setOnClickListener(v -> {
            logout();
            bottomSheetDialogHeader.dismiss();
        });
        appTheme.setOnClickListener(v -> {
            if (sessionManagement.loadNightModeState()) {
                sessionManagement.setNightModeState(false);
            } else {
                sessionManagement.setNightModeState(true);
            }
            restartApp();
            mDrawerLayout.closeDrawer(GravityCompat.START);
            bottomSheetDialogHeader.dismiss();

        });

        bottomSheetDialogHeader.show();
    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        finish();
        startActivity(i);
    }

    private void startAlarmBefore(Date entry, String notifTitle, String notifContent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiverBefore.class);
        intent.putExtra("NOTIF_TITLE_BEFORE", notifTitle);
        intent.putExtra("NOTIF_CONTENT_BEFORE", notifContent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        Calendar cal = Calendar.getInstance();

        String dateToSplit = simpleDateFormat.format(entry);
        Log.e("StartAlarm", "" + dateToSplit);
        String[] splitedDateFull = dateToSplit.split(" ");
        String[] splitedDate = splitedDateFull[0].split("-");
        String[] splitedHours = splitedDateFull[1].split(":");

        int monthToCalendar = Integer.parseInt(splitedDate[1]) - 1;

        cal.set(Calendar.YEAR, Integer.parseInt(splitedDate[0]));
        cal.set(Calendar.MONTH, monthToCalendar);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitedDate[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitedHours[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(splitedHours[1]));
        cal.set(Calendar.SECOND, Integer.parseInt(splitedHours[2]));

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, _triggerReminder, pendingIntent);
    }

    private void startAlarmAfter(Date entry, String notifTitle, String notifContent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiverAfter.class);
        intent.putExtra("NOTIF_TITLE_AFTER", notifTitle);
        intent.putExtra("NOTIF_CONTENT_AFTER", notifContent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, intent, 0);

        Calendar cal = Calendar.getInstance();

        String dateToSplit = simpleDateFormat.format(entry);

        String[] splitedDateFull = dateToSplit.split(" ");
        String[] splitedDate = splitedDateFull[0].split("-");
        String[] splitedHours = splitedDateFull[1].split(":");

        int monthToCalendar = Integer.parseInt(splitedDate[1]) - 1;
        int timeBeforeEnd = Integer.parseInt(splitedHours[1]) - 15;
        cal.set(Calendar.YEAR, Integer.parseInt(splitedDate[0]));
        cal.set(Calendar.MONTH, monthToCalendar);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitedDate[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitedHours[0]));
        cal.set(Calendar.MINUTE, timeBeforeEnd);
        cal.set(Calendar.SECOND, Integer.parseInt(splitedHours[2]));

        Log.e("EndAlarm", "" + cal.getTime());
        if (cal.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }

//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, _triggerReminder, pendingIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_missions_layout, null);
        switch (menuItem.getItemId()) {
            case R.id.main_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.agent_start_files_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new AgentFilesFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.spec_mission_item:
                missionsAdapter = new BottomSheetAdapter();
                recyclerView = view.findViewById(R.id.missions_bottom_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(missionsAdapter);
                missionsAdapter.setResults(missionsViewModel.getSpecMissionList(), "Misja Specjalna");
                if (missionSpec1 != null) {
                    missionsAdapter.setOnItemClickListener(position -> {
                        String mCrcSpec = userLogin.getPassword() + sSys + sLang + sGame + missionsViewModel.getSpecMissionList().get(position).getMission().getIdm() + sLogin + sHash;
                        sCrcSpec = MD5Cipher.md5(mCrcSpec);
                        bottomSheetDialog.dismiss();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        missionSpecViewModel.getSpecMission(sSys, sLang, sGame, missionsViewModel.getSpecMissionList().get(position).getMission().getIdm(), sLogin, sHash, sCrcSpec);
                    });
                } else {
                    emptyMissionDialog();
                }
        bottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
                break;

            case R.id.labor_mission_item:
                missionsAdapter = new BottomSheetAdapter();
                recyclerView = view.findViewById(R.id.missions_bottom_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(missionsAdapter);
                missionsAdapter.setResults(missionsViewModel.getLaboMissionList(), "Misja Laboratoryjna");
                if(missionLabo1 != null){
                    missionsAdapter.setOnItemClickListener(position -> {
                        String mCrc = userLogin.getPassword() + sSys + sLang + sGame + missionsViewModel.getLaboMissionList().get(position).getMission().getIdm() + sLogin + sHash;
                        sCrcLabo = MD5Cipher.md5(mCrc);
                        bottomSheetDialog.dismiss();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        missionLaboViewModel.getLaboMission(sSys, sLang, sGame, missionsViewModel.getLaboMissionList().get(position).getMission().getIdm(), sLogin, sHash, sCrcLabo);
                    });
                }else{
                    emptyMissionDialog();
                }
                    bottomSheetDialog = new BottomSheetDialog(this);
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();
                break;

            case R.id.instant_mission_item:
                Log.e(TAG, "fast klik");
                missionsAdapter = new BottomSheetAdapter();
                recyclerView = view.findViewById(R.id.missions_bottom_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(missionsAdapter);
                missionsAdapter.setResults(missionsViewModel.getFastMissionList(), "Misja Błyskawiczna");
                if (missionFast1 != null){
                    missionsAdapter.setOnItemClickListener(position -> {
                        String mCrcFast = userLogin.getPassword() + sSys + sLang + sGame + missionsViewModel.getFastMissionList().get(position).getMission().getIdm() + sLogin + sHash;
                        sCrcFast = MD5Cipher.md5(mCrcFast);
                        bottomSheetDialog.dismiss();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        missionFastViewModel.getFastMission(sSys, sLang, sGame, missionsViewModel.getFastMissionList().get(position).getMission().getIdm(), sLogin, sHash, sCrcFast);
                    });
                }else {
                    emptyMissionDialog();
                }
                    bottomSheetDialog = new BottomSheetDialog(this);
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();
                break;

            case R.id.badges_item:
                BadgesFragment badgesFragment = BadgesFragment.newInstance(userData.getUserAccount().getAgentNumber(), userData.getUserAccount().getCountBadgesStyle());
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, badgesFragment).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.achievs_item:
                AchievementsFragment achievementsFragment = AchievementsFragment.newInstance(userData.getUserAccount().getAgentNumber());
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, achievementsFragment).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.rankings_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new LeaderboardFragment()).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.extra_attendances_item:
                ExtraAttendancesFragment extraAttendancesFragment = ExtraAttendancesFragment.newInstance(userData.getUserAccount().getAgentNumber());
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, extraAttendancesFragment).commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.hazard_mission_item:
                Uri uri = Uri.parse("http://edug.pl");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.last_mission_item:
                Uri uri1 = Uri.parse("http://edug.pl");
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                startActivity(intent1);
                break;
        }
//        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeCountDrawer() {
        specText.setGravity(Gravity.CENTER_VERTICAL);
        specText.setTypeface(null, Typeface.BOLD);
        specText.setTextColor(getResources().getColor(R.color.edug_red));

        laboText.setGravity(Gravity.CENTER_VERTICAL);
        laboText.setTypeface(null, Typeface.BOLD);
        laboText.setTextColor(getResources().getColor(R.color.edug_red));

        instantText.setGravity(Gravity.CENTER_VERTICAL);
        instantText.setTypeface(null, Typeface.BOLD);
        instantText.setTextColor(getResources().getColor(R.color.edug_red));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void bottomNavigationMenu(Integer elements) {

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        AHBottomNavigationItem missions = new AHBottomNavigationItem(R.string.misje, R.drawable.missions, R.color.edug_bootomNavbar_grey);
        AHBottomNavigationItem avatars = new AHBottomNavigationItem(R.string.avatary, R.drawable.avatars, R.color.edug_bootomNavbar_grey);
        AHBottomNavigationItem bitcoins = new AHBottomNavigationItem(R.string.bitcoin_y, R.drawable.bitcoin, R.color.edug_bootomNavbar_grey);
        AHBottomNavigationItem exacoins = new AHBottomNavigationItem(R.string.exacoin_y, R.drawable.exacoins, R.color.edug_bootomNavbar_grey);
        AHBottomNavigationItem points = new AHBottomNavigationItem(R.string.punkty, R.drawable.points, R.color.edug_bootomNavbar_grey);

        bottomNavigation.addItem(missions);
        bottomNavigation.addItem(avatars);
        bottomNavigation.addItem(bitcoins);
        bottomNavigation.addItem(exacoins);
        bottomNavigation.addItem(points);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor(parseImageColor(R.attr.edug_points_bar_bottombar)));
        bottomNavigation.setBehaviorTranslationEnabled(false);

        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation.setNotificationTextColor(Color.parseColor("#299fc8"));//Ustawia kolor tekstu w ramce
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#CC0000")); //kolor ramki

        for (int i = 0; i < elements; i++) {
            bottomNavigation.disableItemAtPosition(i);
        }

        bottomNavigation.setItemDisableColor(Color.parseColor(parseImageColor(R.attr.bottombar_images)));
    }

    private String parseImageColor(int edug_points_bar_bottombar) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(edug_points_bar_bottombar, typedValue, true);
        int backgroundColor = ContextCompat.getColor(this, typedValue.resourceId);
        return String.format("#%06X", (0xFFFFFF & backgroundColor));
    }

    public void logout() {
        sessionManagement.setLoginToEdug(false);
        sessionManagement.setLogin("");
        sessionManagement.setSys("");
        sessionManagement.setLang("");
        sessionManagement.setGame("");
        sessionManagement.setHash("");
        sessionManagement.setCRC("");
        sessionManagement.setNightModeState(false);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void checkMissionsNotifications(String sGame) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Call<Missions> call = apiRequest.listMissions(sGame);
            call.enqueue(new Callback<Missions>() {
                @Override
                public void onResponse(Call<Missions> call, Response<Missions> response) {
                    Missions res = response.body();
                    try {
                        Date dateTimeNow = simpleDateFormat.parse(date);
                        for (int i = 0; i < res.getListMissions().size(); i++) {

                            if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body().getListMissions().get(i).getMission().getStart()).after(dateTimeNow)) {
                                notificationMissionsStart.put(simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStart()), response.body().getListMissions().get(i).getMission().getIdm());
                            }
                            if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body().getListMissions().get(i).getMission().getStop()).after(dateTimeNow)) {
                                notificationMissionsFinish.put(simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStop()), response.body().getListMissions().get(i).getMission().getIdm());
                            }
                        }

                    } catch (ParseException ignored) {
                    }

                    //TODO:Fix treeMap
                    //Stworzenie osobnej mapy do posortowania i wyciągnięcia najwcześniejszej daty.
                    TreeMap<Date, String> m1 = new TreeMap(notificationMissionsStart);
                    if (!sortData(m1).isEmpty()) {
                        startAlarmBefore(sortData(m1).firstKey(), "Dostępna nowa misja", "Rozpocznij misję " + sortData(m1).firstEntry().getValue() + " Agencie");
                    }
                    TreeMap<Date, String> m2 = new TreeMap(notificationMissionsFinish);
                    if (!sortData(m2).isEmpty()) {
                        startAlarmAfter(sortData(m2).firstKey(), "Czas dobiega końca", "Do końca misji " + sortData(m2).firstEntry().getValue() + " pozozstało 15 minut, pospiesz się Agencie");
                    }

                }

                @Override
                public void onFailure(Call<Missions> call, Throwable t) {
                }
            });
        });

    }

    private TreeMap<Date, String> sortData(TreeMap<Date, String> m1) {
        Date now = new Date(System.currentTimeMillis());
        TreeMap<Date, String> tm = new TreeMap<>();
        for (TreeMap.Entry<Date, String> entry1 : m1.entrySet()) {//Tutaj przechowywana jest posortowana lista dat
            if (entry1.getKey().compareTo(now) >= 0) {
                tm.put(entry1.getKey(), entry1.getValue());
            }
        }
        return tm;
    }

    private void showDialogFinished(String title, String comment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(comment)
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void emptyMissionDialog() {
        new AlertDialog.Builder(this).setCancelable(true)
                .setTitle("Brak misji")
                .setMessage("Aktualnie nie masz żadnej misji do wykonania, Agencie")
                .setNegativeButton("OK", (dialog, which) -> dialog.cancel())
                .show();
    }

    public void callService(String sSys, String sLang, String sGame, String sLogin, String sHash, String sCrc) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()-> {
            apiRequest.userAccount(sSys, sLang, sGame, sLogin, sHash, sCrc).enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    navigationView = findViewById(R.id.nav_view);
                    userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
                    userInfo = navigationView.getHeaderView(0).findViewById(R.id.user_info);
                    userData = response.body();
                    Log.e(TAG, response.body() + " ");

                    userAccount.setCountMission(userData.getUserAccount().getCountMission());
                    userAccount.setCountAvatar(userData.getUserAccount().getCountAvatar());
                    userAccount.setCountBitcoin(userData.getUserAccount().getCountBitcoin());
                    userAccount.setCountExacoin(userData.getUserAccount().getCountExacoin());
                    userAccount.setCountPoint(userData.getUserAccount().getCountPoint());
                    userAccount.setCountBadgesStyle(userData.getUserAccount().getCountBadgesStyle());
                    userAccount.setResult(userData.getUserAccount().getResult());
                    userAccount.setAgentNumber(userData.getUserAccount().getAgentNumber());
                    userAccount.setAgentName(userData.getUserAccount().getAgentName());
                    userAccount.setAgentEmail(userData.getUserAccount().getAgentEmail());
                    userAccount.setGroupName(userData.getUserAccount().getGroupName());

                    userName.setText(userAccount.getAgentName());
                    userInfo.setText("AGENT " + " " + userData.getUserAccount().getAgentNumber() + " [" + userData.getUserAccount().getGroupName() + "] " + sGame);


                    if (userAccount.getCountMission() == 0)
                        bottomNavigation.setNotification("0", 0);
                    else
                        bottomNavigation.setNotification(String.valueOf(userAccount.getCountMission()), 0);

                    if (userAccount.getCountAvatar() == 0) bottomNavigation.setNotification("0", 1);
                    else
                        bottomNavigation.setNotification(String.valueOf(userAccount.getCountAvatar()), 1);

                    if (userAccount.getCountBitcoin() == 0)
                        bottomNavigation.setNotification("0", 2);
                    else
                        bottomNavigation.setNotification(String.valueOf(userAccount.getCountBitcoin()), 2);

                    if (userAccount.getCountExacoin() == 0)
                        bottomNavigation.setNotification("0", 3);
                    else
                        bottomNavigation.setNotification(String.valueOf(userAccount.getCountExacoin()), 3);

                    if (userAccount.getCountPoint() == 0) bottomNavigation.setNotification("0", 4);
                    else
                        bottomNavigation.setNotification(String.valueOf(userAccount.getCountPoint()), 4);

                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {

                }
            });

        });

    }

}
