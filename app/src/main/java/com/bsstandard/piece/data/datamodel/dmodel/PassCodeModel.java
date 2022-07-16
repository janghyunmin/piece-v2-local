package com.bsstandard.piece.data.datamodel.dmodel;

import com.bsstandard.piece.view.passcode.PassCodeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.datamodel.dmodel
 * fileName       : PassCodeModel
 * author         : piecejhm
 * date           : 2022/06/27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/27        piecejhm       최초 생성
 */
public class PassCodeModel {

    private String passCode;

    public PassCodeModel(String passCode){
        this.passCode = passCode;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    @Override
    public String toString(){
        return "PassCode = " + passCode;
    }
}
