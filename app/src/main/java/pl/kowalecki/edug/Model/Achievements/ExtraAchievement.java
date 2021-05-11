package pl.kowalecki.edug.Model.Achievements;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ExtraAchievement {

    @SerializedName("mission")
    @Expose
    private Achievements achievements;

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }

}