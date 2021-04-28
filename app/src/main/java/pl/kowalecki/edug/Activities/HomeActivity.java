package pl.kowalecki.edug.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Data.UserAccount;

import pl.kowalecki.edug.Data.UserDataTest;
import pl.kowalecki.edug.Data.WebServiceData;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikartm.support.BadgeDrawer;
import ru.nikartm.support.ImageBadgeView;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private final String TAG = HomeActivity.class.getSimpleName();
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Button logoutButton;
    SessionManagement sessionManagement;
    LinearLayout linearLayout;
    UserAccount userAccount = new UserAccount();
    UserDataTest userDataTest = new UserDataTest();
    UserService userService;
    WebServiceData webServiceData;
    String sSys, sLang, sGame, sLogin, sHash, sCrc;
    AHBottomNavigation bottomNavigation ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManagement = new SessionManagement(getApplicationContext());
        logoutButton = (Button)findViewById(R.id.logoutButton);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navbar_open_pl, R.string.navbar_close_pl);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        bottomNavigationMenu(5); // liczba elementów, które znajdują się w menu dolnym
//        enableBottomBar(false);
        actionBarDrawerToggle.syncState();
        webServiceData = new WebServiceData();
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        sCrc = sessionManagement.getCRC();
        callService();
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

        for(int i = 0; i <elements; i++){
            System.out.println(i);
            bottomNavigation.disableItemAtPosition(i);
        }

        bottomNavigation.setItemDisableColor(Color.parseColor("#777777"));




    }

//    private void enableBottomBar(boolean enable){
//        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++){
//            bottomNavigationView.getMenu().getItem(i).setEnabled(enable);
//
//        }
//    }

    public void callService() {
        UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
        Call<UserDataTest> call = service.userAccount(sSys, sLang, sGame, sLogin, sHash, sCrc);
        call.enqueue(new Callback<UserDataTest>() {
            @Override
            public void onResponse(Call<UserDataTest> call, Response<UserDataTest> response) {
                Log.e(TAG, call.request().url().toString());
                userDataTest = response.body();


                Log.e(TAG, "On rest"+ userDataTest.getUserAccount().getCountMission());

                Log.e(TAG, "przed "+ userAccount.getCountMission());
                userAccount.setCountMission(userDataTest.getUserAccount().getCountMission());
                userAccount.setCountAvatar(userDataTest.getUserAccount().getCountAvatar());
                userAccount.setCountBitcoin(userDataTest.getUserAccount().getCountBitcoin());
                userAccount.setCountExacoin(userDataTest.getUserAccount().getCountExacoin());
                userAccount.setCountPoint(userDataTest.getUserAccount().getCountPoint());
                userAccount.setCountBadgesStyle(userDataTest.getUserAccount().getCountBadgesStyle());

                Log.e(TAG, "po "+ userAccount.getCountExacoin());


                if (userAccount.getCountMission() == 0)bottomNavigation.setNotification("0", 0);
                else bottomNavigation.setNotification(String.valueOf(userAccount.getCountMission()), 0);

                if(userAccount.getCountAvatar() == 0)bottomNavigation.setNotification("0", 1);
                else bottomNavigation.setNotification(String.valueOf(userAccount.getCountAvatar()), 1);

                if (userAccount.getCountBitcoin() == 0)bottomNavigation.setNotification("0", 2);
                else bottomNavigation.setNotification(String.valueOf(userAccount.getCountBitcoin()), 2);

                if (userAccount.getCountExacoin() == 0)bottomNavigation.setNotification("0", 3);
                else bottomNavigation.setNotification(String.valueOf(userAccount.getCountExacoin()), 3);

                if (userAccount.getCountPoint() == 0)bottomNavigation.setNotification("0", 4);
                else bottomNavigation.setNotification(String.valueOf(userAccount.getCountPoint()), 4);

            }

            @Override
            public void onFailure(Call<UserDataTest> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void logout(View view){
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
