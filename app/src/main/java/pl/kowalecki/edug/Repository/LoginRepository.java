package pl.kowalecki.edug.Repository;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.User.LoginResult;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    private static final String TAG = LoginRepository.class.getSimpleName();
    ExecutorService executorService;
    private MutableLiveData<LoginResult> userLoginMutableLiveData;

    public LoginRepository() {
        executorService = Executors.newSingleThreadExecutor();
        userLoginMutableLiveData = new MutableLiveData<>();
    }

    public void callLogin(String sSys, String sLang, String sGame, String sLogin, String sHash, String sCrc){
        executorService.execute(() -> {
            apiRequest.login(sSys, sLang, sGame, sLogin, sHash, sCrc ).enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    if (response.body() != null){
                        userLoginMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {

                }
            });
        });
    }

    public MutableLiveData<LoginResult> getUserLoginMutableLiveData() {
        return userLoginMutableLiveData;
    }
}
