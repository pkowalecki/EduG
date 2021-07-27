package pl.kowalecki.edug.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import pl.kowalecki.edug.Adapters.BottomSheetAdapter;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.NotificationsApp.AlertReceiver;
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
import pl.kowalecki.edug.Model.WebServiceData;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = HomeActivity.class.getSimpleName();
    private UserLogin userLogin = new UserLogin();
    private  Button logoutButton;
    private  SessionManagement sessionManagement;
    private  UserAccount userAccount = new UserAccount();
    private  UserData userData = new UserData();
    private  WebServiceData webServiceData;
    private String sSys, sLang, sGame, sLogin, sHash, sCrc;
    private  AHBottomNavigation bottomNavigation;
    private  TextView userName, userInfo, specText, laboText, instantText, hazardText, lastText;
    private UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
    private Missions missions = new Missions();
    private List<ListMission> listMissions = new ArrayList<>();
    private  Date currentDate = Calendar.getInstance().getTime();
    private  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  String date;
    private  List<MissionSpec> missionSpecList = new ArrayList<>();
    private  List<MissionLabo> missionLaboList = new ArrayList<>();
    private  List<MissionFast> missionFastList = new ArrayList<>();
    private NavigationView navigationView;
    private BottomSheetDialog bottomSheetDialog;
    private  MissionSpec missionSpec = new MissionSpec();
    private   MissionLabo missionLabo = new MissionLabo();
    private MissionFast missionFast = new MissionFast();
    private Bundle bundle = new Bundle();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> allActive = new ArrayList<>();
    private ArrayList<String> allActiveType = new ArrayList<>();
    private ArrayList<String> laboActive = new ArrayList<>();
    private ArrayList<String> laboActiveType = new ArrayList<>();
    private ArrayList<String> specActive = new ArrayList<>();
    private ArrayList<String> specActiveType = new ArrayList<>();
    private ArrayList<String> fastActive = new ArrayList<>();
    private ArrayList<String> fastActiveType = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BottomSheetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String sCrcSpec, sCrcLabo, sCrcFast, mMenu;
    private TreeMap<Date, String> notificationMissions = new TreeMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigationView = findViewById(R.id.nav_view);
        sessionManagement = new SessionManagement(getApplicationContext());
        logoutButton = findViewById(R.id.logoutButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        specText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.spec_mission_item)));
        laboText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.labor_mission_item)));
        instantText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.instant_mission_item)));
        hazardText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.hazard_mission_item)));
        lastText = (TextView) (MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.last_mission_item)));
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navbar_open_pl, R.string.navbar_close_pl);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        bottomNavigationMenu(5); // liczba elementów, które znajdują się w menu dolnym
        mDrawerToggle.syncState();
        webServiceData = new WebServiceData();
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        sCrc = sessionManagement.getCRC();
        navigationView.setNavigationItemSelectedListener(this);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.image_gravatar);
        String avatarLogin = MD5Cipher.md5(sLogin);
        Picasso.get().load("https://gravatar.com/avatar/" + avatarLogin + "?d=wavatar").into(imageView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();
            navigationView.setCheckedItem(R.id.main_menu);
        }
        date = simpleDateFormat.format(currentDate);
        try {
            callService();
            callListMission(sGame);
        } catch (ParseException e) {
            Toast.makeText(this, "Wystąpił problem z serwisem", Toast.LENGTH_SHORT).show();
        }
        initializeCountDrawer();


        //Usuwanie kanałów powiadomień
//        notificationManagerCompat.deleteNotificationChannel("channel200");
//        Log.e(TAG, "" + notificationManagerCompat.getNotificationChannels());

        //TODO: Ogarnąć pytanie o pływające powiadomienia w aplikacji
        //TODO: zrobić try/catch do metod "call..." <- tego chyba jednak nie trzeba, bo ma własne "On failure"
    }




    private void startAlarm(Date entry, String notifTitle, String notifContent){
        //TODO: Ogarnąć, żeby przy odminimalizownaiu apki metoda "callListMission" się wykonywała
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent  intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("NOTIF_TITLE", notifTitle);
        intent.putExtra("NOTIF_CONTENT", notifContent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        long _currentTime = System.currentTimeMillis();
        long tenSeconds = 1000 * 5;
        long _triggerReminder = _currentTime + tenSeconds; //triggers a reminder after 5 seconds.
        Calendar cal = Calendar.getInstance();

        String dateToSplit = simpleDateFormat.format(entry);
        Log.e("StartAlarm", "" + dateToSplit);
        String[] splitedDateFull = dateToSplit.split(" ");
        String[] splitedDate = splitedDateFull[0].split("-");
        String[] splitedHours = splitedDateFull[1].split(":");

        Integer monthToCalendar = Integer.parseInt(splitedDate[1])- 1;

        cal.set(Calendar.YEAR, Integer.parseInt(splitedDate[0]));
        cal.set(Calendar.MONTH, monthToCalendar);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitedDate[2]));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitedHours[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(splitedHours[1]));
        cal.set(Calendar.SECOND, Integer.parseInt(splitedHours[2]));

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, _triggerReminder, pendingIntent);
    }

        //TODO: Dodać do wysuwanej listy pasek na górze, który pokazuje, że można zwinąć
        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem menuItem){
        //TODO: Zrobić przycisk cofania do bottom sheet i zrobić, żeby to cofanie działało xd
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

                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mAdapter = new BottomSheetAdapter(specActive, "Misja Specjalna");
                    mRecyclerView = view.findViewById(R.id.missions_bottom_recyclerView);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(position -> {

                        String mCrcSpec = userLogin.getPassword() + sSys + sLang + sGame + specActive.get(position) + sLogin + sHash;
                        sCrcSpec = MD5Cipher.md5(mCrcSpec);
                        mMenu = "spec";
                        callSpecMission(sSys, sLang, sGame, specActive.get(position), sLogin, sHash, sCrcSpec, position, mMenu);
                        bottomSheetDialog.dismiss();
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                    });
                    bottomSheetDialog = new BottomSheetDialog(this);
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();
                    break;
                case R.id.labor_mission_item:
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mAdapter = new BottomSheetAdapter(laboActive, "Misja Laboratoryjna");
                    mRecyclerView = view.findViewById(R.id.missions_bottom_recyclerView);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(position -> {

                        String mCrcLabo = userLogin.getPassword() + sSys + sLang + sGame + laboActive.get(position) + sLogin + sHash;
                        sCrcLabo = MD5Cipher.md5(mCrcLabo);
                        mMenu = "labo";
                        callLaboMission(sSys, sLang, sGame, laboActive.get(position), sLogin, sHash, sCrcLabo, position, mMenu);
                        bottomSheetDialog.dismiss();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    });
                    bottomSheetDialog = new BottomSheetDialog(this);
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();

                    break;
                case R.id.instant_mission_item:
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mAdapter = new BottomSheetAdapter(fastActive, "Misja Błyskawiczna");
                    mRecyclerView = view.findViewById(R.id.missions_bottom_recyclerView);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(position -> {
                            String mCrcFast = userLogin.getPassword() + sSys + sLang + sGame + fastActive.get(position) + sLogin + sHash;
                            sCrcFast = MD5Cipher.md5(mCrcFast);
                            mMenu = "labo";
                            callFastMission(sSys, sLang, sGame, fastActive.get(position), sLogin, sHash, sCrcFast, position, mMenu);
                        bottomSheetDialog.dismiss();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        });
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



            }
//        mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }


        private void initializeCountDrawer () {
            specText.setGravity(Gravity.CENTER_VERTICAL);
            specText.setTypeface(null, Typeface.BOLD);
            specText.setTextColor(getResources().getColor(R.color.colorAccent));

            laboText.setGravity(Gravity.CENTER_VERTICAL);
            laboText.setTypeface(null, Typeface.BOLD);
            laboText.setTextColor(getResources().getColor(R.color.colorAccent));

            instantText.setGravity(Gravity.CENTER_VERTICAL);
            instantText.setTypeface(null, Typeface.BOLD);
            instantText.setTextColor(getResources().getColor(R.color.colorAccent));

        }


        @Override
        protected void onPostCreate (@Nullable Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
            mDrawerToggle.syncState();
        }


        @Override
        public void onConfigurationChanged (@NonNull Configuration newConfig){
            super.onConfigurationChanged(newConfig);
            mDrawerToggle.onConfigurationChanged(newConfig);
        }


        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }


        @Override
        public void onBackPressed () {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);

            } else {
                super.onBackPressed();
            }
        }


        private void bottomNavigationMenu (Integer elements){
            bottomNavigation = findViewById(R.id.bottomNavigationView);
            AHBottomNavigationItem missions = new AHBottomNavigationItem(R.string.misje, R.drawable.missions, R.color.colorPrimary);
            AHBottomNavigationItem avatars = new AHBottomNavigationItem(R.string.avatary, R.drawable.avatars, R.color.colorPrimary);
            AHBottomNavigationItem bitcoins = new AHBottomNavigationItem(R.string.bitcoin_y, R.drawable.bitcoin, R.color.colorPrimary);
            AHBottomNavigationItem exacoins = new AHBottomNavigationItem(R.string.exacoin_y, R.drawable.exacoins, R.color.colorPrimary);
            AHBottomNavigationItem points = new AHBottomNavigationItem(R.string.punkty, R.drawable.points, R.color.colorPrimary);

            bottomNavigation.addItem(missions);
            bottomNavigation.addItem(avatars);
            bottomNavigation.addItem(bitcoins);
            bottomNavigation.addItem(exacoins);
            bottomNavigation.addItem(points);

            bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#F1EDED"));
            bottomNavigation.setBehaviorTranslationEnabled(false);
            bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
            bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
            bottomNavigation.setForceTint(true);
            bottomNavigation.setTranslucentNavigationEnabled(false);
            bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
            bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#CC0000"));

            for (int i = 0; i < elements; i++) {
                bottomNavigation.disableItemAtPosition(i);
            }
            bottomNavigation.setItemDisableColor(Color.parseColor("#777777"));
        }


        public void callService () {
            Call<UserData> call = service.userAccount(sSys, sLang, sGame, sLogin, sHash, sCrc);

            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    userName = findViewById(R.id.user_name);
                    userInfo = findViewById(R.id.user_info);
                    Log.e(TAG, call.request().url().toString());
                    userData = response.body();


                    Log.e(TAG, "On rest" + userData.getUserAccount().getCountMission());

                    Log.e(TAG, "przed " + userAccount.getCountMission());
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
                    Log.e(TAG, "SDFJSDF " + userAccount.getAgentName());
                    Log.e(TAG, "po " + userAccount.getCountExacoin());

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

        }


        public void logout (MenuItem item){
            sessionManagement.setLoginToEdug(false);
            sessionManagement.setLogin("");
            sessionManagement.setSys("");
            sessionManagement.setLang("");
            sessionManagement.setGame("");
            sessionManagement.setHash("");
            sessionManagement.setCRC("");
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        public void callListMission (String sGame) throws ParseException{
        //TODO: Ogarnąć parsowanie errorów
            Date dateTimeNow = simpleDateFormat.parse(date);
            Call<Missions> call = service.listMissions(sGame);
            call.enqueue(new Callback<Missions>() {
                @Override
                public void onResponse(Call<Missions> call, Response<Missions> response) {
                    Missions res = response.body();
                    for (int i = 0; i < res.getListMissions().size(); i++) {
                        try {
                            listMissions.add(response.body().getListMissions().get(i));
                            missions.setListMissions(listMissions);

                            Date dateTimeStart = simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStart());
                            Date dateTimeEnd = simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStop());

                            if (dateTimeNow.after(dateTimeStart)) {
                                if (dateTimeNow.before(dateTimeEnd)) {
                                    allActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    allActive.add(response.body().getListMissions().get(i).getMission().getStart());
                                    allActiveType.add(response.body().getListMissions().get(i).getMission().getType());
                                }
                            }

                            if (response.body().getListMissions().get(i).getMission().getType().equals("labo")) {
                                if (dateTimeNow.after(dateTimeStart)) {
                                    if (dateTimeNow.before(dateTimeEnd)) {
                                        laboActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                        laboActiveType.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    }
                                }
                            }
                            if (response.body().getListMissions().get(i).getMission().getType().equals("spec")) {
                                if (dateTimeNow.after(dateTimeStart)) {
                                    if (dateTimeNow.before(dateTimeEnd)) {
                                        specActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                        specActiveType.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    }
                                }
                            }
                            if (response.body().getListMissions().get(i).getMission().getType().equals("fast")) {
                                if (dateTimeNow.after(dateTimeStart)) {
                                    if (dateTimeNow.before(dateTimeEnd)) {
                                        fastActive.add(response.body().getListMissions().get(i).getMission().getIdm());
                                        fastActiveType.add(response.body().getListMissions().get(i).getMission().getIdm());
                                    }
                                }
                            }
                            if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body().getListMissions().get(i).getMission().getStart()).after(dateTimeNow)){
                                notificationMissions.put(simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStart()), response.body().getListMissions().get(i).getMission().getIdm());

                            }
                           } catch (ParseException e) {
                            Log.e(TAG, "catch error");
                        }

                    }



                    try {
                        notificationMissions.put(simpleDateFormat.parse("2021-07-27 11:10:00"), "101");
                        notificationMissions.put(simpleDateFormat.parse("2021-07-25 19:48:00"), "Valeu");
                        notificationMissions.put(simpleDateFormat.parse("2021-07-24 12:09:00"), "Valeu");
                        notificationMissions.put(simpleDateFormat.parse("2021-07-23 13:00:00"), "Valeu");

                    } catch (ParseException e) {
                        Toast.makeText(HomeActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                    }

                    //Stworzenie osobnej mapy do posortowania i wyciągnięcia najwcześniejszej daty.
                    TreeMap<Date, String> m1 = new TreeMap(notificationMissions);
                    //TODO:Hardcoded stringa wrzucić do <strings>
                    if (!sortData(m1).isEmpty()){
                        startAlarm(sortData(m1).firstKey(), "Dostępna nowa misja", "Rozpocznij nową misję, Agencie");
                    }

                    laboText.setText(String.valueOf(laboActive.size()));
                    instantText.setText(String.valueOf(fastActive.size()));
                    specText.setText(String.valueOf(specActive.size()));
                }

                @Override
                public void onFailure(Call<Missions> call, Throwable t) {
                    //TODO: Tutaj muszę coś wyświetlić
                }
            });
        }

    private TreeMap<Date, String> sortData(TreeMap<Date, String> m1) {
        Date now = new Date(System.currentTimeMillis());
        TreeMap<Date, String> tm = new TreeMap<>();
        for (TreeMap.Entry<Date, String> entry1 : m1.entrySet()){//Tutaj przechowywana jest posortowana lista dat
            if (entry1.getKey().compareTo(now) >= 0){
                tm.put(entry1.getKey(), entry1.getValue());
            }
        }
        return tm;
    }



    private void callSpecMission (String sSys, String sLang, String sGame, String
        mMission, String sLogin, String sHash, String sCrc, Integer position, String mMenu){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            Call<MissionSpec> call = service.getSpecMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
            Log.e(TAG, "SpecRespo: " + call.request().url().toString());
            call.enqueue(new Callback<MissionSpec>() {
                @Override
                public void onResponse(Call<MissionSpec> call, Response<MissionSpec> response) {
                    MissionSpec res = response.body();

                    if (response.body().getMissionSpecModel().getResult()) {

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
                        if (mMenu.equals("all")) {
                            bundle.putString("arg_missionNumber", allActive.get(position));
                        }
                        if (mMenu.equals("spec")) {
                            bundle.putString("arg_missionNumber", specActive.get(position));
                        }
                        Fragment fragment = new SpecialMissionFragment();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.parent_fragment_container, fragment).commit();


                    }
                    if (!response.body().getMissionSpecModel().getResult()) {
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

        private void callLaboMission (String sSys, String sLang, String sGame, String
        mMission, String sLogin, String sHash, String sCrc, Integer position, String mMenu){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Call<MissionLabo> call = service.getLaboMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
            Log.e("FastMissionFragment", "Respo url: " + call.request().url().toString());
            call.enqueue(new Callback<MissionLabo>() {
                @Override
                public void onResponse(Call<MissionLabo> call, Response<MissionLabo> response) {

                    MissionLabo res = response.body();
                    if (response.body().getMissionLaboModel().getResult()) {
                        missionLabo.setMissionLaboModel(response.body().getMissionLaboModel());

                        bundle.putString("arg_codename", missionLabo.getMissionLaboModel().getCodename());
                        bundle.putString("arg_missionStart", missionLabo.getMissionLaboModel().getMissionStart());
                        bundle.putString("arg_missionText", missionLabo.getMissionLaboModel().getMissionText());
                        bundle.putString("arg_missionFile", missionLabo.getMissionLaboModel().getMissionFile());
                        bundle.putString("arg_finishTime", missionLabo.getMissionLaboModel().getFinishTime());
                        bundle.putString("arg_finishText", missionLabo.getMissionLaboModel().getFinishText());
                        if (mMenu.equals("all")) {
                            bundle.putString("arg_missionNumber", allActive.get(position));
                        }
                        if (mMenu.equals("fast")) {
                            bundle.putString("arg_missionNumber", fastActive.get(position));
                        }

                        Fragment fragment = new LaboMissionFragment();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.parent_fragment_container, fragment).commit();


                    }
                    if (!response.body().getMissionLaboModel().getResult()) {
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

        private void callFastMission (String sSys, String sLang, String sGame, String
        mMission, String sLogin, String sHash, String sCrc, Integer position, String mMenu){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Call<MissionFast> call = service.getFastMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
            Log.e("FastMissionFragment", "Respo url: " + call.request().url().toString());
            call.enqueue(new Callback<MissionFast>() {
                @Override
                public void onResponse(Call<MissionFast> call, Response<MissionFast> response) {

                    MissionFast res = response.body();
                    if (response.body().getMissionFast().getResult()) {
                        missionFast.setMissionFast(response.body().getMissionFast());
                        bundle.putString("arg_codename", missionFast.getMissionFast().getCodename());
                        bundle.putString("arg_picture", missionFast.getMissionFast().getPicture());
                        bundle.putString("arg_introTime", missionFast.getMissionFast().getIntroTime());
                        bundle.putString("arg_introText", missionFast.getMissionFast().getIntroText());
                        bundle.putString("arg_missionStart", missionFast.getMissionFast().getMissionStart());
                        bundle.putString("arg_missionText", missionFast.getMissionFast().getMissionText());
                        bundle.putString("arg_finishTime", missionFast.getMissionFast().getFinishTime());
                        bundle.putString("arg_finishText", missionFast.getMissionFast().getFinishText());
                        if (mMenu.equals("all")) {
                            bundle.putString("arg_missionNumber", allActive.get(position));
                        }
                        if (mMenu.equals("fast")) {
                            bundle.putString("arg_missionNumber", fastActive.get(position));
                        }

                        Fragment fragment = new FastMissionFragment();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.parent_fragment_container, fragment).commit();


                    }
                    if (!response.body().getMissionFast().getResult()) {
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

    }

