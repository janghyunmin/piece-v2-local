package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.R
import com.bsstandard.piece.data.viewmodel.MagazineViewModel
import com.bsstandard.piece.databinding.FragmentMagazineBinding
import com.bsstandard.piece.view.adapter.magazine.MagazineAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMagazine
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 매거진 탭
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

@AndroidEntryPoint
class FragmentMagazine : Fragment() {

    private var _binding : FragmentMagazineBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: MagazineViewModel
    private var disposable: Disposable? = null

    companion object {
        fun newInstance(): FragmentMagazine {
            return FragmentMagazine()
        }
    }

    // 프래그먼트를 포함하고 있는 액티비티에 붙었을 때 - jhm 2022/07/15
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_magazine, container, false)

        vm = ViewModelProvider(this)[MagazineViewModel::class.java]
        vm.viewInit(binding.magazineRv)
        vm.getMagazine()
        binding.magazine = vm
        binding.lifecycleOwner = viewLifecycleOwner



        binding.tabs.setBackgroundColor(Color.parseColor("#FFFFFF"))
        binding.tabs.addTab(binding.tabs.newTab().setText("전체"))
        binding.tabs.addTab(binding.tabs.newTab().setText("포트폴리오"))
        binding.tabs.addTab(binding.tabs.newTab().setText("핀테크 트렌드"))
        binding.tabs.addTab(binding.tabs.newTab().setText("핫 플레이스"))
        binding.tabs.addTab(binding.tabs.newTab().setText("쿨 피플"))
        binding.tabs.addTab(binding.tabs.newTab().setText("잘알못 칼럼"))
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                LogUtil.logE("tab 재선택")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    LogUtil.logE("tab 선택" + tab.contentDescription)
                    when(tab.contentDescription) {
                        "전체" -> {
                            vm.getMagazine()
                        }
                        "포트폴리오" -> {
                            vm.getMagazinePortfolio()
                        }
                        "핀테크 트렌드" -> {
                            vm.getMagazineFintech()
                        }
                        "핫 플레이스" -> {
                            vm.getMagazinePlace()
                        }
                        "쿨 피플" -> {
                            vm.getMagazinePeople()
                        }
                        "잘알못 칼럼" -> {
                            vm.getMagazineJal()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                LogUtil.logE("tab 선택 안함")
            }
        })
        vm.magazineAdapter.setOnItemClickListener(object: MagazineAdapter.OnItemClickListener {
            override fun onItemClick(v: View, magazineId: String, magazineImagePath: String) {
                LogUtil.logE("magazineId $magazineId")
                LogUtil.logE("magazineThumnail ImagePath $magazineImagePath")

            }
        })



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //requireActivity().setStatusBarTransparent() // 상태 바 투명으로 설정(전체화면)
    }

    fun Activity.setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            )
        }
        if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = window.insetsController
        }
    }

    // 메모리에 올라갔을때 - jhm 2022/07/15
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}