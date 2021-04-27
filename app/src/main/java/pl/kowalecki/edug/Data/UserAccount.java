package pl.kowalecki.edug.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class UserAccount {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("agent_number")
    @Expose
    private String agentNumber;
    @SerializedName("agent_name")
    @Expose
    private String agentName;
    @SerializedName("agent_email")
    @Expose
    private String agentEmail;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("count_bitcoin")
    @Expose
    private Integer countBitcoin;
    @SerializedName("count_avatar")
    @Expose
    private Integer countAvatar;
    @SerializedName("count_exacoin")
    @Expose
    private Integer countExacoin;
    @SerializedName("count_mission")
    @Expose
    private Integer countMission;
    @SerializedName("count_point")
    @Expose
    private Integer countPoint;
    @SerializedName("count_badges_style")
    @Expose
    private Integer countBadgesStyle;
    @SerializedName("lang")
    @Expose
    private String lang;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getCountBitcoin() {
        return countBitcoin;
    }

    public void setCountBitcoin(Integer countBitcoin) {
        this.countBitcoin = countBitcoin;
    }

    public Integer getCountAvatar() {
        return countAvatar;
    }

    public void setCountAvatar(Integer countAvatar) {
        this.countAvatar = countAvatar;
    }

    public Integer getCountExacoin() {
        return countExacoin;
    }

    public void setCountExacoin(Integer countExacoin) {
        this.countExacoin = countExacoin;
    }

    public Integer getCountMission() {
        return countMission;
    }

    public void setCountMission(Integer countMission) {
        this.countMission = countMission;
    }

    public Integer getCountPoint() {
        return countPoint;
    }

    public void setCountPoint(Integer countPoint) {
        this.countPoint = countPoint;
    }

    public Integer getCountBadgesStyle() {
        return countBadgesStyle;
    }

    public void setCountBadgesStyle(Integer countBadgesStyle) {
        this.countBadgesStyle = countBadgesStyle;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}