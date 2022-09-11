package com.bsstandard.piece.data.datasource.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bsstandard.piece.data.viewmodel.BookMarkViewModel;

/**
 * packageName    : com.bsstandard.piece.data.datasource.factory
 * fileName       : BookMarkViewModelFactory
 * author         : piecejhm
 * date           : 2022/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/30        piecejhm       최초 생성
 */

public class BookMarkViewModelFactory implements ViewModelProvider.Factory
{
    private Application application;

    public BookMarkViewModelFactory(Application application)
    {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass)
    {
        return (T) new BookMarkViewModel(application);
    }
}
