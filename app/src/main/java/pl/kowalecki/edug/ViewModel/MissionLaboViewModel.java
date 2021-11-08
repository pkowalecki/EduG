package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.kowalecki.edug.Model.MissionLabo.MissionLabo;
import pl.kowalecki.edug.Repository.MissionsLaboRepository;

public class MissionLaboViewModel extends AndroidViewModel {

    private MissionsLaboRepository missionsLaboRepository;
    private LiveData<MissionLabo> missionLaboLiveData;

    public MissionLaboViewModel(@NonNull Application application){
        super(application);

    }

    public void init(){
        missionsLaboRepository = new MissionsLaboRepository();
        missionLaboLiveData = missionsLaboRepository.getLaboMissionResponseLiveData();
    }

    public void getLaboMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc){
        missionsLaboRepository.callLaboMission(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
    }

    public LiveData<MissionLabo> getLaboMissionLiveData(){
        return missionLaboLiveData;
    }
}
