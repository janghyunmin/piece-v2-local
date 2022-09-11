package com.bsstandard.piece.view.adapter.event

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.EventViewModel
import com.bsstandard.piece.databinding.EventItemBinding

/**
 *packageName    : com.bsstandard.piece.view.adapter.event
 * fileName       : EventAdapter
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */
class EventAdapter(viewModel: EventViewModel, val context: Context) :
RecyclerView.Adapter<EventAdapter.ViewHolder>(){
    private val eventViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val binding: EventItemBinding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = eventViewModel.getEventItem().size

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        holder.bind(eventViewModel, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, eventId: String, eventImagePath: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(viewModel: EventViewModel, pos: Int, listener: OnItemClickListener?) {
            binding.pos = pos
            binding.eventViewModel = viewModel
            binding.executePendingBindings()

            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView, eventViewModel.getEventItem().get(pos).eventId,
                            eventViewModel.getEventItem()
                                .get(pos).representThumbnailPath
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }
        }
    }
}