package pl.kowalecki.edug;

public class ApiUtils {

    public static final String WEB_SERVICE_URL = "https://www.edug.pl/_webservices/";
    public static UserService getUserService(){
        return RetrofitClient.getClient(WEB_SERVICE_URL).create(UserService.class);
    }

}
