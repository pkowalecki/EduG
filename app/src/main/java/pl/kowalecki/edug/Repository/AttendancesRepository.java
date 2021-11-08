package pl.kowalecki.edug.Repository;

import androidx.lifecycle.LiveData;
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
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);
    public MutableLiveData<ListAttendances> attendancesMutableLiveData;
    public MutableLiveData<List<ExtraAttendance>> laboAttendancesMutableLiveData;
    public MutableLiveData<List<ExtraAttendance>> specAttendancesMutableLiveData;
    List<ExtraAttendance> list1;
    List<ExtraAttendance> list2;
    private final ExecutorService executorService;

    public AttendancesRepository(){
        attendancesMutableLiveData = new MutableLiveData<>();
        laboAttendancesMutableLiveData = new MutableLiveData<>();
        specAttendancesMutableLiveData = new MutableLiveData<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getAttendances(String idg, String idu){
        executorService.execute(() -> {
            apiRequest.extraAttendances(idg, idu).enqueue(new Callback<ListAttendances>() {
                @Override
                public void onResponse(Call<ListAttendances> call, Response<ListAttendances> response) {
                    if(response.body() != null){
                        attendancesMutableLiveData.setValue(response.body());

                        for (int i = 0; i<attendancesMutableLiveData.getValue().getExtraAttendances().size(); i++){
                            if(response.body().getExtraAttendances().get(i).getAttendance().getType().equals("L")){
                                list1.add(response.body().getExtraAttendances().get(i));
                                laboAttendancesMutableLiveData.setValue(list1);
                            }
                            if (response.body().getExtraAttendances().get(i).getAttendance().getType().equals("W")){
                                list2.add(response.body().getExtraAttendances().get(i));
                                specAttendancesMutableLiveData.setValue(list2);
                            }
                        }

                        if (laboAttendancesMutableLiveData.getValue() == null){
                            laboAttendancesMutableLiveData.setValue(null);
                        }
                        if (specAttendancesMutableLiveData.getValue() == null){
                            specAttendancesMutableLiveData.setValue(null);
                        }
                    }

                }

                @Override
                public void onFailure(Call<ListAttendances> call, Throwable t) {
                    attendancesMutableLiveData.setValue(null);
                }
            });

        });

    }

    public LiveData<ListAttendances> getAttendancesResponseLiveData(){
        return attendancesMutableLiveData;
    }

    public LiveData<List<ExtraAttendance>> getLaboAttendancesResponseLiveData(){
        return laboAttendancesMutableLiveData;
    }

    public LiveData<List<ExtraAttendance>> getSpecAttendancesResponseLiveData(){
        return specAttendancesMutableLiveData;
    }
}
