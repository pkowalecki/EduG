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

    private LiveData<List<ExtraAttendance>> laboAttendancesLiveData;

    private List<ExtraAttendance> laboAttendancesList;
    private List<ExtraAttendance> specAttendancesList;


    public AttendancesViewModel(@NonNull Application application) {
        super(application);
        attendancesRepository = new AttendancesRepository();
        laboAttendancesLiveData = attendancesRepository.getLaboAttendancesMutableLiveData();
        laboAttendancesList = attendancesRepository.getLaboAttendancesList();
        specAttendancesList = attendancesRepository.getSpecAttendancesList();
    }


    public void getAttendances(String idu, String idg){
        attendancesRepository.getAttendances(idu, idg);
    }

    public LiveData<List<ExtraAttendance>> getLaboAttendancesLiveData() {
        return laboAttendancesLiveData;
    }

    public List<ExtraAttendance> getLaboAttendancesList() {
        return laboAttendancesList;
    }

    public List<ExtraAttendance> getSpecAttendancesList() {
        return specAttendancesList;
    }
}
