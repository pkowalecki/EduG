package pl.kowalecki.edug.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardsRepository {

    private static final String TAG = LeaderboardsRepository.class.getSimpleName();
    private MutableLiveData<ListLeaderboards> mutableLiveData;
    private MutableLiveData<List<ExtraLeaderboard>> specMutableLiveData;
    private MutableLiveData<List<ExtraLeaderboard>> laboMutableLiveData;
    private MutableLiveData<List<ExtraLeaderboard>> fastMutableLiveData;
    private MutableLiveData<List<ExtraLeaderboard>> allMutableLiveData;
    private List<ExtraLeaderboard> list1;
    private List<ExtraLeaderboard> list2;
    private List<ExtraLeaderboard> list3;
    private List<ExtraLeaderboard> list4;
    private final ExecutorService executorService;
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);

    public LeaderboardsRepository(){
        mutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
        specMutableLiveData = new MutableLiveData<>();
        laboMutableLiveData = new MutableLiveData<>();
        fastMutableLiveData = new MutableLiveData<>();
        allMutableLiveData = new MutableLiveData<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
    }

    public void getLeaderboards(String sGame){
        executorService.execute(() -> {
            apiRequest.extraLeaderboards(sGame).enqueue(new Callback<ListLeaderboards>() {
                @Override
                public void onResponse(Call<ListLeaderboards> call, Response<ListLeaderboards> response) {
                    if (response.body() != null){
                        mutableLiveData.setValue(response.body());
                        for (int i = 0; i<mutableLiveData.getValue().getExtraLeaderboards().size(); i++){
                            if (mutableLiveData.getValue().getExtraLeaderboards().get(i).getPosition().getType().equals("spec")){
                                list1.add(mutableLiveData.getValue().getExtraLeaderboards().get(i));
                                specMutableLiveData.setValue(list1);
                            }
                            if (mutableLiveData.getValue().getExtraLeaderboards().get(i).getPosition().getType().equals("labo")){
                                list2.add(mutableLiveData.getValue().getExtraLeaderboards().get(i));
                                laboMutableLiveData.setValue(list2);
                            }
                            if (mutableLiveData.getValue().getExtraLeaderboards().get(i).getPosition().getType().equals("fast")){
                                list3.add(mutableLiveData.getValue().getExtraLeaderboards().get(i));
                                fastMutableLiveData.setValue(list3);
                            }
                            if (mutableLiveData.getValue().getExtraLeaderboards().get(i).getPosition().getType().equals("full")){
                                list4.add(mutableLiveData.getValue().getExtraLeaderboards().get(i));
                                allMutableLiveData.setValue(list4);
                            }
                        }

                        if (mutableLiveData.getValue() == null){
                            mutableLiveData.setValue(null);
                        }
                        if (specMutableLiveData.getValue() == null){
                            specMutableLiveData.setValue(null);
                        }
                        if (laboMutableLiveData.getValue() == null){
                            laboMutableLiveData.setValue(null);
                        }
                        if (fastMutableLiveData.getValue() == null){
                            fastMutableLiveData.setValue(null);
                        }
                        if (allMutableLiveData.getValue() == null){
                            allMutableLiveData.setValue(null);
                        }

                    }

                }

                @Override
                public void onFailure(Call<ListLeaderboards> call, Throwable t) {
                    mutableLiveData.setValue(null);
                }
            });
        });
    }

    public LiveData<ListLeaderboards> listLeaderboardsLiveData(){
        return mutableLiveData;
    }

    public LiveData<List<ExtraLeaderboard>> specListLeaderboardsLiveData(){ return specMutableLiveData;}

    public LiveData<List<ExtraLeaderboard>> laboListLeaderboardsLiveData(){ return laboMutableLiveData;}

    public LiveData<List<ExtraLeaderboard>> fastListLeaderboardsLiveData(){ return fastMutableLiveData;}

    public LiveData<List<ExtraLeaderboard>> allListLeaderboardsLiveData(){ return allMutableLiveData;}
}
