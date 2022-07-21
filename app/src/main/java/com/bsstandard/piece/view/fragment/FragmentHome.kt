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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datamodel.dmodel.portfolio.PortfolioList
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.FragmentHomeBinding
import com.bsstandard.piece.view.adapter.portfolio.PortfolioAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.portfolio_item.*


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

class FragmentHome : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: PortfolioViewModel

    companion object {
        fun newInstance(): FragmentHome {
            return FragmentHome()
        }
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


        vm = ViewModelProvider(this)[PortfolioViewModel::class.java]
        vm.vewInit(binding.portfolioRv)
        vm.getPortfolio()
        binding.home = vm
        binding.lifecycleOwner = viewLifecycleOwner


        vm.portfolioAdapter.setOnItemClickListener(object : PortfolioAdapter.OnItemClickListener {
            override fun onItemClick(v: View, portfolioId: String, portfolioImagePath: String) {
                LogUtil.logE("portfolioId $portfolioId")
                LogUtil.logE("portfolioImagePath $portfolioImagePath")

                val extras = FragmentNavigatorExtras(
                    portfolio_img to id.toString()
                )
                val action = FragmentHomeDirections.navToPortoflioDetailActivity(portfolioImagePath)
                findNavController().navigate(action, extras)

            }
        })

        return binding.root
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