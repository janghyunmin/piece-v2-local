package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bsstandard.piece.data.dto.AuthPinDTO;
import com.bsstandard.piece.data.repository.AuthPinRepository;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : AuthPinViewModel
 * author         : piecejhm
 * date           : 2022/07/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/04        piecejhm       최초 생성
 */
public class AuthPinViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public SingleLiveEvent<AuthPinDTO> authData = null;
    public final AuthPinRepository authPinRepository;

    public AuthPinViewModel(@NonNull Application application) {
        super(application);
        authPinRepository = new AuthPinRepository();
    }

    public SingleLiveEvent<AuthPinDTO> getAuthData() {
        if(authData == null){
            authData = new SingleLiveEvent<>();
            authData = loadAuthData();
        }
        else {
            LogUtil.logE("auth data not null..");
            authData = loadAuthData();
        }
        return authData;
    }
    public SingleLiveEvent<AuthPinDTO> loadAuthData(){
        return authPinRepository.getAuthData();
    }

}
