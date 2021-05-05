package pl.kowalecki.edug.Model.Leaderboards;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListLeaderboards {

    @SerializedName("extra_leaderboards")
    @Expose
    private List<ExtraLeaderboard> extraLeaderboards = null;

    public List<ExtraLeaderboard> getExtraLeaderboards() {
        return extraLeaderboards;
    }

    public void setExtraLeaderboards(List<ExtraLeaderboard> extraLeaderboards) {
        this.extraLeaderboards = extraLeaderboards;
    }

}