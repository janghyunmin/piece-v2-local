package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.ConsentDTO;
import com.bsstandard.piece.data.repository.ConsentRepository;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * packageName    : com.bsstandard.piece.data.model
 * fileName       : ConsentViewModel
 * author         : piecejhm
 * date           : 2022/06/17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/17        piecejhm       최초 생성
 */
public class ConsentViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<ConsentDTO> consentData = new MutableLiveData<>();
    public final ConsentRepository consentRepository; // model 부분 - jhm 2022/06/17

    public ConsentViewModel(@NonNull Application application) {
        super(application);
        consentRepository = new ConsentRepository(application);
    }
    public MutableLiveData<ConsentDTO> getConsentData(){
        consentData = loadConsetData();
        return consentData;
    }
    public MutableLiveData<ConsentDTO> loadConsetData(){
        return consentRepository.getConsentData();
    }

}
