package com.bsstandard.piece.view.adapter.portfolio

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.PortfolioDetailDTO
import com.bsstandard.piece.databinding.ProductsItemCompositionLayoutBinding
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailViewModel
import com.bsstandard.piece.widget.utils.LogUtil
import com.bsstandard.piece.widget.utils.SingleLiveEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.products_item_composition_layout.view.*
import kotlinx.android.synthetic.main.slide_up_dialog_base.view.*


/**
 *packageName    : com.bsstandard.piece.view.adapter.portfolio
 * fileName       : PortfolioDetailCompositionAdapter
 * author         : piecejhm
 * date           : 2022/08/22
 * description    : 포트폴리오 상세 하단 구성 Recycler Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/22        piecejhm       최초 생성
 */


class PortfolioDetailCompositionAdapter(viewModel: PortfolioDetailViewModel, val context: Context) :
    RecyclerView.Adapter<PortfolioDetailCompositionAdapter.ViewHolder>() {
    private val portfolioDetailViewModel = viewModel

    private var selectedItemPosition = -1
    private var selectedLayout: ConstraintLayout? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioDetailCompositionAdapter.ViewHolder {
        val binding: ProductsItemCompositionLayoutBinding = ProductsItemCompositionLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioDetailViewModel.getPortfolioCompositionList().size

    override fun onBindViewHolder(
        holder: PortfolioDetailCompositionAdapter.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.bind(portfolioDetailViewModel, position)
        if (position == selectedItemPosition) {
            holder.binding.itemView.background = context.getDrawable(R.drawable.shape_selected)
        } else {
            holder.binding.itemView.background = context.getDrawable(R.drawable.shape_unselected)
        }

        // Item Initialize
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)

            selectedItemPosition = position
            selectedLayout = holder.binding.itemView
            selectedLayout?.background = context.getDrawable(R.drawable.shape_selected)
            holder.binding.itemTitle.setTextColor(context.getColor(R.color.white))

        }

    }

    inner class ViewHolder(binding: ProductsItemCompositionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        fun bind(viewModel: PortfolioDetailViewModel, pos: Int) {
            binding.pos = pos
            binding.portfolioDetailViewModel = viewModel
            binding.executePendingBindings()

            binding.itemTitle.text = portfolioDetailViewModel.detailResponse.value?.data?.products?.get(pos)?.title
        }

        init {
            // 초기 Default setting - jhm 2022/08/24
            selectedItemPosition = 0
            selectedLayout = binding.root.item_view
            selectedLayout?.background = context.getDrawable(R.drawable.shape_selected)
            binding.itemTitle.setTextColor(context.getColor(R.color.white))

        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener


}