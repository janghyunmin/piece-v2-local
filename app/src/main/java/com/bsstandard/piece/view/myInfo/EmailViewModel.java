package com.bsstandard.piece.view.myInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * packageName    : com.bsstandard.piece.view.myInfo
 * fileName       : EmailViewModel
 * author         : piecejhm
 * date           : 2022/10/17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/17        piecejhm       최초 생성
 */


public class EmailViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        if(email == null) {
            email = new MutableLiveData<String>();
        }
        return email;
    }
}
