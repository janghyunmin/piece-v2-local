package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bsstandard.piece.data.datamodel.dmodel.SmsAuthModel;
import com.bsstandard.piece.data.dto.SmsVerificationDTO;
import com.bsstandard.piece.data.repository.VerifyRepository;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : VerifyViewModel
 * author         : piecejhm
 * date           : 2022/06/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/24        piecejhm       최초 생성
 */
public class VerifyViewModel extends AndroidViewModel {
    // 한번씩만 실행하게 SingleLiveEvent 사용 - jhm 2022/06/24
    @SuppressWarnings({"FieldCanBeLocal"})
    public SingleLiveEvent<SmsVerificationDTO> verifyData = null;
    public final VerifyRepository verifyRepository;

    public VerifyViewModel(@NonNull Application application){
        super(application);
        verifyRepository = new VerifyRepository(application);
    }
    public SingleLiveEvent<SmsVerificationDTO> postCallVerifyData(SmsAuthModel smsAuthModel) {
        if(verifyData == null){
            LogUtil.logE("verifyData null");
            verifyData = new SingleLiveEvent<>();
            verifyData = loadVerifyData(smsAuthModel);
        } else {
            LogUtil.logE("verifyData not null");
            verifyData = loadVerifyData(smsAuthModel);
        }
        return verifyData;
    }
    public SingleLiveEvent<SmsVerificationDTO> loadVerifyData(SmsAuthModel smsAuthModel) {
        return verifyRepository.getVerifyData(smsAuthModel);
    }


}
