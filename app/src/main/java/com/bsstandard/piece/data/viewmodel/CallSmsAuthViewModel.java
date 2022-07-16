package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.datamodel.dmodel.SmsAuthModel;
import com.bsstandard.piece.data.dto.CallSmsAuthDTO;
import com.bsstandard.piece.data.repository.CallSmsAuthRepository;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : CallSmsAuthViewModel
 * author         : piecejhm
 * date           : 2022/06/22
 * description    : /member/call_sms_auth ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/22        piecejhm       최초 생성
 */
public class CallSmsAuthViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<CallSmsAuthDTO> callSmsAuthData = null;
    public final CallSmsAuthRepository callSmsAuthRepository;

    public CallSmsAuthViewModel(@NonNull Application application){
        super(application);
        callSmsAuthRepository = new CallSmsAuthRepository();
    }
    public MutableLiveData<CallSmsAuthDTO> postCallSmsAuthData(SmsAuthModel smsAuthModel){
        if(callSmsAuthData == null){
            callSmsAuthData = new MutableLiveData<>();
            callSmsAuthData = loadSmsData(smsAuthModel);
        }
        return callSmsAuthData;
    }
    public MutableLiveData<CallSmsAuthDTO> loadSmsData(SmsAuthModel smsAuthModel){
        return callSmsAuthRepository.getCallSmsAuthData(smsAuthModel);
    }

    public MutableLiveData<CallSmsAuthDTO> getSmsAuthData(){
        return callSmsAuthRepository.getSmsAuthData();
    }

}
