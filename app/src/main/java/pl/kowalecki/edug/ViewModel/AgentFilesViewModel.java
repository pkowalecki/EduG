package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.kowalecki.edug.Model.Achievements.ExtraAchievement;
import pl.kowalecki.edug.Model.Achievements.ListAchievements;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Repository.AchievementsRepository;
import pl.kowalecki.edug.Repository.AgentFilesRepository;

public class AgentFilesViewModel extends AndroidViewModel {

    private AgentFilesRepository agentFilesRepository;
    private LiveData<FilesList> agentFilesLiveData;
    private LiveData<List<ListFile>> specFilesLiveData;
    private LiveData<List<ListFile>> laboFilesLiveData;
    private LiveData<List<ListFile>> additionalFilesLiveData;


    public AgentFilesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        agentFilesRepository = new AgentFilesRepository();
        agentFilesLiveData = agentFilesRepository.getFilesResponseLiveData();
        specFilesLiveData = agentFilesRepository.getSpecFilesResponseLiveData();
        laboFilesLiveData = agentFilesRepository.getLaboFilesResponseLiveData();
        additionalFilesLiveData = agentFilesRepository.getAdditionalFilesResponseLiveData();
    }

    public void getFiles(String sGame){
            agentFilesRepository.getAgentFiles(sGame);
    }

    public LiveData<FilesList> getAgentFilesLiveData(){
        return agentFilesLiveData;
    }

    public LiveData<List<ListFile>> getSpecFilesLiveData() {
        return specFilesLiveData;
    }

    public LiveData<List<ListFile>> getLaboFilesLiveData() {
        return laboFilesLiveData;
    }

    public LiveData<List<ListFile>> getAdditionalFilesLiveData() {
        return additionalFilesLiveData;
    }
}
