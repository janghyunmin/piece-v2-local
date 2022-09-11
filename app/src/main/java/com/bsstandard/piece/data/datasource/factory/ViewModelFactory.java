package com.bsstandard.piece.data.datasource.factory;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bsstandard.piece.data.viewmodel.BookMarkViewModel;
import com.bsstandard.piece.data.viewmodel.VersionViewModel;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * packageName    : com.bsstandard.piece.data.datasource.factory
 * fileName       : ViewModelFactory
 * author         : piecejhm
 * date           : 2022/06/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/13        piecejhm       최초 생성
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public ViewModelFactory(Application application){
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass){
        if(aClass.isAssignableFrom(VersionViewModel.class)) {
            return (T) new VersionViewModel(application);
        }
        throw new IllegalArgumentException("Unkown ViewModel Class..");
    }
}
