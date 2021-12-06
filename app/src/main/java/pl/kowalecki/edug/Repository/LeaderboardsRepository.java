package pl.kowalecki.edug.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Attendances.ExtraAttendance;
import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.ViewModel.LeaderboardsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardsRepository {

    private static final String TAG = LeaderboardsRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);

    private MutableLiveData<List<ExtraLeaderboard>> specListMutable;
    private List<ExtraLeaderboard> specList;
    private List<ExtraLeaderboard> laboList;
    private List<ExtraLeaderboard> fastList;
    private List<ExtraLeaderboard> topList;
    private final ExecutorService executorService;


    public LeaderboardsRepository(){
        executorService = Executors.newSingleThreadExecutor();
        specListMutable = new MutableLiveData<>();
        specList = new ArrayList<>();
        laboList = new ArrayList<>();
        fastList = new ArrayList<>();
        topList = new ArrayList<>();
    }


    public void getLeaderboards(String sGame){
        executorService.execute(() -> {
            apiRequest.extraLeaderboards(sGame).enqueue(new Callback<ListLeaderboards>() {
                @Override
                public void onResponse(Call<ListLeaderboards> call, Response<ListLeaderboards> response) {
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().getExtraLeaderboards().size(); i++) {
                            if (response.body().getExtraLeaderboards().get(i).getPosition().getType().equals("spec")) {
                                specList.add(response.body().getExtraLeaderboards().get(i));
                                specListMutable.setValue(specList);
                            }
                            if (response.body().getExtraLeaderboards().get(i).getPosition().getType().equals("labo")) {
                                laboList.add(response.body().getExtraLeaderboards().get(i));
                            }
                            if (response.body().getExtraLeaderboards().get(i).getPosition().getType().equals("fast")) {
                                fastList.add(response.body().getExtraLeaderboards().get(i));
                            }
                            if (response.body().getExtraLeaderboards().get(i).getPosition().getType().equals("full")) {
                                topList.add(response.body().getExtraLeaderboards().get(i));
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<ListLeaderboards> call, Throwable t) {
                }
            });
        });
    }


    public List<ExtraLeaderboard> getSpecList() {
        return specList;
    }

    public LiveData<List<ExtraLeaderboard>> getSpecListMutable() {
        return specListMutable;
    }

    public List<ExtraLeaderboard> getLaboList() {
        return laboList;
    }

    public List<ExtraLeaderboard> getFastList() {
        return fastList;
    }

    public List<ExtraLeaderboard> getTopList() {
        return topList;
    }

}
