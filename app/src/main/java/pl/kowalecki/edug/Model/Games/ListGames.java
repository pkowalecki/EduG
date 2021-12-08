package pl.kowalecki.edug.Model.Games;


import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListGames {

    @SerializedName("game")
    @Expose
    private GamesModel game;

    public GamesModel getGame() {
        return game;
    }

    public void setGame(GamesModel game) {
        this.game = game;
    }

}