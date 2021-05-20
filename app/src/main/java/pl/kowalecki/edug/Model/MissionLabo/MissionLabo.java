package pl.kowalecki.edug.Model.MissionLabo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MissionLabo {

    @SerializedName("mission_fast")
    @Expose
    private MissionLaboModel missionLaboModel;

    public MissionLaboModel getMissionLaboModel() {
        return missionLaboModel;
    }

    public void setMissionLaboModel(MissionLaboModel missionLaboModel) {
        this.missionLaboModel = missionLaboModel;
    }

}
