package pl.kowalecki.edug.Model.Games;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GamesList {

    @SerializedName("list_games")
    @Expose
    private List<ListGames> listGames = null;

    public List<ListGames> getListGames() {
        return listGames;
    }

    public void setListGames(List<ListGames> listGames) {
        this.listGames = listGames;
    }

}
