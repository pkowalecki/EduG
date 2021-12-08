package pl.kowalecki.edug.Repository;

import android.app.Service;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Activities.MainActivity;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpecModel;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.ViewModel.MissionsSpecViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsSpecRepository {

    private static final String TAG = MissionsSpecRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    private MutableLiveData<MissionSpec> missionSpecMutableLiveData;
    private MutableLiveData<MissionSpecModel> missionSpecModelMutableLiveData;

    ExecutorService executorService;

    public MissionsSpecRepository() {

        missionSpecMutableLiveData = new MutableLiveData<>();
        missionSpecModelMutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void callSpecMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc) {
        executorService.execute(() -> {
            apiRequest.getSpecMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc)
                    .enqueue(new Callback<MissionSpec>() {
                        @Override
                        public void onResponse(Call<MissionSpec> call, Response<MissionSpec> response) {
                            if (response.body() != null) {
                                missionSpecMutableLiveData.setValue(response.body());
                            }

                            if (missionSpecMutableLiveData.getValue() == null) {
                                missionSpecMutableLiveData.setValue(null);
                            }
                        }

                        @Override
                        public void onFailure(Call<MissionSpec> call, Throwable t) {
                            missionSpecMutableLiveData.setValue(null);
                        }
                    });
        });
    }

    public void finishMission(String sSys, String sLang, String sGame, String mMissionNumber, String str1, String str2, String str3, String str4, String sLogin, String sHash, String mCrc) {
        executorService.execute(() -> {
            apiRequest.setSpecMissionData(sSys, sLang, sGame, mMissionNumber, str1, str2, str3, str4, sLogin, sHash, mCrc)
                    .enqueue(new Callback<MissionSpec>() {
                        @Override
                        public void onResponse(Call<MissionSpec> call, Response<MissionSpec> response) {
                            if (response.body() != null){
                                missionSpecModelMutableLiveData.setValue(response.body().getMissionSpecModel());
                            }
                        }

                        @Override
                        public void onFailure(Call<MissionSpec> call, Throwable t) {
                            Log.e("FAILURE", "Unable to submit post to API" );
                        }
                    });
        });

    }



    public LiveData<MissionSpec> getMissionSpecLiveData() {
        return missionSpecMutableLiveData;
    }

    public MutableLiveData<MissionSpecModel> getMissionSpecModelMutableLiveData() {
        return missionSpecModelMutableLiveData;
    }
}



