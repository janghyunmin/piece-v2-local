package com.bsstandard.piece.view.adapter.portfolio

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.PortfolioItemBinding
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailActivity
import com.bsstandard.piece.widget.utils.LogUtil


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

    override fun onBindViewHolder(holder: PortfolioAdapter.ViewHolder, position: Int) {
        holder.bind(portfolioViewModel, position, listener)
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
            binding.executePendingBindings()

            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView, portfolioViewModel.getPortoflioItem().get(pos).portfolioId,
                            portfolioViewModel.getPortoflioItem()
                                .get(pos).representThumbnailImagePath
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }

        }
    }
}