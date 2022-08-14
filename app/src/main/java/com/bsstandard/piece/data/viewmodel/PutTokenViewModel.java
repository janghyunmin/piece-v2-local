package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.App;
import com.bsstandard.piece.data.dto.PostTokenDTO;
import com.bsstandard.piece.data.repository.PutAuthTokenRepository;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : PutTokenViewModel
 * author         : piecejhm
 * date           : 2022/08/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/11        piecejhm       최초 생성
 */
public class PutTokenViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<PostTokenDTO> putAuthTokenData = null;
    public final PutAuthTokenRepository putAuthTokenRepository;

    public PutTokenViewModel(@NonNull Application application) {
        super(application);
        putAuthTokenRepository = new PutAuthTokenRepository(application);
    }

    public MutableLiveData<PostTokenDTO> putAuthTokenData() {
        if(putAuthTokenData == null) {
            putAuthTokenData = new MutableLiveData<>();
            putAuthTokenData = loadPutAuthTokenData();
        } else {
            LogUtil.logE("put auth token not null..");
            putAuthTokenData = loadPutAuthTokenData();
        }
        return putAuthTokenData;
    }

    public MutableLiveData<PostTokenDTO> loadPutAuthTokenData() { return putAuthTokenRepository.putAuthTokenData();}

}
