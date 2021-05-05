package pl.kowalecki.edug.Model.Achievements;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Achievements {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("idm")
    @Expose
    private String idm;
    @SerializedName("codename")
    @Expose
    private String codename;
    @SerializedName("points")
    @Expose
    private String points;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdm() {
        return idm;
    }

    public void setIdm(String idm) {
        this.idm = idm;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}