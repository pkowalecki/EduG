package pl.kowalecki.edug;

import pl.kowalecki.edug.Retrofit.ApiRequest;

public class ApiUtils {

    public static final String WEB_SERVICE_URL = "https://www.edug.pl/_webservices/";
    public static ApiRequest getUserService(){
        return RetrofitClient.getClient(WEB_SERVICE_URL).create(ApiRequest.class);
    }

}
