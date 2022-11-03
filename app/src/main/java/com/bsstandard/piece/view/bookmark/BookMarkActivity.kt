package com.bsstandard.piece.view.bookmark

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRegModel
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRemoveModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.BaseDTO
import com.bsstandard.piece.data.dto.BookMarkDTO
import com.bsstandard.piece.data.viewmodel.BookMarkViewModel
import com.bsstandard.piece.databinding.ActivityBookmarkBinding
import com.bsstandard.piece.di.hilt.ApiModule
import com.bsstandard.piece.retrofit.RetrofitService
import com.bsstandard.piece.view.common.NetworkActivity
import com.bsstandard.piece.view.webview.MagazineDetailWebView
import com.bsstandard.piece.widget.utils.LocalObserver
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *packageName    : com.bsstandard.piece.view.bookmark
 * fileName       : BookMarkActivity
 * author         : piecejhm
 * date           : 2022/08/31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/31        piecejhm       최초 생성
 */

@AndroidEntryPoint
class BookMarkActivity : BaseActivity<ActivityBookmarkBinding>(R.layout.activity_bookmark) {
    private lateinit var bvm: BookMarkViewModel
    private var context: Context = this@BookMarkActivity
    private var observer = LocalObserver(lifecycle)
    val accessToken: String = PrefsHelper.read("accessToken", "")
    val deviceId: String = PrefsHelper.read("deviceId", "")
    val memberId: String = PrefsHelper.read("memberId", "")

    var layoutManager: RecyclerView.LayoutManager? = null
    private val bookMarkList: ArrayList<BookMarkDTO.Datum> = arrayListOf()
    val response: RetrofitService? = ApiModule.provideRetrofit().create(RetrofitService::class.java)

    @SuppressLint("NewApi", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LogUtil.logE("BookMarkActivity onCreate..")
        bvm = ViewModelProvider(this)[BookMarkViewModel::class.java]
        bvm.viewInit(binding.bookmarkRv)

        binding.memberBookMarkVm = bvm
        binding.lifecycleOwner = this@BookMarkActivity

        setStatusBarIconColor(true) // 상태바 아이콘 true : 검정색
        setStatusBarBgColor("#ffffff") // 상태바 배경색상 설정
        setNaviBarIconColor(true) // 네비게이션 true : 검정색
        setNaviBarBgColor("#ffffff") // 네비게이션 배경색


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected -> // 인터넷 연결되어있음 - jhm 2022/11/02
            if (isConnected) {
                bvm.getBookMark(accessToken = accessToken, deviceId = deviceId, memberId = memberId)
                bookMarkList.clear()

                // isFavorite 정보 받아와야함 - jhm 2022/08/31
                bvm.bookMarkAdapter.setOnItemClickListener(object :
                    BookMarkAdapter.OnItemClickListener {
                    override fun onItemClick(
                        v: View,
                        tag: String,
                        magazineId: String,
                        magazineImagePath: String,
                        isFavorite: Boolean,
                        smallTitle: String,
                        position: Int
                    ) {
                        LogUtil.logE("onClick..")
                        when (tag) {
                            "webView" -> {
                                val intent = Intent(context, MagazineDetailWebView::class.java)
                                intent.putExtra("magazineId", magazineId)
                                intent.putExtra("isFavorite", isFavorite)
                                intent.putExtra("pos", position)
                                startActivity(intent)
                                overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                );
                            }
                            "bookMark" -> {
                                LogUtil.logE("북마크 select..")

                                // 북마크 Post 요청시 필요 모델 - jhm 2022/08/29
                                val memberBookmarkRegModel =
                                    MemberBookmarkRegModel(memberId, magazineId)
                                // 북마크 Delete 요청시 필요 모델 - jhm 2022/08/29
                                val memberBookmarkRemoveModel =
                                    MemberBookmarkRemoveModel(memberId, magazineId)

                                LogUtil.logE("memberBookmarkRegModel : ${memberBookmarkRegModel.memberId}")
                                LogUtil.logE("memberBookmarkRegModel : ${memberBookmarkRegModel.magazineId}")
                                LogUtil.logE("v.isSelected : ${v.isSelected}")
                                LogUtil.logE("isFavorite : $isFavorite")


                                if (!isFavorite) {
                                    // deleteBookMark call - jhm 2022/08/29
                                    response?.deleteBookMark(
                                        "Bearer $accessToken",
                                        deviceId,
                                        memberId,
                                        memberBookmarkRemoveModel
                                    )?.enqueue(object : Callback<BaseDTO> {
                                        @SuppressLint("NotifyDataSetChanged")
                                        override fun onResponse(
                                            call: Call<BaseDTO>,
                                            response: Response<BaseDTO>
                                        ) {
                                            LogUtil.logE("bookmark delete call success.." + response.body()?.status)
                                            try {
                                                if (!response.body().toString().isEmpty()) {
                                                    LogUtil.logE("bookmark status : " + response.body()?.status)
                                                    LogUtil.logE("delete smallTitle : $smallTitle")

                                                    bvm.getBookMark(
                                                        accessToken = accessToken,
                                                        deviceId = deviceId,
                                                        memberId = memberId
                                                    )
                                                    bvm.bookMarkAdapter.notifyDataSetChanged()
                                                }
                                            } catch (ex: Exception) {
                                                ex.printStackTrace()
                                            }
                                        }

                                        override fun onFailure(call: Call<BaseDTO>, t: Throwable) {
                                            LogUtil.logE("bookmark post call fail.." + t.stackTrace)

                                        }
                                    })
                                }
                            }
                        }
                    }
                })
            } else {
                val intent = Intent(applicationContext, NetworkActivity::class.java)
                startActivity(intent)
            }
        }
    }


    /** Util start **/
    /**
     * 상태바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setStatusBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            window.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // minSdk 6.0부터 사용 가능
            window.decorView.systemUiVisibility = if (isBlack) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 기존 uiVisibility 유지
                window.decorView.systemUiVisibility
            }

        } // end if

    }

    /**
     * 상태바 배경 색상 지정
     * @param colorHexValue #ffffff 색상 값
     */
    private fun setStatusBarBgColor(colorHexValue: String) {

        // 상태바 배경색은 5.0부터 가능하나, 아이콘 색상은 6.0부터 변경 가능
        // -> 아이콘/배경색 모두 바뀌어야 의미가 있으므로 6.0으로 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.parseColor(colorHexValue)

        } // end if
    }

    /**
     * 내비바 아이콘 색상 지정
     * @param isBlack true : 검정색 / false : 흰색
     */
    private fun setNaviBarIconColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // android os 12에서 사용 가능

            window.insetsController?.let {
                it.setSystemBarsAppearance(
                    if (isBlack) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 아이콘 색상이 8.0부터 가능하므로 커스텀은 동시에 진행해야 하므로 조건 동일 처리.
            window.decorView.systemUiVisibility =
                if (isBlack) {
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

                } else {
                    // 기존 uiVisibility 유지
                    // -> 0으로 설정할 경우, 상태바 아이콘 색상 설정 등이 지워지기 때문
                    window.decorView.systemUiVisibility

                } // end if

        } // end if
    }

    /**
     * 내비바 배경 색상 설정
     * @param colorHexValue #ffffff 색상 값
     */
    private fun setNaviBarBgColor(colorHexValue: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 내비바 배경색은 8.0부터 지원한다.
            window.navigationBarColor = Color.parseColor(colorHexValue)

        } // end if

    }

}