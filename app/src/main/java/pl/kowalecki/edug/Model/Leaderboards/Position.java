package pl.kowalecki.edug.Model.Leaderboards;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Position {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("idu")
    @Expose
    private String idu;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("points")
    @Expose
    private String points;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdu() {
        return idu;
    }

    public void setIdu(String idu) {
        this.idu = idu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}