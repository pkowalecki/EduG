package pl.kowalecki.edug.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Games.GamesList;
import pl.kowalecki.edug.Model.Games.GamesModel;
import pl.kowalecki.edug.Model.Games.ListGames;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesRepository {
    private static final String TAG = GamesRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    ExecutorService executorService;
    private MutableLiveData<ListGames> listGamesMutableLiveData;
    private List<String> listGames;

    public GamesRepository() {
        executorService = Executors.newSingleThreadExecutor();
        listGames = new ArrayList<>();
        listGamesMutableLiveData = new MutableLiveData<>();
    }

    public void callGames() {
    executorService.execute(() -> {
        apiRequest.listGames().enqueue(new Callback<GamesList>() {
            @Override
            public void onResponse(Call<GamesList> call, Response<GamesList> response) {
                Log.e(TAG, "Games Called");
                for (int i = 0; i<response.body().getListGames().size(); i++){
                    if (response.body().getListGames().get(i).getGame().getActive().equals("Y")){
                        Log.e(TAG, response.body().getListGames().get(i).getGame().getIdg());
                        listGames.add(response.body().getListGames().get(i).getGame().getIdg());
                        listGamesMutableLiveData.setValue(response.body().getListGames().get(i));
                    }

                }
            }

            @Override
            public void onFailure(Call<GamesList> call, Throwable t) {

            }
        });
    });
    }

    public MutableLiveData<ListGames> getListGamesMutableLiveData() {
        return listGamesMutableLiveData;
    }

    public List<String> getListGames() {
        return listGames;
    }
}
