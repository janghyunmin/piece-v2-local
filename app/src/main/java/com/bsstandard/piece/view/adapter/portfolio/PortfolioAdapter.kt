package com.bsstandard.piece.view.adapter.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.PortfolioItemBinding
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.portfolio_item.view.*


/**
 *packageName    : com.bsstandard.piece.view.adapter.portfolio
 * fileName       : PortfolioAdapter
 * author         : piecejhm
 * date           : 2022/07/14
 * description    : 포트폴리오 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/14        piecejhm       최초 생성
 */

class PortfolioAdapter(viewModel: PortfolioViewModel, val context: Context) :
    RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {
    private val portfolioViewModel = viewModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioAdapter.ViewHolder {
        val binding: PortfolioItemBinding =
            PortfolioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioViewModel.getPortoflioItem().size

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: PortfolioAdapter.ViewHolder, position: Int) {
        holder.bind(portfolioViewModel, position, listener)

        holder.itemView.run {
            when (portfolioViewModel.getPortoflioItem()[position].recruitmentState) {
                // 오픈 예정 - jhm 2022/11/02
                "PRS0101" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0101)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                // 판매 중 - jhm 2022/11/02
                "PRS0102" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0102)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                // 조각 완판 - jhm 2022/11/02
                "PRS0103" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0103)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                // 매각 대기 - jhm 2022/11/02
                "PRS0104" -> {
                    LogUtil.logE("매각 대기")
                }
                // 매각 진행 - jhm 2022/11/02
                "PRS0105" -> {
                    LogUtil.logE("매각 진행")
                }
                // 매각 완료 - jhm 2022/11/02
                "PRS0106" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0106)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                // 정산중 - jhm 2022/11/02
                "PRS0107" -> {
                    LogUtil.logE("정산중")
                }
                // 분배 완료 - jhm 2022/11/02
                "PRS0108" -> {
                    LogUtil.logE("분배완료")
                }
                // 일시 중지 - jhm 2022/11/02
                "PRS0109" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0109)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                // 기한 만료 - jhm 2022/11/02
                "PRS0110" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0110)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                // 수익 분배 - jhm 2022/11/02
                "PRS0111" -> {
                    Glide.with(context)
                        .load(R.drawable.prs0111)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(portfolio_status)
                }
                else -> {
                    LogUtil.logE("Image Error!")
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, portfolioId: String, portfolioImagePath: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: PortfolioItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(viewModel: PortfolioViewModel, pos: Int, listener: OnItemClickListener?) {
            binding.pos = pos
            binding.portfolioViewModel = viewModel
            ViewCompat.setTransitionName(
                binding.portfolioImg,
                viewModel.getPortoflioItem()[pos].representThumbnailImagePath
            )

            binding.executePendingBindings()

            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView,
                            portfolioViewModel.getPortoflioItem()[pos].portfolioId,
                            portfolioViewModel.getPortoflioItem()[pos].representThumbnailImagePath
                        )
//                        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(context, R.anim.portfolio_img_anim)
//                        itemView.startAnimation(hyperspaceJump)


//                        itemView.animate().translationY(-1000F).duration = 1000
//                        itemView.animate().scaleX(0.9F).duration = 1000
//                        itemView.animate().scaleY(0.9F).duration = 1000

                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }

        }
    }
}