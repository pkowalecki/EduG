package pl.kowalecki.edug.Model.MissionSpec;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Answers1 implements Parcelable {

    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("3")
    @Expose
    private String _3;
    @SerializedName("4")
    @Expose
    private String _4;

    protected Answers1(Parcel in){
        _2 = in.readString();
        _1 = in.readString();
        _3 = in.readString();
        _4 = in.readString();
    }

    public String get2() {
        return _2;
    }

    public void set2(String _2) {
        this._2 = _2;
    }

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public String get3() {
        return _3;
    }

    public void set3(String _3) {
        this._3 = _3;
    }

    public String get4() {
        return _4;
    }

    public void set4(String _4) {
        this._4 = _4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(_2);
    dest.writeString(_1);
    dest.writeString(_3);
    dest.writeString(_4);
    }
}
