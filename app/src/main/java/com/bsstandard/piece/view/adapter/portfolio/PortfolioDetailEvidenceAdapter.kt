package com.bsstandard.piece.view.adapter.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.databinding.DocumentsItemLayoutBinding
import com.bsstandard.piece.databinding.ProductsItemLayoutBinding
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailViewModel
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide

/**
 *packageName    : com.bsstandard.piece.view.adapter.portfolio
 * fileName       : PortfolioDetailEvidenceAdapter
 * author         : piecejhm
 * date           : 2022/08/25
 * description    : 포트폴리오 상세 하단 증빙 구성 Recycler Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/25        piecejhm       최초 생성
 */


class PortfolioDetailEvidenceAdapter(viewModel: PortfolioDetailViewModel, val context: Context) :
    RecyclerView.Adapter<PortfolioDetailEvidenceAdapter.ViewHolder>() {

    private val portfolioDetailViewModel = viewModel

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioDetailEvidenceAdapter.ViewHolder {
        val binding: DocumentsItemLayoutBinding = DocumentsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioDetailViewModel.getPortfolioDocumentsList().size

    override fun onBindViewHolder(
        holder: PortfolioDetailEvidenceAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(portfolioDetailViewModel, position)
    }

    inner class ViewHolder(binding: DocumentsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        fun bind(viewModel: PortfolioDetailViewModel, pos: Int) {
            binding.pos = pos
            binding.portfolioDetailViewModel = viewModel
            binding.executePendingBindings()

            LogUtil.logE("size :  " + portfolioDetailViewModel.getPortfolioDocumentsList().size)

            Glide.with(context).load(portfolioDetailViewModel.getPortfolioDocumentsList()[pos].documentIconPath).into(binding.icon)
            binding.text.text = portfolioDetailViewModel.getPortfolioDocumentsList()[pos].documentName
        }
    }
}