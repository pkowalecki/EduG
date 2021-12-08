package pl.kowalecki.edug.Model.User;


import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class LoginResult {

    @SerializedName("user_login")
    @Expose
    private UserLoginModel userLogin;

    public UserLoginModel getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLoginModel userLogin) {
        this.userLogin = userLogin;
    }

}
