package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.kowalecki.edug.Model.User.LoginResult;
import pl.kowalecki.edug.Repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    LoginRepository loginRepository;
    private LiveData<LoginResult> loginResultLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
        loginResultLiveData = loginRepository.getUserLoginMutableLiveData();
    }

    public void callLogin(String sSys, String sLang, String sGame, String sLogin, String sHash, String sCrc){
        loginRepository.callLogin(sSys, sLang, sGame, sLogin, sHash, sCrc);
    }

    public LiveData<LoginResult> getLoginResultLiveData() {
        return loginResultLiveData;
    }
}
