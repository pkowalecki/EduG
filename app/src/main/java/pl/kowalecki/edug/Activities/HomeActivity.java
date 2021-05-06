package pl.kowalecki.edug.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.kowalecki.edug.Fragments.LeaderboardFragment;
import pl.kowalecki.edug.Fragments.SpecialMissionFragment;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.User.UserAccount;
import pl.kowalecki.edug.Model.User.UserData;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.Model.WebServiceData;
import pl.kowalecki.edug.Fragments.AgentFilesFragment;
import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.ReceiveData;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private final String TAG = HomeActivity.class.getSimpleName();
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Button logoutButton;
    SessionManagement sessionManagement;
    LinearLayout linearLayout;
    UserAccount userAccount = new UserAccount();
    UserData userData = new UserData();
    UserService userService;
    WebServiceData webServiceData;
    String sSys, sLang, sGame, sLogin, sHash, sCrc;
    AHBottomNavigation bottomNavigation;
    CircleImageView ivResult;
    TextView userName, userInfo;
    ReceiveData receiveData = new ReceiveData();
    UserLogin userLogin = new UserLogin();
    FilesList filesList  = new FilesList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavigationView navigationView = findViewById(R.id.nav_view);
        sessionManagement = new SessionManagement(getApplicationContext());
        logoutButton = (Button) findViewById(R.id.logoutButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
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

        callService();
        gravatarImage();



//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartFilesFragment()).commit();
//            navigationView.setCheckedItem(R.id.agent_start_files_item);
//        }

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

    private void gravatarImage() {

        ImageView imageView1 = (ImageView) findViewById(R.id.image_gravatar);
        String uri = "https://gravatar.com/avatar/" + MD5Cipher.md5(sLogin) + "?d=wavatar";
//        Picasso.get().load(uri).into(imageView);
        // Picasso.get().load(uri).into(imageView1);


    }

    private void bottomNavigationMenu(Integer elements) {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigationView);
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
        UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
        Call<UserData> call = service.userAccount(sSys, sLang, sGame, sLogin, sHash, sCrc);

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                userName = (TextView) findViewById(R.id.user_name);
                userInfo = (TextView) findViewById(R.id.user_info);
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
