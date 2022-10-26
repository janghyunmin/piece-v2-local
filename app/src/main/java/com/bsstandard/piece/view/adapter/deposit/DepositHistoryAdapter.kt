package com.bsstandard.piece.view.adapter.deposit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.DepositHistoryViewModel
import com.bsstandard.piece.databinding.DepositDetailItemBinding

/**
 *packageName    : com.bsstandard.piece.view.adapter.deposit
 * fileName       : DepositHistoryAdapter
 * author         : piecejhm
 * date           : 2022/09/28
 * description    : 회원 거래 내역 조회 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/28        piecejhm       최초 생성
 */


class DepositHistoryAdapter(viewModel: DepositHistoryViewModel, val context: Context) :
    RecyclerView.Adapter<DepositHistoryAdapter.ViewHolder>() {
    private val historyVm = viewModel

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DepositHistoryAdapter.ViewHolder {
        val binding: DepositDetailItemBinding =
            DepositDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = historyVm.getDepositHistoryItem().size


    override fun onBindViewHolder(holder: DepositHistoryAdapter.ViewHolder, position: Int) {
        holder.bind(historyVm, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, portfolioId: String, portfolioImagePath: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: DepositDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(
            viewModel: DepositHistoryViewModel,
            pos: Int,
            listener: DepositHistoryAdapter.OnItemClickListener?
        ) {
            binding.pos = pos
            binding.historyVm = viewModel
            binding.executePendingBindings()

        }
    }
}