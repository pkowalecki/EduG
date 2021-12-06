package pl.kowalecki.edug.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Leaderboards.ExtraLeaderboard;
import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.Repository.LeaderboardsRepository;

public class LeaderboardsViewModel extends AndroidViewModel {
    private static final String TAG = LeaderboardsViewModel.class.getSimpleName();
    private LeaderboardsRepository leaderboardsRepository;
    private List<ExtraLeaderboard> specLeaderboards;
    private LiveData<List<ExtraLeaderboard>> specLeaderboardsMutable;
    private List<ExtraLeaderboard> laboLeaderboards;
    private List<ExtraLeaderboard> fastLeaderboards;
    private List<ExtraLeaderboard> topLeaderboards;



    public LeaderboardsViewModel(@NonNull Application application) {
        super(application);
        leaderboardsRepository = new LeaderboardsRepository();
        specLeaderboardsMutable = leaderboardsRepository.getSpecListMutable();
        specLeaderboards = leaderboardsRepository.getSpecList();
        laboLeaderboards = leaderboardsRepository.getLaboList();
        fastLeaderboards = leaderboardsRepository.getFastList();
        topLeaderboards = leaderboardsRepository.getTopList();

    }

    public void getAllLeaderboards(String sGame){
        leaderboardsRepository.getLeaderboards(sGame);
    }


    public LiveData<List<ExtraLeaderboard>> getSpecLeaderboardsMutable() {
        return specLeaderboardsMutable;
    }

    public List<ExtraLeaderboard> getSpecLeaderboards() {
        return specLeaderboards;
    }

    public List<ExtraLeaderboard> getLaboLeaderboards() {
        return laboLeaderboards;
    }

    public List<ExtraLeaderboard> getFastLeaderboards() {
        return fastLeaderboards;
    }

    public List<ExtraLeaderboard> getTopLeaderboards() {
        return topLeaderboards;
    }

}
