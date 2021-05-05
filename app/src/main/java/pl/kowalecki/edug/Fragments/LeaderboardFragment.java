package pl.kowalecki.edug.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.kowalecki.edug.Model.Leaderboards.ListLeaderboards;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {
    SessionManagement sessionManagement;
    private final String TAG = LeaderboardFragment.class.getSimpleName();
    ListLeaderboards listLeaderboards;
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        receiveLeaderboards(sGame);


        return inflater.inflate(R.layout.fragment_rankings, container, false);
    }

    public void receiveLeaderboards(String sGame){
        Call<ListLeaderboards> call = service.extraLeaderboards(sGame);
        call.enqueue(new Callback<ListLeaderboards>() {
            @Override
            public void onResponse(Call<ListLeaderboards> call, Response<ListLeaderboards> response) {
                ListLeaderboards res = response.body();

                Log.e(TAG, "RESPONSE: " + res.getExtraLeaderboards().size());
                for (int i = 0; i < res.getExtraLeaderboards().size(); i++){

                    listLeaderboards.setExtraLeaderboards(res.getExtraLeaderboards());

                }
                Log.e(TAG, "list extraliders: " + listLeaderboards.getExtraLeaderboards().size());
                Log.e(TAG, "simple boi: " + listLeaderboards.getExtraLeaderboards().get(1).getPosition().getEmail());

            }

            @Override
            public void onFailure(Call<ListLeaderboards> call, Throwable t) {

            }
        });
    }

}
