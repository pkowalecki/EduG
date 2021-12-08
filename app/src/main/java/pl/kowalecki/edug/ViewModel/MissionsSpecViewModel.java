package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpecModel;
import pl.kowalecki.edug.Repository.MissionsSpecRepository;

public class MissionsSpecViewModel extends AndroidViewModel {

    private MissionsSpecRepository missionsSpecRepository;
    private LiveData<MissionSpec> missionSpecLiveData;
    private LiveData<MissionSpecModel> missionSpecModelResultLiveData;


    public MissionsSpecViewModel(@NonNull Application application) {
        super(application);
        missionsSpecRepository = new MissionsSpecRepository();
        missionSpecLiveData = missionsSpecRepository.getMissionSpecLiveData();
        missionSpecModelResultLiveData = missionsSpecRepository.getMissionSpecModelMutableLiveData();
    }

    public void getSpecMission(String sSys, String sLang, String sGame, String mMission, String sLogin, String sHash, String sCrc) {
        missionsSpecRepository.callSpecMission(sSys, sLang, sGame, mMission, sLogin, sHash, sCrc);
    }

    public void setSpecMission(String sSys, String sLang, String sGame, String mMissionNumber, String str1, String str2, String str3, String str4, String sLogin, String sHash, String mCrc) {
        missionsSpecRepository.finishMission(sSys, sLang, sGame, mMissionNumber, str1, str2, str3, str4, sLogin, sHash, mCrc);
    }

    public LiveData<MissionSpecModel> getMissionSpecModelResultLiveData() {
        return missionSpecModelResultLiveData;
    }

    public LiveData<MissionSpec> getMissionSpecLiveData() {
        return missionSpecLiveData;
    }

}
