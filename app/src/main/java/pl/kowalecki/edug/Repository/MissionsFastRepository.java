package pl.kowalecki.edug.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsFastRepository {

    private static final String TAG = MissionsFastRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    private MutableLiveData<MissionFast> missionFastMutableLiveData;
    ExecutorService executorService;


    public MissionsFastRepository(){
        missionFastMutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void callFastMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc){
        executorService.execute(() ->
                apiRequest.getFastMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc)
                        .enqueue(new Callback<MissionFast>() {
                    @Override
                    public void onResponse(Call<MissionFast> call, Response<MissionFast> response) {
                        if (response.body() != null){
                            missionFastMutableLiveData.setValue(response.body());

                            if (missionFastMutableLiveData.getValue() == null){
                                missionFastMutableLiveData.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MissionFast> call, Throwable t) {

                    }
                }));

    }

    public LiveData<MissionFast> getFastMissionLiveData(){
        return missionFastMutableLiveData;
    }
}
