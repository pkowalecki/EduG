package pl.kowalecki.edug.Model.MissionFast;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MissionFastModel {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("codename")
    @Expose
    private String codename;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("intro_time")
    @Expose
    private String introTime;
    @SerializedName("intro_text")
    @Expose
    private String introText;
    @SerializedName("mission_start")
    @Expose
    private String missionStart;
    @SerializedName("mission_text")
    @Expose
    private String missionText;
    @SerializedName("finish_time")
    @Expose
    private String finishTime;
    @SerializedName("finish_text")
    @Expose
    private String finishText;
    @SerializedName("comment")
    @Expose
    private String comment;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIntroTime() {
        return introTime;
    }

    public void setIntroTime(String introTime) {
        this.introTime = introTime;
    }

    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getMissionStart() {
        return missionStart;
    }

    public void setMissionStart(String missionStart) {
        this.missionStart = missionStart;
    }

    public String getMissionText() {
        return missionText;
    }

    public void setMissionText(String missionText) {
        this.missionText = missionText;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getFinishText() {
        return finishText;
    }

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    public String getComment() {
        return comment;
    }

}