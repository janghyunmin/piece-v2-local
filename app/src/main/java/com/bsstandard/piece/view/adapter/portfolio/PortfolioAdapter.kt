package com.bsstandard.piece.view.adapter.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datamodel.dmodel.portfolio.PortfolioList
import com.bsstandard.piece.data.dto.portfolio.PortfolioDTO
import com.bsstandard.piece.data.viewmodel.PortfolioViewModel
import com.bsstandard.piece.databinding.PortfolioItemBinding
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide

/**
 *packageName    : com.bsstandard.piece.view.adapter.portfolio
 * fileName       : PortfolioAdapter
 * author         : piecejhm
 * date           : 2022/07/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/14        piecejhm       최초 생성
 */

class PortfolioAdapter(viewModel: PortfolioViewModel) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {
    private val portfolioViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioAdapter.ViewHolder {
        val binding: PortfolioItemBinding = PortfolioItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioViewModel.getPortoflioItem().size

    override fun onBindViewHolder(holder: PortfolioAdapter.ViewHolder, position: Int) {
        holder.bind(portfolioViewModel, position)
    }


    inner class ViewHolder(binding: PortfolioItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        fun bind(viewModel: PortfolioViewModel, pos:Int) {
            binding.pos = pos
            binding.portfolioViewModel = viewModel
            binding.executePendingBindings()
        }
    }
}