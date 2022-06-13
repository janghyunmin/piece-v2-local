package com.bsstandard.piece.data.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.bsstandard.piece.dao.VersionDAO;
import com.bsstandard.piece.data.repository.VersionRepository;



/**
 * packageName    : com.bsstandard.piece.data.model
 * fileName       : VersionViewModel
 * author         : piecejhm
 * date           : 2022/06/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/13        piecejhm       최초 생성
 */
public class VersionViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<VersionDAO> versionData = new MutableLiveData<>();
    public final VersionRepository versionRepository;


    public VersionViewModel(@NonNull Application application) {
        super(application);
        versionRepository = new VersionRepository();
    }
    public MutableLiveData<VersionDAO> getVersionData(String deviceType){
        versionData = loadVersionData(deviceType);
        return versionData;
    }
    public MutableLiveData<VersionDAO> loadVersionData(String deviceType){
        return versionRepository.getVersionData(deviceType);
    }

}
