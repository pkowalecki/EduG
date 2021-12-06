package pl.kowalecki.edug.Repository;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Attendances.ExtraAttendance;
import pl.kowalecki.edug.Model.Attendances.ListAttendances;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendancesRepository {

    public static final String TAG = AttendancesRepository.class.getSimpleName();
    private final ExecutorService executorService;
    public MutableLiveData<List<ExtraAttendance>> laboAttendancesMutableLiveData;
    public List<ExtraAttendance> laboAttendancesList;
    public List<ExtraAttendance> specAttendancesList;
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);

    public AttendancesRepository() {
        laboAttendancesMutableLiveData = new MutableLiveData<>();
        laboAttendancesList = new ArrayList<>();
        specAttendancesList = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getAttendances(String idg, String idu) {
        executorService.execute(() -> {
            apiRequest.extraAttendances(idg, idu).enqueue(new Callback<ListAttendances>() {
                @Override
                public void onResponse(Call<ListAttendances> call, Response<ListAttendances> response) {
                    if (response.body() != null) {

                        for (int i = 0; i < response.body().getExtraAttendances().size(); i++) {
                            if (response.body().getExtraAttendances().get(i).getAttendance().getType().equals("L")) {
                                laboAttendancesList.add(response.body().getExtraAttendances().get(i));
                                laboAttendancesMutableLiveData.setValue(laboAttendancesList);
                            }
                            if (response.body().getExtraAttendances().get(i).getAttendance().getType().equals("W")) {
                                specAttendancesList.add(response.body().getExtraAttendances().get(i));
                            }
                        }


                    }

                }

                @Override
                public void onFailure(Call<ListAttendances> call, Throwable t) {
                    laboAttendancesMutableLiveData.setValue(null);
                }
            });

        });

    }

    public MutableLiveData<List<ExtraAttendance>> getLaboAttendancesMutableLiveData() {
        return laboAttendancesMutableLiveData;
    }

    public List<ExtraAttendance> getLaboAttendancesList() {
        return laboAttendancesList;
    }

    public List<ExtraAttendance> getSpecAttendancesList() {
        return specAttendancesList;
    }
}
