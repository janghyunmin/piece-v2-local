package com.bsstandard.piece.view.adapter.purchase

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.PurchaseViewModel
import com.bsstandard.piece.databinding.PurchaseItemBinding

/**
 *packageName    : com.bsstandard.piece.view.adapter.purchase
 * fileName       : PurchaseAdapter
 * author         : piecejhm
 * date           : 2022/10/11
 * description    : 회원 소유조각 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/11        piecejhm       최초 생성
 */


class PurchaseAdapter(viewModel: PurchaseViewModel, val context: Context) :
    RecyclerView.Adapter<PurchaseAdapter.ViewHolder>() {
    private val purchaseVm = viewModel


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseAdapter.ViewHolder {
        val binding: PurchaseItemBinding =
            PurchaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = purchaseVm.getPurchaseItem().size

    override fun onBindViewHolder(holder: PurchaseAdapter.ViewHolder, position: Int) {
        holder.bind(purchaseVm, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(
            v: View,
            purchaseAt: String,
            purchasePieceVolume: String,
            purchaseTotalAmount: String,
            purchaseId: String,
            portfolioId: String,
            portfolioImagePath: String,
            isCoupon: String,
            isConfirm: String
        )
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: PurchaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(
            viewModel: PurchaseViewModel,
            pos: Int,
            listener: OnItemClickListener?
        ) {
            binding.pos = pos
            binding.purchaseVm = viewModel
            binding.executePendingBindings()

            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView,
                            purchaseVm.getPurchaseItem()[pos].purchaseAt,
                            purchaseVm.getPurchaseItem()[pos].purchasePieceVolume.toString(),
                            purchaseVm.getPurchaseItem()[pos].purchaseTotalAmount.toString(),
                            purchaseVm.getPurchaseItem()[pos].purchaseId,
                            purchaseVm.getPurchaseItem()[pos].portfolioId,
                            purchaseVm.getPurchaseItem()[pos].portfolio.representThumbnailImagePath,
                            purchaseVm.getPurchaseItem()[pos].isCoupon,
                            purchaseVm.getPurchaseItem()[pos].isConfirm
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }

        }
    }
}