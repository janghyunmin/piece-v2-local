package com.bsstandard.piece.view.join;

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
    private MutableLiveData<String>
            user_name , user_birth , user_mw , user_phone_select , user_phone;

//   public LiveData<String> getMutableData(){
//       if(user_name == null){
//           user_name = new MutableLiveData("");
//
//           return user_name;
//       }
//   }
}
