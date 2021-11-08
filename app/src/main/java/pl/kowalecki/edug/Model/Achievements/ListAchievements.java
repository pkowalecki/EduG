package pl.kowalecki.edug.Model.Achievements;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListAchievements {

    @SerializedName("extra_achievements")
    @Expose
    private List<ExtraAchievement> extraAchievements = null;

    public List<ExtraAchievement> getExtraAchievements() {
        return extraAchievements;
    }

    public void setExtraAchievements(List<ExtraAchievement> extraAchievements) {
        this.extraAchievements = extraAchievements;
    }

    public ListAchievements() {
    }

    public ListAchievements(List<ExtraAchievement> extraAchievements) {
        this.extraAchievements = extraAchievements;
    }

    @Override
    public String toString() {
        return "ListAchievements{" +
                "extraAchievements=" + extraAchievements +
                '}';
    }
}