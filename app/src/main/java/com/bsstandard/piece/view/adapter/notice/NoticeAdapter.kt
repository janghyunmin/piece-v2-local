package com.bsstandard.piece.view.adapter.notice

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.BoardViewModel
import com.bsstandard.piece.databinding.NoticeItemBinding

/**
 *packageName    : com.bsstandard.piece.view.adapter.notice
 * fileName       : NoticeAdapter
 * author         : piecejhm
 * date           : 2022/09/10
 * description    : 공지사항 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/10        piecejhm       최초 생성
 */

class NoticeAdapter(viewModel: BoardViewModel, val context: Context) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {
    private val noticeViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.ViewHolder {
        val binding: NoticeItemBinding =
            NoticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = noticeViewModel.getNoticeItem().size

    override fun onBindViewHolder(holder: NoticeAdapter.ViewHolder, position: Int) {
        holder.bind(noticeViewModel, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, boardId: String, boradTitle: String, date: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: NoticeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(viewModel: BoardViewModel, pos: Int, listener: OnItemClickListener?) {
            binding.pos = pos
            binding.noticeVm = viewModel
            binding.executePendingBindings()

            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView,
                            noticeViewModel.getNoticeItem().get(pos).boardId,
                            noticeViewModel.getNoticeItem().get(pos).title,
                            noticeViewModel.getNoticeItem().get(pos).createdAt
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }
        }
    }
}