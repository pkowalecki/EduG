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

    private LiveData<List<ListFile>> filesListLiveData;

    private List<ListFile> specFiles;
    private List<ListFile> laboFiles;
    private List<ListFile> additionalFiles;


    public AgentFilesViewModel(@NonNull Application application) {
        super(application);

        agentFilesRepository = new AgentFilesRepository();
        filesListLiveData = agentFilesRepository.getSpecFilesListMutable();
        specFiles = agentFilesRepository.getSpecList();
        laboFiles = agentFilesRepository.getLaboList();
        additionalFiles = agentFilesRepository.getDodaList();
    }

    public void init(){

    }

    public void getFiles(String sGame){
            agentFilesRepository.getAgentFiles(sGame);
    }

    public LiveData<List<ListFile>> getFilesListLiveData() {
        return filesListLiveData;
    }

    public List<ListFile> getSpecFiles() {
        return specFiles;
    }

    public List<ListFile> getLaboFiles() {
        return laboFiles;
    }

    public List<ListFile> getAdditionalFiles() {
        return additionalFiles;
    }
}
