package pl.kowalecki.edug.Repository;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.User.UserData;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDataRepository {

    private final static String TAG = UserDataRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    ExecutorService executorService;
    private MutableLiveData<UserData> userDataMutableLiveData;

    public UserDataRepository() {
        userDataMutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void callUserData(String sSys, String sLang, String sGame, String sLogin, String sHash, String sCrc) {
        executorService.execute(() -> {
            apiRequest.userAccount(sSys, sLang, sGame, sLogin, sHash, sCrc).enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    if (response.body() != null){
                        userDataMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {

                }
            });

        });
    }

    public MutableLiveData<UserData> getUserDataMutableLiveData() {
        return userDataMutableLiveData;
    }
}
