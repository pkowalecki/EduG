package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Repository.MissionsFastRepository;

public class MissionFastViewModel extends AndroidViewModel {

    private MissionsFastRepository missionsFastRepository;
    private LiveData<MissionFast> missionFastLiveData;

    public MissionFastViewModel(@NonNull Application application) {
        super(application);
    }
    public void init(){
        missionsFastRepository = new MissionsFastRepository();
        missionFastLiveData = missionsFastRepository.getFastMissionLiveData();
    }

    public void getFastMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc){
        missionsFastRepository.callFastMission(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
    }

    public LiveData<MissionFast> getMissionFastLiveData(){
        return missionFastLiveData;
    }
}