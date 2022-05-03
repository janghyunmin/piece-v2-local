package com.bsstandard.piece.base

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsstandard.piece.widget.utils.ErrorType
import com.bsstandard.piece.widget.utils.RemoteErrorEmitter
import com.bsstandard.piece.widget.utils.ScreenState
import com.bsstandard.piece.widget.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.base
 * fileName       : BaseViewModel
 * author         : piecejhm
 * date           : 2022/04/27
 * description    : BaseViewModel 공통 모듈
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/27        piecejhm       최초 생성
 */

abstract class BaseViewModel : ViewModel(), RemoteErrorEmitter {

    val mutableProgress = MutableLiveData<Int>(View.GONE)
    val mutableScreenState = SingleLiveEvent<ScreenState>()
    val mutableErrorMessage = SingleLiveEvent<String>()
    val mutableSuccessMessage = MutableLiveData<String>()
    val mutableErrorType = SingleLiveEvent<ErrorType>()


    override fun onError(errorType: ErrorType) {
        mutableErrorType.postValue(errorType)
    }

    override fun onError(msg: String) {
        mutableErrorMessage.postValue(msg)
    }
}