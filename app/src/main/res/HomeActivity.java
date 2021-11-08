package pl.kowalecki.edug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Activities.MainActivity;
import pl.kowalecki.edug.Model.User.UserAccount;

import pl.kowalecki.edug.Model.User.UserData;
import pl.kowalecki.edug.Model.WebServiceData;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.TestRetrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikartm.support.ImageBadgeView;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String TAG = pl.kowalecki.edug.Activities.HomeActivity.class.getSimpleName();
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Button logoutButton;
    SessionManagement sessionManagement;
    LinearLayout linearLayout;
    UserAccount userAccount = new UserAccount();
    UserData userData = new UserData();
    ApiRequest apiRequest;
    WebServiceData webServiceData;
    String sSys, sLang, sGame, sLogin, sHash, sCrc;
    ImageBadgeView missionsToolbar, avatarsToolbar, bitcoinsToolbar, exacoinsToolbar, missionsPointsToolbar;

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

        missionsPointsToolbar = (ImageBadgeView)findViewById(R.id.missionsPointsToolbar);
        avatarsToolbar = (ImageBadgeView)findViewById(R.id.avatarsToolbar);
        bitcoinsToolbar = (ImageBadgeView)findViewById(R.id.bitcoinsToolbar);
        exacoinsToolbar = (ImageBadgeView)findViewById(R.id.exacoinsToolbar);
        missionsToolbar = (ImageBadgeView)findViewById(R.id.missionsToolbar);
        actionBarDrawerToggle.syncState();
        webServiceData = new WebServiceData();
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        sCrc = sessionManagement.getCRC();
        callService();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutBottomMenu);



    }

    public void callService() {
        ApiRequest service = ServiceGenerator.getRetrofit().create(ApiRequest.class);
        Call<UserData> call = service.userAccount(sSys, sLang, sGame, sLogin, sHash, sCrc);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
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

                exacoinsToolbar.setBadgeValue(userAccount.getCountExacoin());
                missionsToolbar.setBadgeValue(userAccount.getCountMission());
                avatarsToolbar.setBadgeValue(userAccount.getCountAvatar());
                bitcoinsToolbar.setBadgeValue(userAccount.getCountBitcoin());

                missionsPointsToolbar.setBadgeValue(userAccount.getCountPoint()).setMaxBadgeValue(500);
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
