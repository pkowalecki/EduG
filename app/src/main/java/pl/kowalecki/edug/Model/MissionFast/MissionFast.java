package pl.kowalecki.edug.Model.MissionFast;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MissionFast {

    @SerializedName("mission_fast")
    @Expose
    private MissionFastModel missionFast;

    public MissionFastModel getMissionFast() {
        return missionFast;
    }

    public void setMissionFast(MissionFastModel missionFast) {
        this.missionFast = missionFast;
    }

}