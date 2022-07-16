package com.bsstandard.piece.data.datasource.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * packageName    : com.bsstandard.piece.data.datasource.factory
 * fileName       : JoinViewModelFactory
 * author         : piecejhm
 * date           : 2022/06/16
 * description    : 해당 팩토리 안씀
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/16        piecejhm       최초 생성
 */

public class JoinViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("팩토리 런타임 에러");
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("팩토리 런타임 에러2");
        }
    }
}
