package com.bsstandard.piece.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datamodel.dmodel.portfolio.PortfolioList
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.FragmentHomeBinding
import com.bsstandard.piece.view.adapter.portfolio.PortfolioAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 *packageName    : com.bsstandard.piece.view.fragment
 * fileName       : FragmentMain
 * author         : piecejhm
 * date           : 2022/07/08
 * description    : 홈화면 탭이며 포트폴리오 목록 조회를 함 , homeViewModel 의존성 주입
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/08        piecejhm       최초 생성
 */

@AndroidEntryPoint
class FragmentHome : Fragment() {
    // arraylist 초기화 - jhm 2022/07/14
    var portfolioList: ArrayList<PortfolioList> = arrayListOf()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: PortfolioViewModel

    companion object {
        fun newInstance(): FragmentHome {
            return FragmentHome()
        }
    }

    // 메모리에 올라갔을때 - jhm 2022/07/15
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // 프래그먼트를 포함하고 있는 액티비티에 붙었을 때 - jhm 2022/07/15
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        vm = ViewModelProvider(this).get(PortfolioViewModel::class.java)
        vm.vewInit(binding.portfolioRv)
        vm.getPortfolio()
        binding.home = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}