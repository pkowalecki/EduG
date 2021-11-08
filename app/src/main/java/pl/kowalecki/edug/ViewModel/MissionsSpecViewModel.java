package pl.kowalecki.edug.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Repository.MissionsSpecRepository;

public class MissionsSpecViewModel extends AndroidViewModel {

    private MissionsSpecRepository missionsSpecRepository;
    private LiveData<MissionSpec> missionSpecLiveData;


    public MissionsSpecViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        missionsSpecRepository = new MissionsSpecRepository();
        missionSpecLiveData = missionsSpecRepository.getMissionSpecLiveData();
    }

    public void getSpecMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc){
        missionsSpecRepository.callSpecMission(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
    }


    public LiveData<MissionSpec> getMissionSpecLiveData() {
        return missionSpecLiveData;
    }

}
