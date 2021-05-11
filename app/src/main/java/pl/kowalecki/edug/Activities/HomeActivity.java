package pl.kowalecki.edug.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Fragments.AchievementsFragment;
import pl.kowalecki.edug.Fragments.AgentFilesFragment;
import pl.kowalecki.edug.Fragments.BadgesFragment;
import pl.kowalecki.edug.Fragments.ExtraAttendancesFragment;
import pl.kowalecki.edug.Fragments.LeaderboardFragment;
import pl.kowalecki.edug.Fragments.MissionsFragment;
import pl.kowalecki.edug.Fragments.SpecialMissionFragment;
import pl.kowalecki.edug.Model.Games.ListGames;
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
    Button logoutButton;
    SessionManagement sessionManagement;
    UserAccount userAccount = new UserAccount();
    UserData userData = new UserData();
    WebServiceData webServiceData;
    String sSys, sLang, sGame, sLogin, sHash, sCrc;
    AHBottomNavigation bottomNavigation;
    TextView userName, userInfo;
    ImageView imageView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavigationView navigationView = findViewById(R.id.nav_view);
        sessionManagement = new SessionManagement(getApplicationContext());
        logoutButton = findViewById(R.id.logoutButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navbar_open_pl, R.string.navbar_close_pl);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        bottomNavigationMenu(5); // liczba elementów, które znajdują się w menu dolnym
        actionBarDrawerToggle.syncState();
        webServiceData = new WebServiceData();
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        sCrc = sessionManagement.getCRC();
        navigationView.setNavigationItemSelectedListener(this);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.image_gravatar);
        Picasso.get().load("https://gravatar.com/avatar/ca3c4fee4a98a55552050909ad294f5e?d=wavatar").into(imageView);
        callService();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();
            navigationView.setCheckedItem(R.id.main_menu);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.agent_start_files_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new AgentFilesFragment()).commit();
                break;
            case R.id.spec_mission_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new SpecialMissionFragment()).commit();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, badgesFragment).commit();
                break;
            case R.id.main_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.parent_fragment_container, new MissionsFragment()).commit();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#FF0000"));

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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




}
