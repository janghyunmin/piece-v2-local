package com.bsstandard.piece.view.portfolioDetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.ImgTestBinding
import com.bsstandard.piece.view.main.MainActivity
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.disposables.Disposable

/**
 *packageName    : com.bsstandard.piece.view.portfolioDetail
 * fileName       : PortfolioDetailFragment
 * author         : piecejhm
 * date           : 2022/10/31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/31        piecejhm       최초 생성
 */

class PortfolioDetailFragment : Fragment(){
    private var _binding: ImgTestBinding? = null
    private val binding get() = _binding!!

    val args: PortfolioDetailFragmentArgs by navArgs()
    private var disposable: Disposable? = null
    // 1. Context를 할당할 변수를 프로퍼티로 선언(어디서든 사용할 수 있게)
    lateinit var mainActivity: MainActivity


    companion object {
        fun newInstance(): PortfolioDetailFragment {
            return PortfolioDetailFragment()
        }
    }

    // 프래그먼트를 포함하고 있는 액티비티에 붙었을 때 - jhm 2022/07/15
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    // 메모리 누수 방지를 위해 자원 해제 - jhm 2022/08/08
    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        sharedElementEnterTransition = TransitionInflater.from(mainActivity).inflateTransition(R.transition.change_bounds)
//        sharedElementReturnTransition =  TransitionInflater.from(mainActivity).inflateTransition(R.transition.change_bounds)

        _binding = DataBindingUtil.inflate(inflater,R.layout.img_test,container,false)



        val portfolioId = args.portfolioId
        LogUtil.logE("넘겨받은 portfolioId : $portfolioId")

        val portfolioPath = args.portfolioImagePath
        LogUtil.logE("넘겨받은 portfolioImagePath $portfolioPath")



        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)

//        ViewCompat.setTransitionName(portfolio_img,portfolioPath)

        Glide.with(this)
            .load(portfolioPath)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.portfolioImg)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.logE("화면 파괴")
        _binding = null
    }

}