package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.BaseDTO;
import com.bsstandard.piece.data.repository.GetAuthTokenRepository;
import com.bsstandard.piece.widget.utils.LogUtil;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : GetAuthTokenViewModel
 * author         : piecejhm
 * date           : 2022/08/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/11        piecejhm       최초 생성
 */
public class GetAuthTokenViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<BaseDTO> getAuthTokenData = null;
    public final GetAuthTokenRepository getAuthTokenRepository;

    public GetAuthTokenViewModel(@NonNull Application application) {
        super(application);
        getAuthTokenRepository = new GetAuthTokenRepository(application);
    }

    public MutableLiveData<BaseDTO> getAuthTokenData() {
        if(getAuthTokenData == null) {
            getAuthTokenData = new MutableLiveData<>();
            getAuthTokenData = loadAuthTokenData();
        } else {
            LogUtil.logE("get auth token not null..");
            getAuthTokenData = loadAuthTokenData();
        }
        return getAuthTokenData;
    }
    public MutableLiveData<BaseDTO> loadAuthTokenData() { return getAuthTokenRepository.getAuthTokenData();}
}
