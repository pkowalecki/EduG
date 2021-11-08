package pl.kowalecki.edug.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<FilesList> filesListMutableLiveData;
    private MutableLiveData<List<ListFile>> laboFilesListMutableLiveData;
    private MutableLiveData<List<ListFile>> specFilesListMutableLiveData;
    private MutableLiveData<List<ListFile>> additionalFilesListMutableLiveData;
    private List<ListFile> list1;
    private List<ListFile> list2;
    private List<ListFile> list3;
    private final ExecutorService executorService;

    public AgentFilesRepository(){
        filesListMutableLiveData = new MutableLiveData<>();
        laboFilesListMutableLiveData = new MutableLiveData<>();
        specFilesListMutableLiveData = new MutableLiveData<>();
        additionalFilesListMutableLiveData = new MutableLiveData<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getAgentFiles(String idg){
        executorService.execute(() -> {
            apiRequest.filesDataArray(idg).enqueue(new Callback<FilesList>() {
                @Override
                public void onResponse(Call<FilesList> call, Response<FilesList> response) {
                    if (response.body() !=null){
                        filesListMutableLiveData.setValue(response.body());

                        for (int i = 0; i<filesListMutableLiveData.getValue().getListFiles().size(); i++){
                            if (response.body().getListFiles().get(i).getFileData().getCategory().equals("spec")){
                                list2.add(response.body().getListFiles().get(i));
                                specFilesListMutableLiveData.setValue(list2);
                            }
                            if (response.body().getListFiles().get(i).getFileData().getCategory().equals("labo")){
                                list1.add(response.body().getListFiles().get(i));
                                laboFilesListMutableLiveData.setValue(list1);
                            }
                            if (response.body().getListFiles().get(i).getFileData().getCategory().equals("doda")){
                                list3.add(response.body().getListFiles().get(i));
                                additionalFilesListMutableLiveData.setValue(list3);
                            }
                        }
                        if (specFilesListMutableLiveData.getValue() == null){
                            specFilesListMutableLiveData.setValue(null);
                        }
                        if (laboFilesListMutableLiveData.getValue() == null){
                            laboFilesListMutableLiveData.setValue(null);
                        }
                        if (additionalFilesListMutableLiveData.getValue() == null){
                            additionalFilesListMutableLiveData.setValue(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FilesList> call, Throwable t) {
                    filesListMutableLiveData.setValue(null);
                }
            });
        });
    }

    public LiveData<FilesList> getFilesResponseLiveData(){
        return filesListMutableLiveData;
    }

    public LiveData<List<ListFile>> getLaboFilesResponseLiveData(){
        return  laboFilesListMutableLiveData;
    }
    public LiveData<List<ListFile>> getSpecFilesResponseLiveData(){
        return  specFilesListMutableLiveData;
    }
    public LiveData<List<ListFile>> getAdditionalFilesResponseLiveData(){
        return  additionalFilesListMutableLiveData;
    }
}
