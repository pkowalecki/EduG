package pl.kowalecki.edug.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.media.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import pl.kowalecki.edug.Adapters.MissionsAdapter;
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
import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.Model.Missions.Mission;
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
    Button logoutButton;
    SessionManagement sessionManagement;
    UserAccount userAccount = new UserAccount();
    UserData userData = new UserData();
    WebServiceData webServiceData;
    String sSys, sLang, sGame, sLogin, sHash, sCrc;
    AHBottomNavigation bottomNavigation;
    TextView userName, userInfo, specText, laboText, instantText, hazardText, lastText;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);

    Missions missions = new Missions();

    List<ListMission> listMissions = new ArrayList<>();
    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date;
    private ArrayList<String> allActive = new ArrayList<>();
    private ArrayList<String> allActiveType = new ArrayList<>();
    private ArrayList<String> laboActive = new ArrayList<>();
    private ArrayList<String> laboActiveType = new ArrayList<>();
    private ArrayList<String> specActive = new ArrayList<>();
    private ArrayList<String> specActiveType = new ArrayList<>();
    private ArrayList<String> fastActive = new ArrayList<>();
    private ArrayList<String> fastActiveType = new ArrayList<>();
    NavigationView navigationView;
    ListView listView;
    ArrayAdapter<String> adapter;
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
        Picasso.get().load("https://gravatar.com/avatar/"+ avatarLogin +"?d=wavatar").into(imageView);
        callService();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();
            navigationView.setCheckedItem(R.id.main_menu);
        }
        date = simpleDateFormat.format(currentDate);
        callListMission(sGame);
        initializeCountDrawer();

        try {
            notification();
        } catch (ParseException e) {
            Log.e("Notif", e.getMessage());
        }
    }
    private void notification() throws ParseException {

        Date dateTimeNow = simpleDateFormat.parse(date);
        String dateNotif = "2021-05-28 13:54:28";
        Date dateTimeNotif = simpleDateFormat.parse(dateNotif);

        Log.e(TAG, "Datetimenow: " + dateTimeNow);

        Log.e(TAG, "dateTimeNotif: " + dateTimeNotif);

    }


    private void initializeCountDrawer() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.agent_start_files_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new AgentFilesFragment()).commit();

                break;
            case R.id.spec_mission_item:
                Log.e(TAG, "Spec array" + specActive.size());
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, specActive);
                break;
            case R.id.labor_mission_item:

                break;
            case R.id.instant_mission_item:
                break;
            case R.id.rankings_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new LeaderboardFragment()).commit();

                break;
            case R.id.extra_attendances_item:
                ExtraAttendancesFragment extraAttendancesFragment = ExtraAttendancesFragment.newInstance(userData.getUserAccount().getAgentNumber());
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, extraAttendancesFragment).commit();
                break;
            case R.id.achievs_item:
                AchievementsFragment achievementsFragment = AchievementsFragment.newInstance(userData.getUserAccount().getAgentNumber());
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, achievementsFragment).commit();
                break;
            case R.id.badges_item:
                BadgesFragment badgesFragment = BadgesFragment.newInstance(userData.getUserAccount().getAgentNumber(), userData.getUserAccount().getCountBadgesStyle());
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, badgesFragment).commit();break;
            case R.id.main_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();

                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
            System.out.println(i);
            bottomNavigation.disableItemAtPosition(i);
        }
        bottomNavigation.setItemDisableColor(Color.parseColor("#777777"));
    }


    public void callService() {
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


                if (userAccount.getCountMission() == 0) bottomNavigation.setNotification("0", 0);
                else
                    bottomNavigation.setNotification(String.valueOf(userAccount.getCountMission()), 0);

                if (userAccount.getCountAvatar() == 0) bottomNavigation.setNotification("0", 1);
                else
                    bottomNavigation.setNotification(String.valueOf(userAccount.getCountAvatar()), 1);

                if (userAccount.getCountBitcoin() == 0) bottomNavigation.setNotification("0", 2);
                else
                    bottomNavigation.setNotification(String.valueOf(userAccount.getCountBitcoin()), 2);

                if (userAccount.getCountExacoin() == 0) bottomNavigation.setNotification("0", 3);
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


    public void logout(MenuItem item) {
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

    private void callListMission(String sGame) {

        Call<Missions> call = service.listMissions(sGame);
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

                laboText.setText(String.valueOf(laboActive.size()));
                instantText.setText(String.valueOf(fastActive.size()));
                specText.setText(String.valueOf(specActive.size()));
            }

            @Override
            public void onFailure(Call<Missions> call, Throwable t) {
            }
        });
    }

}