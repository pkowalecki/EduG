package pl.kowalecki.edug.Model.Badges;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListBadge {

    @SerializedName("extra_badges")
    @Expose
    private List<ExtraBadge> extraBadges = null;

    public List<ExtraBadge> getExtraBadges() {
        return extraBadges;
    }

    public void setExtraBadges(List<ExtraBadge> extraBadges) {
        this.extraBadges = extraBadges;
    }

}