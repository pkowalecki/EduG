package pl.kowalecki.edug.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Badges.Badge;
import pl.kowalecki.edug.Model.Badges.ListBadge;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadgesRepository {

    private static final String TAG = BadgesRepository.class.getSimpleName();
    private MutableLiveData<ListBadge> mutableLiveData;
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    private final ExecutorService executorService;

    public BadgesRepository(){
        mutableLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getBadges(String sLang, String agentIdu, String sGame){
        executorService.execute(() -> {
            apiRequest.extraBadges(sLang, sGame, agentIdu).enqueue(new Callback<ListBadge>() {
                @Override
                public void onResponse(Call<ListBadge> call, Response<ListBadge> response) {
                    if (response.body() != null){
                        mutableLiveData.setValue(response.body());
                        if (mutableLiveData.getValue() == null){
                            mutableLiveData.setValue(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListBadge> call, Throwable t) {
                    mutableLiveData.setValue(null);
                }
            });
        });

    }

    public LiveData<ListBadge> listBadgeLiveData(){
        return mutableLiveData;
    }
}
