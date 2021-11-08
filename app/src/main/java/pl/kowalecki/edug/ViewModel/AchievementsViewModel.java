package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Achievements.Achievements;
import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Achievements.ListAchievements;
import pl.kowalecki.edug.Repository.AchievementsRepository;

public class AchievementsViewModel extends AndroidViewModel {

    private AchievementsRepository achievementsRepository;
    private LiveData<ListAchievements> achievementsLiveData;
    private LiveData<List<ExtraAchievement>> specAchievementsLiveData;
    private LiveData<List<ExtraAchievement>> lastAchievementsLiveData;
    private LiveData<List<ExtraAchievement>> fastAchievementsLiveData;
    private LiveData<List<ExtraAchievement>> laboAchievementsLiveData;

    public AchievementsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        achievementsRepository = new AchievementsRepository();
        achievementsLiveData = achievementsRepository.getAchievementsResponseLiveData();
        specAchievementsLiveData = achievementsRepository.getSpecAchievementsResponseLiveData();
        lastAchievementsLiveData = achievementsRepository.getLastAchievementsResponseLiveData();
        fastAchievementsLiveData = achievementsRepository.getFastAchievementsResponseLiveData();
        laboAchievementsLiveData = achievementsRepository.getLaboAchievementsResponseLiveData();
    }

    public void getAchievements(String idg, String idu) {
        achievementsRepository.getAchievements(idg, idu);
    }

    public LiveData<ListAchievements> getAchievementsLiveData() {
        return achievementsLiveData;

    }

    public LiveData<List<ExtraAchievement>> getSpecAchievementsLiveData() {
        return specAchievementsLiveData;
    }

    public LiveData<List<ExtraAchievement>> getLaboAchievementsLiveData() {
        return laboAchievementsLiveData;
    }

    public LiveData<List<ExtraAchievement>> getFastAchievementsLiveData() {
        return fastAchievementsLiveData;

    }
    public LiveData<List<ExtraAchievement>> getLastAchievementsLiveData() {
        return lastAchievementsLiveData;
    }


}
