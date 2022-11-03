package com.bsstandard.piece.view.adapter.deposit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.R
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

        when (historyVm.getDepositHistoryItem()[position].changeReason) {
            // 예치금 입금 - jhm 2022/11/02
            "MDR0101" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.VISIBLE
            }
            // 예치금 출금 - jhm 2022/11/02
            "MDR0102" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_131313))
                holder.binding.remainAmount.visibility = View.VISIBLE
            }
            // 분배금 입금 - jhm 2022/11/02
            "MDR0103" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.GONE
            }
            // 분배 수수료 - jhm 2022/11/02
            "MDR0104" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_131313))
                holder.binding.remainAmount.visibility = View.GONE
            }
            // 분배금 입금 - jhm 2022/11/02
            "MDR0105" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.GONE
            }
            // 조각 구매 - jhm 2022/11/02
            "MDR0201" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_131313))
                holder.binding.remainAmount.visibility = View.GONE
            }
            // 구매 취소 - jhm 2022/11/02
            "MDR0202" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.GONE
            }
            // 조각 판매 - jhm 2022/11/02
            "MDR0203" -> {

            }
            // 부가가치세 - jhm 2022/11/02
            "MDR0204" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.GONE
            }

            // 부가가치세 환급 - jhm 2022/11/02
            "MDR0205" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.GONE
            }

            // 예치금 출금 신청 - jhm 2022/11/02
            "MDR0301" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_131313))
                holder.binding.remainAmount.visibility = View.GONE
            }

            // 예치금 출금 완료  - jhm 2022/11/02
            "MDR0306" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_131313))
                holder.binding.remainAmount.visibility = View.VISIBLE
            }

            // 예치금 출금 실패 - jhm 2022/11/02
            "MDR0307" -> {
                holder.binding.changeAmount.setTextColor(context.getColorStateList(R.color.c_10cfc9))
                holder.binding.remainAmount.visibility = View.VISIBLE
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