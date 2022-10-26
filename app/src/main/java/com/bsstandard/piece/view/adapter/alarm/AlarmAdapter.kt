package com.bsstandard.piece.view.adapter.alarm

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.AlarmViewModel
import com.bsstandard.piece.databinding.AlarmItemBinding

/**
 *packageName    : com.bsstandard.piece.view.adapter.alarm
 * fileName       : AlarmAdapter
 * author         : piecejhm
 * date           : 2022/10/16
 * description    : 알림 및 혜택 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/16        piecejhm       최초 생성
 */


class AlarmAdapter(viewModel: AlarmViewModel, val context: Context) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private val alarmVm = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AlarmItemBinding =
            AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = alarmVm.getAlarmItem().size

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: AlarmAdapter.ViewHolder, position: Int) {

        holder.bind(alarmVm, position, listener)
        if(alarmVm.getAlarmItem()[position].isRead.equals("Y")) {

            holder.binding.notificationType.alpha = 0.5F
            holder.binding.title.alpha = 0.5F
            holder.binding.message.alpha = 0.5F
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, title: String, notificationType: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        @SuppressLint("Range")
        fun bind(
            viewModel: AlarmViewModel,
            pos: Int,
            listener: AlarmAdapter.OnItemClickListener?
        ) {
            binding.pos = pos
            binding.alarmVm = viewModel
            binding.executePendingBindings()

        }
    }
}