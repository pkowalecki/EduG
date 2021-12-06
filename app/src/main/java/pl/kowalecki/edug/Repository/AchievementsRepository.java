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

    private MutableLiveData<List<ExtraAchievement>> specListMutable;
    private List<ExtraAchievement> specList;
    private List<ExtraAchievement> laboList;
    private List<ExtraAchievement> fastList;
    private List<ExtraAchievement> lastList;
    private final ExecutorService executorService;

    public AchievementsRepository(){
        executorService = Executors.newSingleThreadExecutor();

        specListMutable = new MutableLiveData<>();

        specList = new ArrayList<>();
        laboList = new ArrayList<>();
        fastList = new ArrayList<>();
        lastList = new ArrayList<>();
    }

    public void getAchievements(String idg, String idu){
        executorService.execute(() ->
                apiRequest.extraAchievements(idg, idu)
                .enqueue(new Callback<ListAchievements>() {
                    @Override
                    public void onResponse(Call<ListAchievements> call, Response<ListAchievements> response) {
                        if (response.body()!=null){

                            for (int i = 0 ; i<response.body().getExtraAchievements().size(); i++){
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("spec")){
                                    specList.add(response.body().getExtraAchievements().get(i));
                                    specListMutable.setValue(specList);
                                }
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("labo")){
                                    laboList.add(response.body().getExtraAchievements().get(i));
                                }
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("fast")){
                                    fastList.add(response.body().getExtraAchievements().get(i));
                                }
                                if (response.body().getExtraAchievements().get(i).getAchievements().getType().equals("last")){
                                    lastList.add(response.body().getExtraAchievements().get(i));
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ListAchievements> call, Throwable t) {

                    }
                }));

    }

    public MutableLiveData<List<ExtraAchievement>> getSpecListMutable() {
        return specListMutable;
    }

    public List<ExtraAchievement> getFastList() {
        return fastList;
    }

    public List<ExtraAchievement> getSpecList() {
        return specList;
    }

    public List<ExtraAchievement> getLaboList() {
        return laboList;
    }

    public List<ExtraAchievement> getLastList() {
        return lastList;
    }
}
