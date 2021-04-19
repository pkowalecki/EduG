package pl.kowalecki.edug;

public class ApiUtils {

    public static final String LOGIN_URL = "https://www.edug.pl/_webservices/";
    public static UserService getUserService(){
        return RetrofitClient.getClient(LOGIN_URL).create(UserService.class);
    }

}
