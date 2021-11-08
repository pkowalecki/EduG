package pl.kowalecki.edug.Model.MissionSpec;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MissionSpec {

    @SerializedName("mission_fast")
    @Expose
    private MissionSpecModel missionSpecModel;

    public MissionSpecModel getMissionSpecModel() {
        return missionSpecModel;
    }

    public void setMissionSpecModel(MissionSpecModel missionSpecModel) {
        this.missionSpecModel = missionSpecModel;
    }

    @Override
    public String toString() {
        return "MissionSpec{" +
                "missionSpecModel=" + missionSpecModel +
                '}';
    }
}