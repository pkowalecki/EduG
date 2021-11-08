package pl.kowalecki.edug.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.MissionLabo.MissionLabo;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsLaboRepository {

    private final static String TAG = MissionsLaboRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    public MutableLiveData<MissionLabo> missionLaboMutableLiveData;
    private final ExecutorService executorService;

    public MissionsLaboRepository(){
        missionLaboMutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void callLaboMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc){
        executorService.execute(() -> {
            apiRequest.getLaboMissionData(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc).enqueue(new Callback<MissionLabo>() {
                @Override
                public void onResponse(Call<MissionLabo> call, Response<MissionLabo> response) {
                    if (response.body() != null){
                        missionLaboMutableLiveData.setValue(response.body());
                    }

                    if (missionLaboMutableLiveData.getValue() == null){
                        missionLaboMutableLiveData.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<MissionLabo> call, Throwable t) {
                    missionLaboMutableLiveData.setValue(null);
                }
            });
        });


    }

    public LiveData<MissionLabo> getLaboMissionResponseLiveData(){
        return missionLaboMutableLiveData;
    }

}
