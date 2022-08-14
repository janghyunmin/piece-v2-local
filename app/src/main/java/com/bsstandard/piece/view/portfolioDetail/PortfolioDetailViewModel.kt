package com.bsstandard.piece.view.portfolioDetail

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsstandard.piece.widget.utils.Event
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import org.antlr.v4.runtime.misc.MurmurHash.finish


/**
 *packageName    : com.bsstandard.piece.view.portfolioDetail
 * fileName       : PortfolioDetailViewModel
 * author         : piecejhm
 * date           : 2022/08/12
 * description    : 포트폴리오 상세 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/12        piecejhm       최초 생성
 */

@HiltViewModel
class PortfolioDetailViewModel(application: Application) : AndroidViewModel(application) {
    val mApplication = application

}