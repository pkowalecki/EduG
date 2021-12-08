package pl.kowalecki.edug.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Model.Games.ListGames;
import pl.kowalecki.edug.Model.User.LoginResult;
import pl.kowalecki.edug.Model.User.UserData;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.GamesViewModel;
import pl.kowalecki.edug.ViewModel.LoginViewModel;


public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    MD5Cipher md5Cipher = new MD5Cipher();
    UserLogin userLogin = new UserLogin();
    SessionManagement sessionManagement;
    GamesViewModel gamesViewModel;
    LoginViewModel loginViewModel;
    Context context;

    private String TAG = MainActivity.class.getSimpleName();
    private Spinner spinner;
    private TextInputLayout loginInputField, passwordInputField;
    private TextInputEditText emailaddress;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.edug_black));

        gamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        spinner = (Spinner) findViewById(R.id.spinner);
        loginInputField = (TextInputLayout) findViewById(R.id.loginField);
        emailaddress = (TextInputEditText) findViewById(R.id.emailaddress);
        emailaddress.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_baseline_person_24), null);
        passwordInputField = (TextInputLayout) findViewById(R.id.passwordField);
        submit = (Button) findViewById(R.id.loginButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        sessionManagement = new SessionManagement(getApplicationContext());

        callGames();


        gamesViewModel.getListGamesLiveData().observe(this, new Observer<ListGames>() {
            @Override
            public void onChanged(ListGames gamesList) {
                if (gamesList.getGame() != null) {
                    initSpinner(gamesViewModel.getListGamesList());
                } else {
                    Log.e(TAG, "nul");
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            callGames();
            swipeRefreshLayout.setRefreshing(false);
        });

        submit.setOnClickListener(v -> {

            userLogin.setSys("wp");
            userLogin.setLang("pl");
            userLogin.setGame(spinner.getSelectedItem().toString());
            userLogin.setLogin(loginInputField.getEditText().getText().toString());
            userLogin.setHash(md5Cipher.md5(passwordInputField.getEditText().getText().toString()));
            userLogin.setCrc(md5Cipher.md5(userLogin.getPassword() + userLogin.getSys() + userLogin.getLang() + userLogin.getGame() + userLogin.getLogin() + userLogin.getHash()));

            if (validateLogin() && validatePassword()) {
                loginViewModel.callLogin(userLogin.getSys(), userLogin.getLang(), userLogin.getGame(), userLogin.getLogin(), userLogin.getHash(), userLogin.getCrc());
                loginViewModel.getLoginResultLiveData().observe(this, new Observer<LoginResult>() {
                    @Override
                    public void onChanged(LoginResult loginResult) {
                        if (loginResult.getUserLogin().getResult()) {
                            doLogin(userLogin.getSys(), userLogin.getLang(), userLogin.getGame(), userLogin.getLogin(), userLogin.getHash(), userLogin.getCrc());
                        } else {
                            showFailedDialog("Błąd logowania", "Wprowadzono niepoprawne dane");
                        }
                    }
                });
            }
        });
    }

    private void showFailedDialog(String title, String comment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(comment)
                .setCancelable(true)
                .setPositiveButton("ANULUJ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void callGames() {
        gamesViewModel.callGames();
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

    @Override
    protected void onStart() {
        super.onStart();

        if (checkNetworkState()) {
            checkSession();
        }

    }

    private void checkSession() {
        if (sessionManagement.getLoginToEdug()) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    private boolean validateLogin() {
        String emailInput = loginInputField.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            loginInputField.setError("Pole nie może być puste");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            loginInputField.setError("Wprowadź poprawny adres e-mail");
            return false;
        } else {
            loginInputField.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {
        String passwordInput = passwordInputField.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            passwordInputField.setError("Pole nie może być puste");
            return false;
        } else {
            passwordInputField.setError(null);
            return true;
        }
    }


    private void doLogin(final String sys, final String lang, final String game, final String login, final String hash, final String crc) {

        sessionManagement.setLoginToEdug(true);
        sessionManagement.setSys(sys);
        sessionManagement.setLang(lang);
        sessionManagement.setGame(game);
        sessionManagement.setLogin(login);
        sessionManagement.setHash(hash);
        sessionManagement.setCRC(crc);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void initSpinner(List<String> game) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, game);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}



