package com.bsstandard.piece.widget.utils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * packageName    : com.bsstandard.piece.utils
 * fileName       : CustomViewModelFactory
 * author         : piecejhm
 * date           : 2022/04/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */


public class CustomViewModelFactory<VM extends ViewModel> implements ViewModelProvider.Factory {

    private Lazy<VM> viewModel;

    @Inject
    public CustomViewModelFactory(Lazy<VM> viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public <VM extends ViewModel> VM create(Class<VM> modelClass) {
        if (modelClass.isAssignableFrom(viewModel.get().getClass())) {
            return (VM) viewModel.get();
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}