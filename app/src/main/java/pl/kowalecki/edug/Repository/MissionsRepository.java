package pl.kowalecki.edug.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.Model.Missions.Missions;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionsRepository {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = Calendar.getInstance().getTime();
    String date = simpleDateFormat.format(currentDate);;
    private static final String TAG = MissionsRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);

    public MutableLiveData<List<ListMission>> allMissionsListMutableLiveData;

    private List<ListMission> allList;
    private List<ListMission> specList;
    private List<ListMission> laboList;
    private List<ListMission> fastList;

    private final ExecutorService executorService;

    public MissionsRepository(){

        allMissionsListMutableLiveData = new MutableLiveData<>();

        allList = new ArrayList<>();
        specList = new ArrayList<>();
        laboList = new ArrayList<>();
        fastList = new ArrayList<>();

        executorService = Executors.newSingleThreadExecutor();
    }

    public void getMissions(String idg){
        executorService.execute(() ->{
            apiRequest.listMissions(idg).enqueue(new Callback<Missions>() {
                @Override
                public void onResponse(Call<Missions> call, Response<Missions> response) {
                    if (response.body() != null){
                        try{

                        Date dateTimeNow = simpleDateFormat.parse(date);
                        for (int i = 0; i<response.body().getListMissions().size(); i++){
                            Date dateTimeStart = simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStart());
                            Date dateTimeEnd = simpleDateFormat.parse(response.body().getListMissions().get(i).getMission().getStop());
                            if (dateTimeNow.after(dateTimeStart)) {
                                if (dateTimeNow.before(dateTimeEnd)) {
                                    allList.add(response.body().getListMissions().get(i));
                                    allMissionsListMutableLiveData.setValue(allList);
                                }}

                            if (response.body().getListMissions().get(i).getMission().getType().equals("labo")) {
                                if (dateTimeNow.after(dateTimeStart)) {
                                    if (dateTimeNow.before(dateTimeEnd)) {
                                        laboList.add(response.body().getListMissions().get(i));
                                    }}}

                            if (response.body().getListMissions().get(i).getMission().getType().equals("fast")) {
                                if (dateTimeNow.after(dateTimeStart)) {
                                    if (dateTimeNow.before(dateTimeEnd)) {
                                        fastList.add(response.body().getListMissions().get(i));
                                    }}}

                            if (response.body().getListMissions().get(i).getMission().getType().equals("spec")) {
                                if (dateTimeNow.after(dateTimeStart)) {
                                    if (dateTimeNow.before(dateTimeEnd)) {
                                        specList.add(response.body().getListMissions().get(i));
                                    }}}

                        }
                        if (allMissionsListMutableLiveData.getValue() == null){
                            allMissionsListMutableLiveData.setValue(null);
                        }

                    }catch (ParseException ignored) {
                        }}
                }

                @Override
                public void onFailure(Call<Missions> call, Throwable t) {

                }
            });
        });

    }



    public LiveData<List<ListMission>> getAllMissionsResponseLiveData(){
        return allMissionsListMutableLiveData;
    }

    public List<ListMission> getSpecMissionsList(){
        return specList;
    }

    public List<ListMission> getFastMissionsList(){
        return fastList;
    }

    public List<ListMission> getLaboMissionsList(){
        return laboList;
    }

    public List<ListMission> getAllMissionsList(){
        return allList;
    }


}
