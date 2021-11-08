package pl.kowalecki.edug.ViewModel;

import android.app.Application;
import android.view.inputmethod.ExtractedText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Attendances.ExtraAttendance;
import pl.kowalecki.edug.Model.Attendances.ListAttendances;
import pl.kowalecki.edug.Repository.AttendancesRepository;

public class AttendancesViewModel extends AndroidViewModel {

    private AttendancesRepository attendancesRepository;
    private LiveData<ListAttendances> listAttendancesLiveData;
    private LiveData<List<ExtraAttendance>> laboAttendancesLiveData;
    private LiveData<List<ExtraAttendance>> specAttendancesLiveData;


    public AttendancesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        attendancesRepository = new AttendancesRepository();
        listAttendancesLiveData = attendancesRepository.getAttendancesResponseLiveData();
        laboAttendancesLiveData = attendancesRepository.getLaboAttendancesResponseLiveData();
        specAttendancesLiveData = attendancesRepository.getSpecAttendancesResponseLiveData();
    }

    public void getAttendances(String idu, String idg){
        attendancesRepository.getAttendances(idu, idg);
    }

    public LiveData<ListAttendances> getListAttendancesLiveData(){
        return listAttendancesLiveData;
    }

    public LiveData<List<ExtraAttendance>> getLaboAttendancesLiveData() {
        return laboAttendancesLiveData;
    }

    public LiveData<List<ExtraAttendance>> getSpecAttendancesLiveData() {
        return specAttendancesLiveData;
    }
}
