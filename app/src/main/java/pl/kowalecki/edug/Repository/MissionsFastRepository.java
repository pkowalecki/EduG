package pl.kowalecki.edug.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Model.MissionFast.MissionFastModel;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsFastRepository {

    private static final String TAG = MissionsFastRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    ExecutorService executorService;
    private MutableLiveData<MissionFast> missionFastMutableLiveData;
    private MutableLiveData<MissionFastModel> missionFastResponseMutableLiveData;


    public MissionsFastRepository() {
        missionFastMutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
        missionFastResponseMutableLiveData = new MutableLiveData<>();
    }

    public void callFastMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc) {
        executorService.execute(() ->
                apiRequest.getFastMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc)
                        .enqueue(new Callback<MissionFast>() {
                            @Override
                            public void onResponse(Call<MissionFast> call, Response<MissionFast> response) {
                                if (response.body() != null) {
                                    missionFastMutableLiveData.setValue(response.body());
                                    if (missionFastMutableLiveData.getValue() == null) {
                                        missionFastMutableLiveData.setValue(null);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MissionFast> call, Throwable t) {
                            }
                        }));

    }

    public void finishMission(String sSys, String sLang, String sGame, String mMissionNumber, String answer, String sLogin, String sHash, String sCrc) {
        executorService.execute(() -> {
            apiRequest.setFastMissionData(sSys, sLang, sGame, mMissionNumber, answer, sLogin, sHash, sCrc)
                    .enqueue(new Callback<MissionFast>() {
                        @Override
                        public void onResponse(Call<MissionFast> call, Response<MissionFast> response) {
                            if (response.body()!= null){
                                missionFastResponseMutableLiveData.setValue(response.body().getMissionFast());
                            }
                        }

                        @Override
                        public void onFailure(Call<MissionFast> call, Throwable t) {
                        }
                    });
        });

    }

    public MutableLiveData<MissionFast> getFastMissionLiveData() {
        return missionFastMutableLiveData;
    }

    public MutableLiveData<MissionFastModel> getMissionFastResponseMutableLiveData() {
        return missionFastResponseMutableLiveData;
    }
}
