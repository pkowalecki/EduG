package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.Repository.LeaderboardsRepository;

public class LeaderboardsViewModel extends AndroidViewModel {
    private LeaderboardsRepository leaderboardsRepository;
    private LiveData<ListLeaderboards> listLeaderboardsLiveData;
    private LiveData<List<ExtraLeaderboard>> specLeaderboardsLiveData;
    private LiveData<List<ExtraLeaderboard>> laboLeaderboardsLiveData;
    private LiveData<List<ExtraLeaderboard>> fastLeaderboardsLiveData;
    private LiveData<List<ExtraLeaderboard>> allLeaderboardsLiveData;

    public LeaderboardsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        leaderboardsRepository = new LeaderboardsRepository();
        listLeaderboardsLiveData = leaderboardsRepository.listLeaderboardsLiveData();
        specLeaderboardsLiveData = leaderboardsRepository.specListLeaderboardsLiveData();
        laboLeaderboardsLiveData = leaderboardsRepository.laboListLeaderboardsLiveData();
        fastLeaderboardsLiveData = leaderboardsRepository.fastListLeaderboardsLiveData();
        allLeaderboardsLiveData = leaderboardsRepository.allListLeaderboardsLiveData();
    }

    public void getAllLeaderboards(String sGame){
        leaderboardsRepository.getLeaderboards(sGame);
    }

    public LiveData<ListLeaderboards> getListLeaderboardsLiveData(){
        return listLeaderboardsLiveData;
    }

    public LiveData<List<ExtraLeaderboard>> getSpecLeaderboardsLiveData(){
        return specLeaderboardsLiveData;
    }

    public LiveData<List<ExtraLeaderboard>> getLaboLeaderboardsLiveData() {
        return laboLeaderboardsLiveData;
    }

    public LiveData<List<ExtraLeaderboard>> getFastLeaderboardsLiveData() {
        return fastLeaderboardsLiveData;
    }

    public LiveData<List<ExtraLeaderboard>> getAllLeaderboardsLiveData() {
        return allLeaderboardsLiveData;
    }
}
