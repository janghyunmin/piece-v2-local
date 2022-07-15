package com.bsstandard.piece.view.join;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * packageName    : com.bsstandard.piece.ui.join
 * fileName       : JoinViewModel
 * author         : piecejhm
 * date           : 2022/05/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/10        piecejhm       최초 생성
 */
public class JoinViewModel extends ViewModel {
    public final ObservableField<String> errorPhoneNumber = new ObservableField<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> userBirth = new MutableLiveData<>();
    public MutableLiveData<String> userGender = new MutableLiveData<>();
    public MutableLiveData<String> userAgency = new MutableLiveData<>();
    public MutableLiveData<String> userPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<Boolean> isNext = new MutableLiveData<>();


    public MutableLiveData<String> getUserName() {
        if(userName == null){
            userName = new MutableLiveData<String>();
        }
        return userName;
    }
    public MutableLiveData<String> getUserBirth() {
        if(userBirth == null){
            userBirth = new MutableLiveData<String>();
        }
        return userBirth;
    }
    public MutableLiveData<String> getUserGender() {
        if(userGender == null){
            userGender = new MutableLiveData<String>();
        }
        return userGender;
    }
    public MutableLiveData<String> getUserAgency() {
        if(userAgency == null){
            userAgency = new MutableLiveData<String>();
        }
        return userAgency;
    }
    public MutableLiveData<String> getUserPhoneNumber() {
        if(userPhoneNumber == null){
            userPhoneNumber = new MutableLiveData<String>();
        }
        return userPhoneNumber;
    }



}
