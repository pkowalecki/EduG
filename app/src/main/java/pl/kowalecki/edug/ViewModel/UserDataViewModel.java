package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.kowalecki.edug.Model.User.UserData;
import pl.kowalecki.edug.Repository.UserDataRepository;

public class UserDataViewModel extends AndroidViewModel {

    UserDataRepository userDataRepository;

    private LiveData<UserData> userDataLiveData;

    public UserDataViewModel(@NonNull Application application) {
        super(application);

        userDataRepository = new UserDataRepository();
        userDataLiveData = userDataRepository.getUserDataMutableLiveData();
    }

    public void getUserData(String sSys, String sLang, String sGame, String sLogin, String sHash, String sCrc){
        userDataRepository.callUserData(sSys, sLang, sGame, sLogin, sHash, sCrc);
    }

    public LiveData<UserData> getUserDataLiveData() {
        return userDataLiveData;
    }
}
