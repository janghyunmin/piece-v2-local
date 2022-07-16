package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.ConsentDetailDTO;
import com.bsstandard.piece.data.repository.ConsentDetailRepository;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : ConsentDetailViewModel
 * author         : piecejhm
 * date           : 2022/06/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/21        piecejhm       최초 생성
 */
public class ConsentDetailViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<ConsentDetailDTO> consentDetailData = new MutableLiveData<>();
    public final ConsentDetailRepository consentDetailRepository;

    public ConsentDetailViewModel(@NonNull Application application){
        super(application);
        consentDetailRepository = new ConsentDetailRepository();
    }
    public MutableLiveData<ConsentDetailDTO> getConsentDetailData(String consentCode) {
        consentDetailData = loadConsentDetailData(consentCode);
        return consentDetailData;
    }

    public MutableLiveData<ConsentDetailDTO> loadConsentDetailData(String consentCode){
        return consentDetailRepository.getConsentDetailData(consentCode);
    }
}
