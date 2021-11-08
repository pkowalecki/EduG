package pl.kowalecki.edug.Repository;

import android.nfc.Tag;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Achievements.Achievements;
import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Achievements.ListAchievements;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementsRepository {

    private static final String TAG = AchievementsRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    private MutableLiveData<ListAchievements> achievementsResponseMutableLiveData;
    private MutableLiveData<List<ExtraAchievement>> specAchievementsResponseMutableLiveData;
    private MutableLiveData<List<ExtraAchievement>> laboAchievementsResponseMutableLiveData;
    private MutableLiveData<List<ExtraAchievement>> fastAchievementsResponseMutableLiveData;
    private MutableLiveData<List<ExtraAchievement>> lastAchievementsResponseMutableLiveData;
    private List<ExtraAchievement> list1;
    private List<ExtraAchievement> list2;
    private List<ExtraAchievement> list3;
    private List<ExtraAchievement> list4;
    private final ExecutorService executorService;

    public AchievementsRepository(){
            list1 = new ArrayList<>();
            list2 = new ArrayList<>();
            list3 = new ArrayList<>();
            list4 = new ArrayList<>();
            achievementsResponseMutableLiveData = new MutableLiveData<>();
            specAchievementsResponseMutableLiveData = new MutableLiveData<>();
            laboAchievementsResponseMutableLiveData = new MutableLiveData<>();
            fastAchievementsResponseMutableLiveData = new MutableLiveData<>();
            lastAchievementsResponseMutableLiveData = new MutableLiveData<>();
            executorService = Executors.newSingleThreadExecutor();
    }

    public void getAchievements(String idg, String idu){
        executorService.execute(() ->
                apiRequest.extraAchievements(idg, idu)
                .enqueue(new Callback<ListAchievements>() {
                    @Override
                    public void onResponse(Call<ListAchievements> call, Response<ListAchievements> response) {
                        if (response.body()!=null){
                            achievementsResponseMutableLiveData.setValue(response.body());

                            for (int i = 0 ; i<achievementsResponseMutableLiveData.getValue().getExtraAchievements().size(); i++){
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("spec")){
                                    list1.add(response.body().getExtraAchievements().get(i));
                                    specAchievementsResponseMutableLiveData.setValue(list1);
                                }
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("labo")){
                                    list2.add(response.body().getExtraAchievements().get(i));
                                    laboAchievementsResponseMutableLiveData.setValue(list2);
                                }
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("fast")){
                                    list3.add(response.body().getExtraAchievements().get(i));
                                    fastAchievementsResponseMutableLiveData.setValue(list3);
                                }
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("last")){
                                    list4.add(response.body().getExtraAchievements().get(i));
                                    lastAchievementsResponseMutableLiveData.setValue(list4);
                                }
                            }
                            if (specAchievementsResponseMutableLiveData.getValue() == null){
                                specAchievementsResponseMutableLiveData.setValue(null);
                            }

                            if (laboAchievementsResponseMutableLiveData.getValue() == null){
                                laboAchievementsResponseMutableLiveData.setValue(null);
                            }
                            if (fastAchievementsResponseMutableLiveData.getValue() == null){
                                fastAchievementsResponseMutableLiveData.setValue(null);
                            }
                            if (specAchievementsResponseMutableLiveData.getValue() == null){
                                specAchievementsResponseMutableLiveData.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListAchievements> call, Throwable t) {
                        achievementsResponseMutableLiveData.setValue(null);
                    }
                }));

    }

    public LiveData<ListAchievements> getAchievementsResponseLiveData(){
        return achievementsResponseMutableLiveData;

    }
    public LiveData<List<ExtraAchievement>> getSpecAchievementsResponseLiveData(){
        return specAchievementsResponseMutableLiveData;

    }
    public LiveData<List<ExtraAchievement>> getLaboAchievementsResponseLiveData(){
        return laboAchievementsResponseMutableLiveData;

    }
    public LiveData<List<ExtraAchievement>> getFastAchievementsResponseLiveData(){
        return fastAchievementsResponseMutableLiveData;

    }
    public LiveData<List<ExtraAchievement>> getLastAchievementsResponseLiveData(){
        return lastAchievementsResponseMutableLiveData;
    }

}
