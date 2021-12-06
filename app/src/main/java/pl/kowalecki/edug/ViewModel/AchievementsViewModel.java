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

    private LiveData<List<ExtraAchievement>> specAchievementListLiveData;

    private List<ExtraAchievement> specAchievementsList;
    private List<ExtraAchievement> lastAchievementsList;
    private List<ExtraAchievement> fastAchievementsList;
    private List<ExtraAchievement> laboAchievementsList;

    public AchievementsViewModel(@NonNull Application application) {
        super(application);
        achievementsRepository = new AchievementsRepository();

        specAchievementListLiveData = achievementsRepository.getSpecListMutable();

        specAchievementsList = achievementsRepository.getSpecList();
        lastAchievementsList = achievementsRepository.getLastList();
        fastAchievementsList = achievementsRepository.getFastList();
        laboAchievementsList = achievementsRepository.getLaboList();
    }


    public void getAchievements(String idg, String idu) {
        achievementsRepository.getAchievements(idg, idu);
    }

    public LiveData<List<ExtraAchievement>> getSpecAchievementListLiveData() {
        return specAchievementListLiveData;
    }

    public List<ExtraAchievement> getSpecAchievementsList() {
        return specAchievementsList;
    }

    public List<ExtraAchievement> getLastAchievementsList() {
        return lastAchievementsList;
    }

    public List<ExtraAchievement> getFastAchievementsList() {
        return fastAchievementsList;
    }

    public List<ExtraAchievement> getLaboAchievementsList() {
        return laboAchievementsList;
    }

}
