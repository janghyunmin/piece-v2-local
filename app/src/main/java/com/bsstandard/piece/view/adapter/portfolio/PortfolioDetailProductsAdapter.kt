package com.bsstandard.piece.view.adapter.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.datamodel.dmodel.PortfolioDetailViewData
import com.bsstandard.piece.databinding.ProductsItemLayoutBinding
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailViewModel

/**
 *packageName    : com.bsstandard.piece.view.adapter.portfolio
 * fileName       : PortfolioDetailProductsAdapter
 * author         : piecejhm
 * date           : 2022/08/22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/22        piecejhm       최초 생성
 */
class PortfolioDetailProductsAdapter(viewModel: PortfolioDetailViewModel, val context: Context) :
    RecyclerView.Adapter<PortfolioDetailProductsAdapter.ViewHolder>() {

    private val portfolioDetailViewModel = viewModel

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioDetailProductsAdapter.ViewHolder {
        val binding: ProductsItemLayoutBinding = ProductsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioDetailViewModel.getPortfolioProductList().size

    override fun onBindViewHolder(
        holder: PortfolioDetailProductsAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(portfolioDetailViewModel, position)
    }

    inner class ViewHolder(binding: ProductsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        fun bind(viewModel: PortfolioDetailViewModel, pos: Int) {
            binding.pos = pos
            binding.portfolioDetailViewModel = viewModel
            binding.executePendingBindings()

            binding.title.text = "· " + portfolioDetailViewModel.detailResponse.value?.data?.products?.get(pos)?.title

        }
    }
}