package pl.kowalecki.edug.Model.Missions;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListMission {

    @SerializedName("mission")
    @Expose
    private Mission mission;

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public String toString() {
        return "ListMission{" +
                "mission=" + mission +
                '}';
    }
}