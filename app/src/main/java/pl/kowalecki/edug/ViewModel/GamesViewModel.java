package pl.kowalecki.edug.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Games.GamesList;
import pl.kowalecki.edug.Model.Games.GamesModel;
import pl.kowalecki.edug.Model.Games.ListGames;
import pl.kowalecki.edug.Repository.GamesRepository;

public class GamesViewModel extends AndroidViewModel {

    private static final String TAG = GamesModel.class.getSimpleName();
    private LiveData<ListGames> listGamesLiveData;
    private List<String> listGamesList;
    GamesRepository gamesRepository;

    public GamesViewModel(@NonNull  Application application) {
        super(application);
        gamesRepository = new GamesRepository();
        listGamesList = gamesRepository.getListGames();
        listGamesLiveData = gamesRepository.getListGamesMutableLiveData();

    }

    public LiveData<ListGames> getListGamesLiveData() {
        return listGamesLiveData;
    }

    public List<String> getListGamesList() {
        return listGamesList;
    }

    public void callGames(){
        Log.e(TAG, "Games Called");
        gamesRepository.callGames();
    }
}
