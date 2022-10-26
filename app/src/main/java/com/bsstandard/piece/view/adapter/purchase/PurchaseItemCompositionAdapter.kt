package com.bsstandard.piece.view.adapter.purchase

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.databinding.DocumentItemCompositionLayoutBinding
import com.bsstandard.piece.view.portfolioDetail.PortfolioDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.products_item_composition_layout.view.*

/**
 *packageName    : com.bsstandard.piece.view.adapter.purchase
 * fileName       : PurchaseItemCompositionAdapter
 * author         : piecejhm
 * date           : 2022/10/12
 * description    : 내지갑 - 소유조각 증빙 구성 productDocuments Recycler Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/12        piecejhm       최초 생성
 */


class PurchaseItemCompositionAdapter(viewModel: PortfolioDetailViewModel, val context: Context) :
    RecyclerView.Adapter<PurchaseItemCompositionAdapter.ViewHolder>() {
    private val portfolioDetailViewModel = viewModel
    private var selectedItemPosition = -1
    private var selectedLayout: ConstraintLayout? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseItemCompositionAdapter.ViewHolder {
        val binding: DocumentItemCompositionLayoutBinding = DocumentItemCompositionLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioDetailViewModel.getProductDocumentList().size

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: PurchaseItemCompositionAdapter.ViewHolder, position: Int) {
        holder.bind(portfolioDetailViewModel, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(
            v: View,
            documentImagePath: String
        )
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: DocumentItemCompositionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0


        fun bind(viewModel: PortfolioDetailViewModel, pos: Int , listener: OnItemClickListener?) {
            binding.pos = pos
            binding.portfolioDetailViewModel = viewModel
            binding.executePendingBindings()

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(10))
            Glide.with(context)
                .load(portfolioDetailViewModel.detailResponse.value!!.data.products[pos].productDocuments[pos].documentImagePath)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.thumnaimPath)

            binding.itemTitle.text = portfolioDetailViewModel.detailResponse.value!!.data.products[pos].productDocuments[pos].documentName


            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView,
                            portfolioDetailViewModel.detailResponse.value!!.data.products[pos].productDocuments[pos].documentImagePath
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }
        }

        init {
            // 초기 Default setting - jhm 2022/08/24
            selectedItemPosition = 0
            selectedLayout = binding.root.item_view

        }
    }
}