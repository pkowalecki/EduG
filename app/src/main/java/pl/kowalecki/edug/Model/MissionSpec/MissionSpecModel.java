package pl.kowalecki.edug.Model.MissionSpec;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MissionSpecModel {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("codename")
    @Expose
    private String codename;
    @SerializedName("mission_start")
    @Expose
    private String missionStart;
    @SerializedName("intro_text")
    @Expose
    private String introText;
    @SerializedName("question_1")
    @Expose
    private String question1;
    @SerializedName("answers_1")
    @Expose
    private Answers1 answers1;
    @SerializedName("question_2")
    @Expose
    private String question2;
    @SerializedName("answers_2")
    @Expose
    private Answers2 answers2;
    @SerializedName("question_3")
    @Expose
    private String question3;
    @SerializedName("answers_3")
    @Expose
    private Answers3 answers3;
    @SerializedName("question_4")
    @Expose
    private String question4;
    @SerializedName("answers_4")
    @Expose
    private Answers4 answers4;
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

    public String getMissionStart() {
        return missionStart;
    }

    public void setMissionStart(String missionStart) {
        this.missionStart = missionStart;
    }

    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public Answers1 getAnswers1() {
        return answers1;
    }

    public void setAnswers1(Answers1 answers1) {
        this.answers1 = answers1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public Answers2 getAnswers2() {
        return answers2;
    }

    public void setAnswers2(Answers2 answers2) {
        this.answers2 = answers2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public Answers3 getAnswers3() {
        return answers3;
    }

    public void setAnswers3(Answers3 answers3) {
        this.answers3 = answers3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public Answers4 getAnswers4() {
        return answers4;
    }

    public void setAnswers4(Answers4 answers4) {
        this.answers4 = answers4;
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

    public String getComment() {return comment;}
}