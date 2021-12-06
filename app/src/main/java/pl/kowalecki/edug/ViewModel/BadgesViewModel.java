package pl.kowalecki.edug.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.kowalecki.edug.Model.Badges.ListBadge;
import pl.kowalecki.edug.Repository.BadgesRepository;

public class BadgesViewModel extends AndroidViewModel {

    private BadgesRepository badgesRepository;
    private LiveData<ListBadge> listBadgeLiveData;

    public BadgesViewModel(@NonNull Application application) {
        super(application);
        badgesRepository = new BadgesRepository();
        listBadgeLiveData = badgesRepository.listBadgeLiveData();
    }


    public void getAllBadges(String sLang, String agentIdu, String sGame){
        badgesRepository.getBadges(sLang, agentIdu, sGame);
    }

    public LiveData<ListBadge> getListBadgeLiveData(){
        return listBadgeLiveData;
    }
}
