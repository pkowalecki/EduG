package pl.kowalecki.edug.Model.Attendances;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListAttendances {

    @SerializedName("extra_attendances")
    @Expose
    private List<ExtraAttendance> extraAttendances = null;

    public List<ExtraAttendance> getExtraAttendances() {
        return extraAttendances;
    }

    public void setExtraAttendances(List<ExtraAttendance> extraAttendances) {
        this.extraAttendances = extraAttendances;
    }

}