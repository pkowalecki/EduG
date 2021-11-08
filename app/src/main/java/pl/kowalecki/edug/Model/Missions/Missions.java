package pl.kowalecki.edug.Model.Missions;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Missions {

    @SerializedName("list_missions")
    @Expose
    private List<ListMission> listMissions = null;

    public List<ListMission> getListMissions() {
        return listMissions;
    }

    public void setListMissions(List<ListMission> listMissions) {
        this.listMissions = listMissions;
    }

    @Override
    public String toString() {
        return "Missions{" +
                "listMissions=" + listMissions +
                '}';
    }
}