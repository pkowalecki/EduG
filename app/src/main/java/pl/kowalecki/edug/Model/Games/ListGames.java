package pl.kowalecki.edug.Model.Games;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListGames {

    @SerializedName("idg")
    @Expose
    private String idg;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("missions")
    @Expose
    private String missions;

    public String getIdg() {
        return idg;
    }

    public void setIdg(String idg) {
        this.idg = idg;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMissions() {
        return missions;
    }

    public void setMissions(String missions) {
        this.missions = missions;
    }

}