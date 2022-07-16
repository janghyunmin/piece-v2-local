package com.bsstandard.piece.data.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * packageName    : com.bsstandard.piece.view.join.dialog
 * fileName       : SmsViewModel
 * author         : piecejhm
 * date           : 2022/06/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/24        piecejhm       최초 생성
 */
public class SmsViewModel extends ViewModel {
    public MutableLiveData<String> verifyNum = new MutableLiveData<>();

    public MutableLiveData<String> getVerifyNum() {
        if(verifyNum == null){
            verifyNum = new MutableLiveData<>();
        }
        return verifyNum;
    }
}
