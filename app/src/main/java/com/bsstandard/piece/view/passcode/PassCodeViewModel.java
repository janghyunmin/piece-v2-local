package com.bsstandard.piece.view.passcode;

import androidx.lifecycle.ViewModel;

import com.bsstandard.piece.data.datamodel.dmodel.PassCodeModel;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

import java.util.ArrayList;

/**
 * packageName    : com.bsstandard.piece.view.passcode
 * fileName       : PassCodeViewModel
 * author         : piecejhm
 * date           : 2022/06/27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/27        piecejhm       최초 생성
 */
public class PassCodeViewModel extends ViewModel {
    public SingleLiveEvent<String> Code0 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code1 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code2 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code3 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code4 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code5 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code6 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code7 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code8 = new SingleLiveEvent<>();
    public SingleLiveEvent<String> Code9 = new SingleLiveEvent<>();

    public SingleLiveEvent<ArrayList<PassCodeModel>> passCode = new SingleLiveEvent<>();

    public SingleLiveEvent<String> getCode0(){
        if(Code0 == null){
            Code0 = new SingleLiveEvent<>();
        }
        return Code0;
    }


    public SingleLiveEvent<String> getCode1(){
        if(Code1 == null){
            Code1 = new SingleLiveEvent<>();
        }
        return Code1;
    }

    public SingleLiveEvent<String> getCode2() {
        if(Code2 == null){
            Code2 = new SingleLiveEvent<>();
        }
        return Code2;
    }

    public SingleLiveEvent<String> getCode3() {
        if(Code3 == null){
            Code3 = new SingleLiveEvent<>();
        }
        return Code3;
    }

    public SingleLiveEvent<String> getCode4() {
        if(Code4 == null){
            Code4 = new SingleLiveEvent<>();
        }
        return Code4;
    }

    public SingleLiveEvent<String> getCode5() {
        if(Code5 == null){
            Code5 = new SingleLiveEvent<>();
        }
        return Code5;
    }

    public SingleLiveEvent<String> getCode6() {
        if(Code6 == null){
            Code6 = new SingleLiveEvent<>();
        }
        return Code6;
    }

    public SingleLiveEvent<String> getCode7() {
        if(Code7 == null){
            Code7 = new SingleLiveEvent<>();
        }
        return Code7;
    }

    public SingleLiveEvent<String> getCode8() {
        if(Code8 == null){
            Code8 = new SingleLiveEvent<>();
        }
        return Code8;
    }

    public SingleLiveEvent<String> getCode9() {
        if(Code9 == null){
            Code9 = new SingleLiveEvent<>();
        }
        return Code9;
    }


}
