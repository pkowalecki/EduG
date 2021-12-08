package pl.kowalecki.edug.Model.User;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UserLoginModel {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("comment")
    @Expose
    private String comment;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
