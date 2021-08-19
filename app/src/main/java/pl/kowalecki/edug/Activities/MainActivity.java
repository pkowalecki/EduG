package pl.kowalecki.edug.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.kowalecki.edug.ApiUtils;
import pl.kowalecki.edug.Model.Games.ListGames;
import pl.kowalecki.edug.Model.User.UserAccount;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.Model.WebServiceData;
import pl.kowalecki.edug.HttpHandler;
import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.ReceiveData;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;


public class MainActivity extends AppCompatActivity {


    private TextView mTextViewResult;
    SwipeRefreshLayout swipeRefreshLayout;
   // private RequestQueue mQueue;
    ArrayList<HashMap<String, String>> gamesList;
    private String TAG = MainActivity.class.getSimpleName();
    private Spinner spinner;
    private TextInputLayout loginInputField, passwordInputField;
    private TextInputEditText emailaddress;
    private Button submit;
    MD5Cipher md5Cipher = new MD5Cipher();
    ArrayList<String> listOfGames = new ArrayList<>();
    UserService userService;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    WebServiceData webServiceData = new WebServiceData();
    UserLogin userLogin = new UserLogin();
    UserAccount userAccount = new UserAccount();
    ListGames listGames = new ListGames();
    SessionManagement sessionManagement;
    CircleImageView ivResult;
    ReceiveData receiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("Density",  getResources().getDisplayMetrics().density + "");
        //mQueue = Volley.newRequestQueue(this);
        spinner=(Spinner) findViewById(R.id.spinner);
        loginInputField=(TextInputLayout) findViewById(R.id.loginField);
        emailaddress=(TextInputEditText) findViewById(R.id.emailaddress);
        emailaddress.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_24), null);
        passwordInputField=(TextInputLayout) findViewById(R.id.passwordField);
        submit=(Button)findViewById(R.id.loginButton);
        userService = ApiUtils.getUserService();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        sessionManagement = new SessionManagement(getApplicationContext());
        getGames();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getGames();
            swipeRefreshLayout.setRefreshing(false);
        });

        submit.setOnClickListener(v -> {
            userLogin.setSys("wp");
            userLogin.setLang("pl");
            userLogin.setGame(spinner.getSelectedItem().toString());
            userLogin.setLogin(loginInputField.getEditText().getText().toString());
            userLogin.setHash(md5Cipher.md5(passwordInputField.getEditText().getText().toString()));
            userLogin.setCrc(md5Cipher.md5(userLogin.getPassword()+ userLogin.getSys()+ userLogin.getLang()+ userLogin.getGame()+ userLogin.getLogin()+ userLogin.getHash()));

           if(validateLogin() && validatePassword()){
                doLogin(userLogin.getSys(), userLogin.getLang(), userLogin.getGame(), userLogin.getLogin(), userLogin.getHash(), userLogin.getCrc());
                webServiceData.userAccountData(userLogin.getSys(), userLogin.getLang(), userLogin.getGame(), userLogin.getLogin(), userLogin.getHash(), userLogin.getCrc());
            }
        });
    }

    private boolean checkNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check network state
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //Chcek status
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
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

    @Override
    protected void onStart() {
        super.onStart();
        if (checkNetworkState())
        {
            checkSession();
        }

    }

    private void checkSession() {
        if (sessionManagement.getLoginToEdug()){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    private boolean validateLogin(){
        String emailInput = loginInputField.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            loginInputField.setError("Pole nie może być puste");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            loginInputField.setError("Wprowadź poprawny adres e-mail");
            return false;
        }
        else{
            loginInputField.setError(null);
            return true;
        }

    }
    private boolean validatePassword(){
        String passwordInput = passwordInputField.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            passwordInputField.setError("Pole nie może być puste");
            return false;
        }else{
            passwordInputField.setError(null);
            return true;
        }
    }
    private void doLogin(final String sys,final String lang,final String game,final String login,final String hash,final String crc){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        executorService.execute(() -> {

            HttpHandler sh = new HttpHandler();
            String url = userService.login(sys, lang, game, login, hash, crc).request().url().toString();
            Log.e(TAG,"URL DO ODPOWIEDZI " + url);
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject user_login = jsonObject.getJSONObject("user_login");

                        String result = user_login.getString("result");
                        String comment = user_login.getString("comment");

                        if (result.equals("true")) {
                    sessionManagement.setLoginToEdug(true);
                    sessionManagement.setSys(sys);
                    sessionManagement.setLang(lang);
                    sessionManagement.setGame(game);
                    sessionManagement.setLogin(login);
                    sessionManagement.setHash(hash);
                    sessionManagement.setCRC(crc);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                        } else{
                            runOnUiThread(() -> {
                                builder.setCancelable(true);
                                builder.setTitle("Błąd logowania");
                                builder.setMessage("Wprowadzono niepoprawne dane");
                                builder.setNegativeButton("ANULUJ", (dialog, which) -> dialog.cancel());
                                builder.show();
                            });

                        }
                    } catch (final JSONException ignored) {

                }
                }
        });}

    private void getGames(){
        executorService.execute(() -> {

            HttpHandler sh = new HttpHandler();

            String url = "https://www.edug.pl/_webservices/list_games.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray list_games = jsonObject.getJSONArray("list_games");

                    for(int i = 0; i < list_games.length(); i++){
                        JSONObject g = list_games.getJSONObject(i);
                        JSONObject gameObj = g.getJSONObject("game");
                        listGames.setIdg(gameObj.getString("idg"));
                        listGames.setActive(gameObj.getString("active"));
                        listGames.setMissions(gameObj.getString("missions"));


                        if (listGames.getActive().equals("Y")) {
                            listOfGames.add(listGames.getIdg());
                        }
                    }
                } catch (final JSONException ignored){

                        }
                    }

            handler.post(() -> {
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listOfGames);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            });
        });
    }
}



