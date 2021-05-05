package pl.kowalecki.edug;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Fragments.StartFilesFragment;
import pl.kowalecki.edug.Model.Files.FileData;
import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Model.User.UserAccount;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ReceiveData {
    FilesList filesList = new FilesList();
    ListFile listFile = new ListFile();
    FileData fileData = new FileData();
    private final String TAG = ReceiveData.class.getSimpleName();
    UserService service = ServiceGenerator.getRetrofit().create(UserService.class);
    List<ListFile> dataList = new ArrayList<>();
    public ReceiveData() {

    }


    public void receiveListFromServer(String sGame){
        Call <FilesList> call = service.filesDataArray(sGame);
        call.enqueue(new Callback<FilesList>() {
            @Override
            public void onResponse(Call<FilesList> call, Response<FilesList> response) {

                FilesList res;
                String category;
                String filename;
                String location;
                List<ListFile> aaaa = new ArrayList<>();
                res = response.body();


                for (int i = 0; i < res.getListFiles().toArray().length; i++) {

                    category = response.body().getListFiles().get(i).getFileData().getCategory();
                    filename = response.body().getListFiles().get(i).getFileData().getFilemane();
                    location = response.body().getListFiles().get(i).getFileData().getLocation();

                    fileData.setCategory(category);
                    fileData.setFilemane(filename);
                    fileData.setLocation(location);


                    listFile.setFileData(fileData);
//                    Log.e(TAG, "NEXTONE" + listFile.getFileData().getFilemane());
                    aaaa.add(listFile);


                    filesList.setListFiles(aaaa);

                    if (filesList.getListFiles().get(i).getFileData().getCategory().equals("spec")){



//                        specItems.add(response.body().getListFiles().get(i).getFileData().getCategory());
//                        specItems.add(response.body().getListFiles().get(i).getFileData().getLocation());
//                        specItems.add(response.body().getListFiles().get(i).getFileData().getFilemane());
//                        Log.e(TAG, "specItems: "+ specItems);
//                        Log.e(TAG, "getSpecItems: "+ getSpecItems());
//                        Log.e(TAG, "NEXTONEOEN: "+ filesList.getListFiles().get(i).getFileData().getFilemane());
                    }
                }
                Log.e(TAG, "files list" + filesList.getListFiles());
            }

            @Override
            public void onFailure(Call<FilesList> call, Throwable t) {
                Log.e(TAG, "URL on failure: " + call.request().url().toString());
                Log.e(TAG, "on failure: " + t.getMessage());
            }
        });

    }

    }
