package com.bsstandard.piece.view.adapter.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.databinding.PurchaseGuideItemLayoutBinding
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailViewModel
import com.bumptech.glide.Glide

/**
 *packageName    : com.bsstandard.piece.view.adapter.portfolio
 * fileName       : PortfolioDetailPurchaseAdapter
 * author         : piecejhm
 * date           : 2022/08/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/18        piecejhm       최초 생성
 */
class PortfolioDetailPurchaseAdapter(viewModel: PortfolioDetailViewModel, val context: Context) :
    RecyclerView.Adapter<PortfolioDetailPurchaseAdapter.ViewHolder>() {

    private val portfolioDetailViewModel = viewModel

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioDetailPurchaseAdapter.ViewHolder {
        val binding: PurchaseGuideItemLayoutBinding = PurchaseGuideItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    // 구매포인트 array size return - jhm 2022/08/18
    override fun getItemCount(): Int = portfolioDetailViewModel.getPortfolioPurchasGuideList().size

    override fun onBindViewHolder(
        holder: PortfolioDetailPurchaseAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(portfolioDetailViewModel,position)
    }

    inner class ViewHolder(binding: PurchaseGuideItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        @SuppressLint("SetTextI18n")
        fun bind(viewModel: PortfolioDetailViewModel, pos: Int) {
            binding.pos = pos
            binding.portfolioDetailViewModel = viewModel
            binding.executePendingBindings()

            // 구매 포인트 icon set - jhm 2022/08/18
            Glide.with(context).load(portfolioDetailViewModel.getPortfolioPurchasGuideList().get(pos).guideIconPath).into(binding.icon)
            // 구매 포인트 text set - jhm 2022/08/18
            binding.title.text = portfolioDetailViewModel.detailResponse.value?.data?.purchaseGuides?.get(pos)?.guideName
        }
    }
}