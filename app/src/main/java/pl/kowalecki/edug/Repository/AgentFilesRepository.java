package pl.kowalecki.edug.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kowalecki.edug.Model.Files.FilesList;
import pl.kowalecki.edug.Model.Files.ListFile;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentFilesRepository {

    private static final String TAG = AgentFilesRepository.class.getSimpleName();
    ApiRequest apiRequest = ServiceGenerator.getRetrofit().create(ApiRequest.class);

    private MutableLiveData<List<ListFile>> specFilesListMutable;
    private List<ListFile> laboList;
    private List<ListFile> specList;
    private List<ListFile> dodaList;
    private final ExecutorService executorService;

    public AgentFilesRepository(){

        executorService = Executors.newSingleThreadExecutor();
        specFilesListMutable = new MutableLiveData<>();
        laboList = new ArrayList<>();
        specList = new ArrayList<>();
        dodaList = new ArrayList<>();

    }

    public void getAgentFiles(String idg){
        executorService.execute(() -> {
            apiRequest.filesDataArray(idg).enqueue(new Callback<FilesList>() {
                @Override
                public void onResponse(Call<FilesList> call, Response<FilesList> response) {
                    if (response.body() !=null){

                        for (int i = 0; i<response.body().getListFiles().size(); i++){
                            if (response.body().getListFiles().get(i).getFileData().getCategory().equals("spec")){
                                specList.add(response.body().getListFiles().get(i));
                                specFilesListMutable.setValue(specList);
                            }
                            if (response.body().getListFiles().get(i).getFileData().getCategory().equals("labo")){
                                laboList.add(response.body().getListFiles().get(i));
                            }
                            if (response.body().getListFiles().get(i).getFileData().getCategory().equals("doda")){
                                dodaList.add(response.body().getListFiles().get(i));
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<FilesList> call, Throwable t) {

                }
            });
        });
    }

    public MutableLiveData<List<ListFile>> getSpecFilesListMutable() {
        return specFilesListMutable;
    }

    public List<ListFile> getLaboList() {
        return laboList;
    }

    public List<ListFile> getSpecList() {
        return specList;
    }

    public List<ListFile> getDodaList() {
        return dodaList;
    }
}
