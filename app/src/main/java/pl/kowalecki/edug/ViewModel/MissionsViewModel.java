package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.Model.Missions.Missions;
import pl.kowalecki.edug.Repository.MissionsRepository;

public class MissionsViewModel extends AndroidViewModel {

    private MissionsRepository missionsRepository;

    private LiveData<List<ListMission>> allMissionListLiveData;

    private List<ListMission> specMissionList;
    private List<ListMission> fastMissionList;
    private List<ListMission> laboMissionList;
    private List<ListMission> allMissionList;

    public MissionsViewModel(@NonNull Application application) {
        super(application);

        missionsRepository = new MissionsRepository();

        allMissionListLiveData = missionsRepository.getAllMissionsResponseLiveData();

        specMissionList = missionsRepository.getSpecMissionsList();
        fastMissionList = missionsRepository.getFastMissionsList();
        laboMissionList = missionsRepository.getLaboMissionsList();
        allMissionList = missionsRepository.getAllMissionsList();
    }

    public void getAllMissions(String sGame) {
        missionsRepository.getMissions(sGame);
    }


    public LiveData<List<ListMission>> getAllActiveMissionListLiveData() {
        return allMissionListLiveData;
    }

    public List<ListMission> getFastMissionList() {
        return fastMissionList;
    }

    public List<ListMission> getLaboMissionList() {
        return laboMissionList;
    }

    public List<ListMission> getAllListMissions() {
        return allMissionList;
    }

    public List<ListMission> getSpecMissionList() {
        return specMissionList;
    }
}
