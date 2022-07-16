package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bsstandard.piece.App;
import com.bsstandard.piece.data.datamodel.dmodel.MemberPinModel;
import com.bsstandard.piece.data.dto.AuthPinDTO;
import com.bsstandard.piece.data.repository.AuthPinPutRepository;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : AuthPinPutViewModel
 * author         : piecejhm
 * date           : 2022/07/05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/05        piecejhm       최초 생성
 */
public class AuthPinPutViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public SingleLiveEvent<AuthPinDTO> authPinData = null;
    public final AuthPinPutRepository authPinPutRepository;

    public AuthPinPutViewModel(@NonNull Application application) {
        super(application);
        authPinPutRepository = new AuthPinPutRepository();
    }
    public SingleLiveEvent<AuthPinDTO> putCallAuthPinData(MemberPinModel memberPinModel) {
        if(authPinData == null) {
            LogUtil.logE("authPinData null");
            authPinData = new SingleLiveEvent<>();
            authPinData = loadPutPinData(memberPinModel);
        } else {
            LogUtil.logE("verifyData not null");
            authPinData = loadPutPinData(memberPinModel);
        }
        return authPinData;
    }
    public SingleLiveEvent<AuthPinDTO> loadPutPinData(MemberPinModel memberPinModel) {
        return authPinPutRepository.putAuthData(memberPinModel);
    }


}
