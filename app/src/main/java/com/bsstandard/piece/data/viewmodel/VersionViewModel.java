package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.VersionDTO;
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
    public MutableLiveData<VersionDTO> versionData = new MutableLiveData<>();
    public final VersionRepository versionRepository; // model 부분 - jhm 2022/06/17


    public VersionViewModel(@NonNull Application application) {
        super(application);
        versionRepository = new VersionRepository();
    }
    public MutableLiveData<VersionDTO> getVersionData(String deviceType){
        versionData = loadVersionData(deviceType);
        return versionData;
    }
    public MutableLiveData<VersionDTO> loadVersionData(String deviceType){
        return versionRepository.getVersionData(deviceType);
    }

}
