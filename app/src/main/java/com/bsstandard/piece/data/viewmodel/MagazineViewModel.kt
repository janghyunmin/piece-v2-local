package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.dto.MagazineDTO
import com.bsstandard.piece.data.repository.MagazineRepository
import com.bsstandard.piece.view.adapter.magazine.MagazineAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MagazineViewModel
 * author         : piecejhm
 * date           : 2022/08/25
 * description    : 라운지 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/25        piecejhm       최초 생성
 */

@HiltViewModel
class MagazineViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MagazineRepository = MagazineRepository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var magazineAdapter = MagazineAdapter(this , context)
    private val magazineList: ArrayList<MagazineDTO.Data.Magazine> = arrayListOf()

    private val _isFavorite: MutableLiveData<String> = MutableLiveData()
    val isFavorite: LiveData<String>
        get() = _isFavorite


    var n = 1

    // 매거진 (비회원) 전체 list - jhm 2022/08/30
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getNoMemberMagazine(magazineType: String) {
        repo.getNoMemberMagazine(magazineType).subscribe(
            { MagazineDTO ->
                LogUtil.logE("매거진 갯수 : " + MagazineDTO.data.totalCount)

                magazineList.clear()
                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
                    magazineList.add(MagazineDTO.data.magazines[i])
                    LogUtil.logE("isFavorite : ${MagazineDTO.data.magazines[i].isFavorite}")

                    magazineAdapter.notifyDataSetChanged()
                }

            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + throwable.message) }
        )
    }


    // 매거진 전체 list - jhm 2022/08/28
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getMagazine(magazineType: String) {
        repo.getMagazine(magazineType).subscribe(
            { MagazineDTO ->
                magazineList.clear()
                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
                    magazineList.add(MagazineDTO.data.magazines[i])

                    _isFavorite.value = MagazineDTO.data.magazines[i].isFavorite
                    _isFavorite.postValue(MagazineDTO.data.magazines[i].isFavorite)

                    magazineAdapter.notifyDataSetChanged()
                }

            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + throwable.message) }
        )
    }


//    // 매거진 - 포트폴리오 list - jhm 2022/08/28
//    @SuppressLint("CheckResult","NotifyDataSetChanged")
//    fun getMagazinePortfolio() {
//        repo.getMagazinePortfolio().subscribe(
//            { MagazineDTO ->
//                LogUtil.logE("매거진 - 포트폴리오 갯수 : " + MagazineDTO.data.totalCount)
//
//                magazineList.clear()
//                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
//                    magazineList.add(MagazineDTO.data.magazines[i])
//                    magazineAdapter.notifyDataSetChanged()
//                }
//
//            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + "") }
//        )
//    }
//
//    // 매거진 - 핀테크 list - jhm 2022/08/28
//    @SuppressLint("CheckResult","NotifyDataSetChanged")
//    fun getMagazineFintech() {
//        repo.getMagazineFintech().subscribe(
//            { MagazineDTO ->
//                LogUtil.logE("매거진 - 핀테크 갯수 : " + MagazineDTO.data.totalCount)
//
//                magazineList.clear()
//                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
//                    magazineList.add(MagazineDTO.data.magazines[i])
//                    magazineAdapter.notifyDataSetChanged()
//                }
//
//            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + "") }
//        )
//    }
//
//    // 매거진 - 핫플레이스 list - jhm 2022/08/28
//    @SuppressLint("CheckResult","NotifyDataSetChanged")
//    fun getMagazinePlace() {
//        repo.getMagazinePlace().subscribe(
//            { MagazineDTO ->
//                LogUtil.logE("매거진 - 핫플레이스 갯수 : " + MagazineDTO.data.totalCount)
//
//                magazineList.clear()
//                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
//                    magazineList.add(MagazineDTO.data.magazines[i])
//                    magazineAdapter.notifyDataSetChanged()
//                }
//
//            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + "") }
//        )
//    }
//
//    // 매거진 - 쿨피플 list - jhm 2022/08/28
//    @SuppressLint("CheckResult","NotifyDataSetChanged")
//    fun getMagazinePeople() {
//        repo.getMagazinePeople().subscribe(
//            { MagazineDTO ->
//                LogUtil.logE("매거진 - 쿨피플 갯수 : " + MagazineDTO.data.totalCount)
//
//                magazineList.clear()
//                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
//                    magazineList.add(MagazineDTO.data.magazines[i])
//                    magazineAdapter.notifyDataSetChanged()
//                }
//
//            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + "") }
//        )
//    }
//
//    // 매거진 - 잘알못 list - jhm 2022/08/28
//    @SuppressLint("CheckResult","NotifyDataSetChanged")
//    fun getMagazineJal() {
//        repo.getMagazineJal().subscribe(
//            { MagazineDTO ->
//                LogUtil.logE("매거진 - 잘알못 갯수 : " + MagazineDTO.data.totalCount)
//
//                magazineList.clear()
//                for (i in ArrayList(MagazineDTO.data.magazines).indices) {
//                    magazineList.add(MagazineDTO.data.magazines[i])
//                    magazineAdapter.notifyDataSetChanged()
//                }
//
//            }, { throwable -> LogUtil.logE("라운지 리스트 GET List Error!" + "") }
//        )
//    }


    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = magazineAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    // 라운지 리스트 리턴 - jhm 2022/08/25
    fun getMagazineItem(): List<MagazineDTO.Data.Magazine> {
        return magazineList;
    }

}


object MagazineBindingAdapter {
    // Text - jhm 2022/08/26
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("app:magazineTitle")
    @JvmStatic
    fun loadTitle(
        titleTv: TextView,
        magazineTitle: String?, // 대제목 - jhm 2022/08/26
    ) {
        val _title = magazineTitle
        titleTv.text = _title
    }

    // Text - jhm 2022/08/26
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter( "app:magazineMidTitle")
    @JvmStatic
    fun loadMidTitle(
        titleTv: TextView,
        magazineMidTitle: String?, // 중제목 - jhm 2022/08/26
    ) {

        val _midTitle = magazineMidTitle
        titleTv.text = _midTitle
    }

    // Text - jhm 2022/08/26
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter( "app:magazineSmallTitle")
    @JvmStatic
    fun loadSmallTitle(
        titleTv: TextView,
        magazineSmallTitle: String?, // 중제목 - jhm 2022/08/26
    ) {

        val _smallTitle = magazineSmallTitle
        titleTv.text = _smallTitle
    }


    @BindingAdapter("app:representThumbnailPath")
    @JvmStatic
    fun loadImage(imageView: ImageView , imagePath: String ) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(10))
        val ImgPath: String = imagePath

        Glide.with(imageView.context)
            .load(ImgPath)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }


}