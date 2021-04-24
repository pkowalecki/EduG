package pl.kowalecki.edug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import pl.kowalecki.edug.Data.UserAccount;
import pl.kowalecki.edug.Data.UserLogin;
import pl.kowalecki.edug.Data.WebServiceData;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Button logoutButton;

    SessionManagement sessionManagement;
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
        actionBarDrawerToggle.syncState();
        WebServiceData webServiceData = new WebServiceData();
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);

        String sSys = sessionManagement.getSys();
        String sLang = sessionManagement.getLang();
        String sGame = sessionManagement.getGame();
        String sLogin = sessionManagement.getLogin();
        String sHash = sessionManagement.getHash();
        String sCrc = sessionManagement.getCRC();
        System.out.println(sSys);
        webServiceData.userAccountData(sSys,sLang, sGame, sLogin, sHash, sCrc);
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
